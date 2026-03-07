package org.imesense.dynamicspawncontrol.mixins.divinerpg.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import lombok.NonNull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import divinerpg.config.Config;
import divinerpg.objects.entities.entity.vethea.EntityLheiva;
import divinerpg.registry.ItemRegistry;
import divinerpg.registry.LootTableRegistry;
import divinerpg.registry.SoundRegistry;

@Mixin(value = EntityLheiva.class, remap = false)
@SuppressWarnings("UnusedMixin")
public abstract class EntityLheivaRework extends EntityMob
{
    @Unique
    private static final String ATTACKER_UUID_TAG = "AttackerUUID";

    @Unique
    private UUID $$attackerUUID;

    @Unique
    private EntityPlayer $$persistentAttacker;

    public EntityLheivaRework(World worldIn)
    {
        super(worldIn);

        this.setSize(1.0F, 1.4F);
    }

    @Override
    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));

        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true));

        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false)
        {
            @Override
            public void startExecuting()
            {
                super.startExecuting();

                EntityLivingBase attacker = this.taskOwner.getRevengeTarget();

                if (attacker instanceof EntityPlayer)
                {
                    ((EntityShadahierRework) this.taskOwner).$$setPersistentAttacker((EntityPlayer) attacker);
                }
            }
        });

        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 0, true, false, null)
        {
            @Override
            public boolean shouldExecute()
            {
                if ($$persistentAttacker != null && $$persistentAttacker.isEntityAlive())
                {
                    double distance = this.taskOwner.getDistanceSq($$persistentAttacker);

                    if (distance <= this.getTargetDistance() * this.getTargetDistance())
                    {
                        this.targetEntity = $$persistentAttacker;
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();

        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.27D);

        Config.initEntityAttributes(this);
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (!this.world.isRemote && this.$$persistentAttacker == null && this.$$attackerUUID != null)
        {
            for (EntityPlayer player : this.world.playerEntities)
            {
                if (player.getUniqueID().equals(this.$$attackerUUID) && player.isEntityAlive())
                {
                    this.$$persistentAttacker = player;
                    this.setRevengeTarget(player);
                    this.setAttackTarget(player);
                    break;
                }
            }
        }

        if (this.$$persistentAttacker != null && !this.$$persistentAttacker.isEntityAlive())
        {
            $$clearPersistentAttacker();
        }

        if (this.$$persistentAttacker != null && this.getAttackTarget() == null)
        {
            double distance = this.getDistanceSq(this.$$persistentAttacker);
            if (distance > 1024.0D)
            {
                $$clearPersistentAttacker();
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(@NonNull Entity target)
    {
        if (!(target instanceof EntityPlayer))
        {
            return false;
        }

        EntityPlayer targetPlayer = (EntityPlayer) target;

        if ($$persistentAttacker != null && targetPlayer != $$persistentAttacker)
        {
            return false;
        }

        if ($$persistentAttacker == null && this.getRevengeTarget() == targetPlayer)
        {
            $$setPersistentAttacker(targetPlayer);
        }

        if (super.attackEntityAsMob(target))
        {
            if (!this.world.isRemote)
            {
                targetPlayer.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 200, 0));
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        Entity entity = source.getTrueSource();

        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;

            if (player.isCreative())
            {
                return super.attackEntityFrom(source, amount);
            }

            if (((EntityPlayer)entity).inventory.hasItemStack(new ItemStack(ItemRegistry.bandOfHeivaHunting)))
            {
                return super.attackEntityFrom(source, amount);
            }
        }
        else if (source == DamageSource.OUT_OF_WORLD)
        {
            return super.attackEntityFrom(source, amount);
        }

        return false;
    }

    @Override
    public void setAttackTarget(@Nullable EntityLivingBase target)
    {
        if (target == null || target instanceof EntityPlayer)
        {
            super.setAttackTarget(target);
        }
    }

    @Override
    public void setRevengeTarget(@Nullable EntityLivingBase target)
    {
        if (target == null || target instanceof EntityPlayer)
        {
            super.setRevengeTarget(target);
        }
    }

    @Override
    public void writeEntityToNBT(@NonNull NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        if (this.$$attackerUUID != null)
        {
            compound.setString(ATTACKER_UUID_TAG, this.$$attackerUUID.toString());
        }
    }

    @Override
    public void readEntityFromNBT(@NonNull NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey(ATTACKER_UUID_TAG))
        {
            this.$$attackerUUID = UUID.fromString(compound.getString(ATTACKER_UUID_TAG));
        }
    }

    @Unique
    public void $$setPersistentAttacker(EntityPlayer attacker)
    {
        if (attacker != null)
        {
            this.$$persistentAttacker = attacker;
            this.$$attackerUUID = attacker.getUniqueID();
            this.setRevengeTarget(attacker);
            this.setAttackTarget(attacker);
        }
    }

    @Unique
    private void $$clearPersistentAttacker()
    {
        this.$$persistentAttacker = null;
        this.$$attackerUUID = null;
        this.setRevengeTarget(null);
        this.setAttackTarget(null);
    }

    @Unique
    public int $$getSpawnLayer()
    {
        return 3;
    }

    public boolean getCanSpawnHere()
    {
        return this.posY < 48.0D * this.$$getSpawnLayer() &&
                this.posY > 48.0D * (this.$$getSpawnLayer() - 1) &&
                super.getCanSpawnHere();
    }

    protected ResourceLocation getLootTable()
    {
        return LootTableRegistry.ENTITIES_LHEIVA;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundRegistry.LHEIVA;
    }

    @NonNull
    protected SoundEvent getHurtSound(@NonNull DamageSource source)
    {
        return SoundRegistry.LHEIVA_HURT;
    }

    @NonNull
    protected SoundEvent getDeathSound()
    {
        return SoundRegistry.LHEIVA_HURT;
    }
}
