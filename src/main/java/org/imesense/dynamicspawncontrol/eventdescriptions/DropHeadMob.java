package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
public final class DropHeadMob
{
    private static volatile DropHeadMob _INSTANCE;

    public static DropHeadMob getInstance()
    {
        return CodeGeneric.getInstance(DropHeadMob.class);
    }

    public DropHeadMob()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void handleEntityDeath(LivingDeathEvent event)
    {
        if (!(event.getSource().getTrueSource() instanceof EntityLivingBase))
            return;

        EntityLivingBase attacker =
                (EntityLivingBase) event.getSource().getTrueSource();

        ItemStack heldItem = attacker.getHeldItemMainhand();

        if (heldItem.isEmpty() || !(heldItem.getItem() instanceof ItemSword))
            return;

        Entity entity = event.getEntity();

        if (entity instanceof EntitySkeleton ||
                entity instanceof EntityZombie ||
                entity instanceof EntityCreeper)
        {
            float dropChance = calculateDropChance(attacker);

            if (attacker.getRNG().nextFloat() < dropChance)
            {
                dropHead((EntityLivingBase) entity, attacker.world);
            }
        }
    }


    private float calculateDropChance(EntityLivingBase entityLivingBase)
    {
        float baseChance = 0.0f;

        ItemStack heldItem = entityLivingBase.getHeldItemMainhand();

        double attackDamage = 1.0D;

        if (entityLivingBase.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) != null)
        {
            attackDamage = entityLivingBase
                    .getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
                    .getAttributeValue();
        }

        float damageFactor =
                MathHelper.clamp((float) attackDamage / 35.0f, 0.0f, 0.5f);

        baseChance += damageFactor;

        int smiteLevel = EnchantmentHelper
                .getEnchantmentLevel(Enchantments.SMITE, heldItem);

        float smiteMultiplier = 1.0f;

        if (smiteLevel > 0)
        {
            smiteMultiplier += smiteLevel * 0.75f;
            baseChance *= smiteMultiplier;
        }

        float durabilityFactor = 1.0f;

        if (!heldItem.isEmpty() && heldItem.isItemStackDamageable())
        {
            durabilityFactor =
                    1.0f - ((float) heldItem.getItemDamage() / heldItem.getMaxDamage());

            durabilityFactor = MathHelper.clamp(durabilityFactor, 0.2f, 1.0f);
            baseChance *= durabilityFactor;
        }

        boolean isCritical =
                entityLivingBase.fallDistance > 0.0F &&
                        !entityLivingBase.onGround &&
                        !entityLivingBase.isInWater() &&
                        !entityLivingBase.isPotionActive(net.minecraft.init.MobEffects.BLINDNESS) &&
                        entityLivingBase.getRidingEntity() == null;

        float critMultiplier = 1.0f;

        if (isCritical)
        {
            critMultiplier = 1.35f;
            baseChance *= critMultiplier;
        }

        float difficultyMultiplier = 1.0f;

        switch (entityLivingBase.world.getDifficulty())
        {
            case PEACEFUL:
                difficultyMultiplier = 0.0f;
                break;
            case EASY:
                difficultyMultiplier = 0.75f;
                break;
            case NORMAL:
                difficultyMultiplier = 1.0f;
                break;
            case HARD:
                difficultyMultiplier = 1.25f;
                break;
        }

        baseChance *= difficultyMultiplier;

        float finalChance = MathHelper.clamp(baseChance, 0.0f, 1.0f);

        Log.write(0,
                "[HeadDrop] " +
                        "Damage=" + String.format("%.2f", attackDamage) +
                        " DmgFactor=" + String.format("%.2f", damageFactor) +
                        " SmiteLvl=" + smiteLevel +
                        " SmiteMul=" + String.format("%.2f", smiteMultiplier) +
                        " DurMul=" + String.format("%.2f", durabilityFactor) +
                        " Crit=" + isCritical +
                        " CritMul=" + String.format("%.2f", critMultiplier) +
                        " Difficulty=" + entityLivingBase.world.getDifficulty() +
                        " DiffMul=" + String.format("%.2f", difficultyMultiplier) +
                        " Final=" + String.format("%.3f", finalChance)
        );

        return finalChance;
    }

    private void dropHead(EntityLivingBase entityLivingBase, World world)
    {
        ItemStack itemStack = ItemStack.EMPTY;

        if (entityLivingBase instanceof EntitySkeleton)
        {
            itemStack = new ItemStack(Items.SKULL, 1, 0);
        }
        else if (entityLivingBase instanceof EntityZombie)
        {
            itemStack = new ItemStack(Items.SKULL, 1, 2);
        }
        else if (entityLivingBase instanceof EntityCreeper)
        {
            itemStack = new ItemStack(Items.SKULL, 1, 4);
        }

        if (!itemStack.isEmpty())
        {
            entityLivingBase.entityDropItem(itemStack, 0);
        }
    }
}
