package org.imesense.dynamicspawncontrol.mixins.divinerpg;

import divinerpg.objects.entities.entity.EntityPeacefulUntilAttacked;
import lombok.NonNull;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import java.util.UUID;

@Mixin(value = EntityPeacefulUntilAttacked.class, remap = false)
public abstract class MixinEntityPeacefulUntilAttacked extends net.minecraft.entity.monster.EntityMob
{
    private int customAngerLevel = 0;
    private UUID customAngerTargetUUID = null;

    public MixinEntityPeacefulUntilAttacked(World worldIn)
    {
        super(worldIn);
    }

    @Overwrite
    protected void initEntityAI()
    {
        EntityPeacefulUntilAttacked mob = (EntityPeacefulUntilAttacked)(Object)this;

        mob.tasks.taskEntries.clear();
        mob.targetTasks.taskEntries.clear();

        mob.tasks.addTask(0, new EntityAISwimming(mob));
        mob.tasks.addTask(5, new EntityAIWanderAvoidWater(mob, 0.8D));
        mob.tasks.addTask(6, new EntityAIWatchClosest(mob, EntityPlayer.class, 8.0F));
        mob.tasks.addTask(6, new EntityAILookIdle(mob));

        mob.tasks.addTask(2, new EntityAIAttackMelee(mob, 1.0D, true)
        {
            @Override
            public boolean shouldExecute()
            {
                if (customAngerLevel <= 0)
                {
                    return false;
                }

                EntityLivingBase target = mob.getAttackTarget();

                if (target == null)
                {
                    return false;
                }

                return target.getUniqueID().equals(customAngerTargetUUID);
            }

            @Override
            public boolean shouldContinueExecuting()
            {
                if (customAngerLevel <= 0)
                {
                    return false;
                }

                EntityLivingBase target = mob.getAttackTarget();

                if (target == null)
                {
                    return false;
                }

                return target.getUniqueID().equals(customAngerTargetUUID);
            }
        });

        mob.targetTasks.addTask(1, new EntityAIHurtByTarget(mob, true, new Class[0])
        {
            @Override
            public boolean shouldExecute()
            {
                if (super.shouldExecute())
                {
                    EntityLivingBase revengeTarget = mob.getRevengeTarget();

                    if (revengeTarget != null)
                    {
                        mob.setAttackTarget(revengeTarget);
                        customAngerTargetUUID = revengeTarget.getUniqueID();
                        customAngerLevel = 400 + mob.getRNG().nextInt(400);

                        return true;
                    }
                }

                return false;
            }
        });
    }

    @Overwrite
    public boolean attackEntityAsMob(Entity entity)
    {
        if (this.customAngerLevel <= 0)
        {
            return false;
        }

        if (this.customAngerTargetUUID != null &&
                entity.getUniqueID().equals(this.customAngerTargetUUID))
        {
            return super.attackEntityAsMob(entity);
        }

        return false;
    }

    @Overwrite
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (super.isEntityInvulnerable(source))
        {
            return false;
        }

        boolean result = super.attackEntityFrom(source, amount);

        Entity entity = source.getTrueSource();

        if (entity instanceof EntityLivingBase)
        {
            this.customAngerLevel = 400 + this.getRNG().nextInt(400);
            this.customAngerTargetUUID = entity.getUniqueID();
            this.setRevengeTarget((EntityLivingBase)entity);
            this.setAttackTarget((EntityLivingBase)entity);
        }

        return result;
    }

    @Overwrite
    public boolean isAngry()
    {
        return this.customAngerLevel > 0;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.world.isRemote)
        {
            return;
        }

        if (this.customAngerLevel > 0)
        {
            if (this.getAttackTarget() == null && this.customAngerTargetUUID != null)
            {
                EntityPlayer player = this.world.getPlayerEntityByUUID(this.customAngerTargetUUID);

                if (player != null)
                {
                    this.setAttackTarget(player);
                }
            }
        }
    }

    @Overwrite
    public boolean isPreventingPlayerRest(EntityPlayer playerIn)
    {
        return this.customAngerLevel > 0 &&
                this.customAngerTargetUUID != null &&
                this.customAngerTargetUUID.equals(playerIn.getUniqueID());
    }

    @Overwrite
    public void readEntityFromNBT(@NonNull NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        this.customAngerLevel = compound.getShort("Anger");
        String s = compound.getString("HurtBy");

        if (!s.isEmpty())
        {
            this.customAngerTargetUUID = UUID.fromString(s);
        }
    }

    @Overwrite
    public void writeEntityToNBT(@NonNull NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        compound.setShort("Anger", (short)this.customAngerLevel);

        if (this.customAngerTargetUUID != null)
        {
            compound.setString("HurtBy", this.customAngerTargetUUID.toString());
        }
    }
}