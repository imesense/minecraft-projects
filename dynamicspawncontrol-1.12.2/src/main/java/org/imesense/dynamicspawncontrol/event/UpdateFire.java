package org.imesense.dynamicspawncontrol.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class UpdateFire
{
    public UpdateFire()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    @SubscribeEvent
    public void onLivingDeath_0(LivingDeathEvent livingDeathEvent)
    {
        if (livingDeathEvent.getSource().isFireDamage() &&
                !livingDeathEvent.getEntityLiving().isBurning() &&
                    !livingDeathEvent.getEntity().world.isRemote)
        {
            livingDeathEvent.getEntityLiving().setFire(1);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld_1(EntityJoinWorldEvent entityJoinWorldEvent)
    {
        if (entityJoinWorldEvent.getEntity() instanceof EntityArrow &&
                !entityJoinWorldEvent.getEntity().world.isRemote)
        {
            EntityArrow arrowEntity = (EntityArrow) entityJoinWorldEvent.getEntity();
            Entity shooter = arrowEntity.shootingEntity;

            if (shooter instanceof AbstractSkeleton && shooter.isBurning() && !shooter.isDead && this.tryPercentage(0.70))
            {
                arrowEntity.setFire(5);
            }
        }
    }

    @SubscribeEvent
    public void onLivingTick_2(LivingEvent.LivingUpdateEvent livingUpdateEvent)
    {
        if (!livingUpdateEvent.getEntityLiving().world.isRemote &&
                livingUpdateEvent.getEntityLiving().isBurning() &&
                    livingUpdateEvent.getEntityLiving().isPotionActive(MobEffects.FIRE_RESISTANCE))
        {
            livingUpdateEvent.getEntityLiving().extinguish();
        }
    }

    @SubscribeEvent
    public void onLivingAttack_3(LivingAttackEvent livingAttackEvent)
    {
        if (!livingAttackEvent.getEntity().world.isRemote)
        {
            Entity sourceEntity = livingAttackEvent.getSource().getTrueSource();

            if (sourceEntity instanceof EntityLivingBase)
            {
                EntityLivingBase sourceLiving = (EntityLivingBase)sourceEntity;
                ItemStack heldItem = sourceLiving.getHeldItemMainhand();

                if (!(sourceLiving instanceof EntityZombie) &&
                        heldItem.isEmpty() &&
                            sourceLiving.isBurning() && this.tryPercentage(0.30))
                {
                    float damage = Math.max(1.0F, livingAttackEvent.getEntityLiving().world.getDifficultyForLocation(new BlockPos(livingAttackEvent.getEntity())).getAdditionalDifficulty());
                    livingAttackEvent.getEntityLiving().setFire(2 * (int)damage);
                }
                else if (heldItem.getItem() == Items.FLINT_AND_STEEL)
                {
                    livingAttackEvent.getEntityLiving().setFire(3);
                    EntityPlayerMP player = sourceLiving instanceof EntityPlayerMP ? (EntityPlayerMP)sourceLiving : null;
                    heldItem.attemptDamageItem(1, sourceLiving.getRNG(), player);
                }
            }
        }
    }

    private boolean tryPercentage(double chance)
    {
        return Math.random() < chance;
    }
}
