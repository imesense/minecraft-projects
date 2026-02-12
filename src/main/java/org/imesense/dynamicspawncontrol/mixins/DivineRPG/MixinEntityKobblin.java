package org.imesense.dynamicspawncontrol.mixins.DivineRPG;

import divinerpg.objects.entities.entity.vanilla.EntityKobblin;
import divinerpg.registry.LootTableRegistry;
import divinerpg.registry.SoundRegistry;
import lombok.NonNull;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import java.util.UUID;

@Mixin(value = EntityKobblin.class, remap = false)
public abstract class MixinEntityKobblin extends net.minecraft.entity.monster.EntityMob
{
    private UUID provokedByUUID = null;
    private int provokedLevel = 0;

    private static final DataParameter<Boolean> CUSTOM_PROVOKED =
            EntityDataManager.createKey(EntityKobblin.class, DataSerializers.BOOLEAN);

    public MixinEntityKobblin(World worldIn)
    {
        super(worldIn);
    }

    @Overwrite
    public void entityInit()
    {
        super.entityInit();
        this.dataManager.register(CUSTOM_PROVOKED, false);
    }

    @Overwrite
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();

        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.27D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
    }

    @Overwrite
    public boolean needsSpecialAI()
    {
        return true;
    }

    protected void addBasicAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
    }

    protected void addAttackingAI()
    {
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, true)
        {
            @Override
            public boolean shouldExecute()
            {
                if (!getProvoked())
                {
                    return false;
                }

                EntityLivingBase target = getAttackTarget();

                if (target == null)
                {
                    return false;
                }

                return target.getUniqueID().equals(provokedByUUID);
            }

            @Override
            public boolean shouldContinueExecuting()
            {
                if (!getProvoked())
                {
                    return false;
                }

                EntityLivingBase target = getAttackTarget();

                if (target == null)
                {
                    return false;
                }

                return target.getUniqueID().equals(provokedByUUID);
            }
        });
    }

    @Overwrite
    public void onUpdate()
    {
        super.onUpdate();

        if (this.world.isRemote)
        {
            return;
        }

        if (this.provokedLevel > 0)
        {
            this.provokedLevel--;
        }

        if (!this.getProvoked() && this.provokedLevel <= 0)
        {
            this.renderYawOffset = 0.0F;
            EntityPlayer player = this.world.getNearestAttackablePlayer(this, 4.0F, 4.0F);

            if (player != null)
            {
                this.setProvoked(player);
                this.motionY = 0.6;
            }
        }

        if (this.provokedByUUID != null && this.getAttackTarget() == null && this.provokedLevel > 0)
        {
            Entity entity = ((WorldServer)this.world).getEntityFromUuid(this.provokedByUUID);
            if (entity instanceof EntityPlayer && entity.isEntityAlive())
            {
                this.setAttackTarget((EntityLivingBase)entity);
            }
            else
            {
                this.provokedByUUID = null;
                this.provokedLevel = 0;
                this.setProvoked(null);
            }
        }

        if (this.provokedLevel <= 0 && this.getProvoked())
        {
            this.setProvoked(null);
        }
    }

    @Overwrite
    public boolean getProvoked()
    {
        return this.dataManager.get(CUSTOM_PROVOKED);
    }

    @Overwrite
    public void setProvoked(EntityPlayer player)
    {
        this.dataManager.set(CUSTOM_PROVOKED, true);
        this.addBasicAI();
        this.addAttackingAI();

        if (player != null && !player.capabilities.isCreativeMode)
        {
            this.setAttackTarget(player);
            this.provokedByUUID = player.getUniqueID();
            this.provokedLevel = 600;
        }
        else
        {
            this.dataManager.set(CUSTOM_PROVOKED, false);
            this.setAttackTarget(null);
            this.provokedByUUID = null;
            this.provokedLevel = 0;
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity)
    {
        if (!this.getProvoked())
        {
            return false;
        }

        if (this.provokedByUUID != null && entity.getUniqueID().equals(this.provokedByUUID))
        {
            return super.attackEntityAsMob(entity);
        }

        return false;
    }

    @Overwrite
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        Entity entity = source.getTrueSource();

        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entity;

            if (!this.getProvoked())
            {
                this.setProvoked(player);
                this.motionY = 0.6;
            }
            else
            {
                this.provokedByUUID = player.getUniqueID();
                this.provokedLevel = 600;
                this.setAttackTarget(player);
            }
        }

        return super.attackEntityFrom(source, amount);
    }

    @Overwrite
    public void writeEntityToNBT(@NonNull NBTTagCompound tag)
    {
        super.writeEntityToNBT(tag);
        tag.setBoolean("Provoked", this.getProvoked());
        if (this.provokedByUUID != null) {
            tag.setString("ProvokedBy", this.provokedByUUID.toString());
        }
        tag.setInteger("ProvokedLevel", this.provokedLevel);
    }

    @Overwrite
    public void readEntityFromNBT(@NonNull NBTTagCompound tag)
    {
        super.readEntityFromNBT(tag);

        if (tag.getBoolean("Provoked"))
        {
            this.dataManager.set(CUSTOM_PROVOKED, true);

            String uuid = tag.getString("ProvokedBy");

            if (!uuid.isEmpty())
            {
                this.provokedByUUID = UUID.fromString(uuid);
                this.provokedLevel = tag.getInteger("ProvokedLevel");

                this.addBasicAI();
                this.addAttackingAI();
            }
        }
    }

    @Overwrite
    protected SoundEvent getHurtSound(@NonNull DamageSource source)
    {
        return SoundRegistry.KOBBLIN;
    }

    @Overwrite
    @NonNull protected SoundEvent getDeathSound()
    {
        return SoundRegistry.KOBBLIN;
    }

    @Overwrite
    protected ResourceLocation getLootTable()
    {
        return LootTableRegistry.ENTITIES_KOBBLIN;
    }

    @Overwrite
    protected void playStepSound(@NonNull BlockPos pos, @NonNull Block blockIn)
    {

    }

    @Overwrite
    public float getEyeHeight()
    {
        return 0.9F;
    }

    @Overwrite
    public void addVelocity(double x, double y, double z)
    {
        if (this.getProvoked())
        {
            super.addVelocity(x, y, z);
        }
    }

    @Overwrite
    public boolean getCanSpawnHere()
    {
        return this.world.provider.getDimension() == 0 &&
                this.world.getBlockState(this.getPosition().down()).getBlock() == Blocks.GRASS &&
                this.world.getBlockState(this.getPosition().down(2)).getBlock() != Blocks.AIR &&
                super.getCanSpawnHere();
    }

    @Overwrite
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }
}