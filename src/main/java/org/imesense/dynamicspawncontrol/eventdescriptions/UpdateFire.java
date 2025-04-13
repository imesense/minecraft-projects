package org.imesense.dynamicspawncontrol.eventdescriptions;

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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
public final class UpdateFire
{
    private static volatile UpdateFire _INSTANCE;

    public static UpdateFire getInstance()
    {
        return CodeGeneric.getInstance(UpdateFire.class);
    }

    public UpdateFire()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void handleLivingDeath(LivingDeathEvent event)
    {
        if (event.getSource().isFireDamage() &&
                !event.getEntityLiving().isBurning() &&
                !event.getEntity().world.isRemote)
        {
            event.getEntityLiving().setFire(1);
        }
    }

    public void handleEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityArrow &&
                !event.getEntity().world.isRemote)
        {
            EntityArrow entityArrow = (EntityArrow) event.getEntity();
            Entity shooter = entityArrow.shootingEntity;

            if (shooter instanceof AbstractSkeleton && shooter.isBurning() && !shooter.isDead && tryPercentage(0.70))
            {
                entityArrow.setFire(5);
            }
        }
    }

    public void handleLivingTick(LivingEvent.LivingUpdateEvent event)
    {
        if (!event.getEntityLiving().world.isRemote &&
                event.getEntityLiving().isBurning() &&
                event.getEntityLiving().isPotionActive(MobEffects.FIRE_RESISTANCE))
        {
            event.getEntityLiving().extinguish();
        }
    }

    public void handleLivingAttack(LivingAttackEvent event)
    {
        if (!event.getEntity().world.isRemote)
        {
            Entity sourceEntity = event.getSource().getTrueSource();

            if (sourceEntity instanceof EntityLivingBase)
            {
                EntityLivingBase sourceLiving = (EntityLivingBase) sourceEntity;
                ItemStack heldItem = sourceLiving.getHeldItemMainhand();

                if (!(sourceLiving instanceof EntityZombie) &&
                        heldItem.isEmpty() &&
                        sourceLiving.isBurning() && tryPercentage(0.30))
                {
                    float damage = Math.max(1.0F, event.getEntityLiving().world.getDifficultyForLocation(new BlockPos(event.getEntity())).getAdditionalDifficulty());
                    event.getEntityLiving().setFire(2 * (int) damage);
                }
                else if (heldItem.getItem() == Items.FLINT_AND_STEEL)
                {
                    event.getEntityLiving().setFire(3);
                    EntityPlayerMP entityPlayerMP = sourceLiving instanceof EntityPlayerMP ? (EntityPlayerMP) sourceLiving : null;
                    heldItem.attemptDamageItem(1, sourceLiving.getRNG(), entityPlayerMP);
                }
            }
        }
    }

    private boolean tryPercentage(double chance)
    {
        return Math.random() < chance;
    }
}