package org.imesense.dynamicspawncontrol.ai.zombie.event;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import static org.imesense.dynamicspawncontrol.ai.zombie.shield.ZombieShieldConfig.*;
import static org.imesense.dynamicspawncontrol.ai.zombie.shield.ZombieShieldEffects.*;
import static org.imesense.dynamicspawncontrol.ai.zombie.shield.ZombieShieldNBT.*;
import static org.imesense.dynamicspawncontrol.ai.zombie.shield.ZombieShieldUtils.*;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class ZombieHasShieldEventHandler
{
    private static volatile ZombieHasShieldEventHandler _INSTANCE;

    public static ZombieHasShieldEventHandler getInstance()
    {
        return CodeGeneric.getInstance(ZombieHasShieldEventHandler.class);
    }

    public ZombieHasShieldEventHandler()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    @SubscribeEvent
    public void handleEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityZombie)
        {
            EntityZombie zombie = (EntityZombie) event.getEntity();
            initShieldData(zombie);

            if (USE_RANDOM_SHIELD_STATE)
            {
                ItemStack mainHand = zombie.getHeldItem(EnumHand.MAIN_HAND);

                if (!mainHand.isEmpty() && mainHand.getItem() instanceof ItemShield)
                {
                    int max = mainHand.getMaxDamage();
                    int remaining = 1 + zombie.getRNG().nextInt(max);

                    mainHand.setItemDamage(max - remaining);

                    return;
                }

                ItemStack offHand = zombie.getHeldItem(EnumHand.OFF_HAND);

                if (!offHand.isEmpty() && offHand.getItem() instanceof ItemShield)
                {
                    int max = offHand.getMaxDamage();
                    int remaining = 1 + zombie.getRNG().nextInt(max);
                    offHand.setItemDamage(max - remaining);
                }
            }
        }
    }

    @SubscribeEvent
    public void handleLivingHurt(LivingHurtEvent event)
    {
        if (!(event.getEntityLiving() instanceof EntityZombie)) return;

        EntityZombie zombie = (EntityZombie) event.getEntityLiving();
        ItemStack shield = getShieldItem(zombie);

        checkAndRemoveBrokenShield(zombie, shield);

        shield = getShieldItem(zombie);

        if (shield == null)
        {
            return;
        }

        NBTTagCompound dscData = getDSCData(zombie);
        float shieldHealth = dscData.getFloat(SHIELD_HEALTH);
        int cooldown = dscData.getInteger(SHIELD_COOLDOWN);

        if (cooldown > 0)
        {
            return;
        }

        if (shieldHealth <= 0)
        {
            if (DESTROY_SHIELD_ON_BREAK)
            {
                checkAndRemoveBrokenShield(zombie, shield);
            }

            return;
        }

        if (!isAttackFromFront(zombie, event.getSource()))
        {
            return;
        }

        float damage = event.getAmount();
        float maxBlock = shieldHealth;
        float blocked = Math.min(damage * BLOCK_PERCENTAGE, maxBlock);

        shieldHealth -= blocked;
        dscData.setFloat(SHIELD_HEALTH, shieldHealth);

        if (shieldHealth <= 0)
        {
            dscData.setInteger(SHIELD_COOLDOWN, SHIELD_BREAK_COOLDOWN);

            if (ENABLE_SOUNDS)
            {
                zombie.world.playSound(null, zombie.posX, zombie.posY, zombie.posZ,
                        net.minecraft.init.SoundEvents.ENTITY_ITEM_BREAK,
                        SoundCategory.HOSTILE, 1.0F, 1.0F);
            }

            if (DESTROY_SHIELD_ON_BREAK)
            {
                checkAndRemoveBrokenShield(zombie, shield);
            }
        }
        else
        {
            dscData.setInteger(SHIELD_COOLDOWN, BLOCK_COOLDOWN);
            dscData.setLong(LAST_HIT_TIME, zombie.world.getTotalWorldTime());

            playBlockEffects(zombie, blocked / damage);
        }

        float newDamage = damage - blocked;
        event.setAmount(newDamage);

        knockbackAttacker(zombie, event.getSource());

        int shieldDamage = (int) (blocked * SHIELD_DAMAGE_MULTIPLIER);
        shield.damageItem(shieldDamage, zombie);

        checkAndRemoveBrokenShield(zombie, shield);
    }

    @SubscribeEvent
    public void handleLivingUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if (!(event.getEntityLiving() instanceof EntityZombie)) return;

        EntityZombie zombie = (EntityZombie) event.getEntityLiving();
        ItemStack shield = getShieldItem(zombie);

        checkAndRemoveBrokenShield(zombie, shield);

        shield = getShieldItem(zombie);

        NBTTagCompound dscData = getDSCData(zombie);

        int cooldown = dscData.getInteger(SHIELD_COOLDOWN);

        if (cooldown > 0)
        {
            dscData.setInteger(SHIELD_COOLDOWN, cooldown - 1);
        }

        float shieldHealth = dscData.getFloat(SHIELD_HEALTH);

        if (cooldown <= 0 && shieldHealth < MAX_SHIELD_HEALTH)
        {
            long lastHit = dscData.getLong(LAST_HIT_TIME);
            long currentTime = zombie.world.getTotalWorldTime();
            long timeSinceHit = currentTime - lastHit;

            if (timeSinceHit > RECHARGE_DELAY)
            {
                shieldHealth = Math.min(MAX_SHIELD_HEALTH, shieldHealth + RECHARGE_RATE);
                dscData.setFloat(SHIELD_HEALTH, shieldHealth);

                if (shield != null)
                {
                    repairShieldIfNeeded(zombie, shield, shieldHealth);
                }

                if (ENABLE_PARTICLES && zombie.world.rand.nextFloat() < 0.1F)
                {
                    showRechargeParticles(zombie);
                }
            }
        }

        if (shieldHealth > 0 && shield != null && ENABLE_PARTICLES)
        {
            showShieldParticles(zombie, shieldHealth / MAX_SHIELD_HEALTH);
        }
    }
}
