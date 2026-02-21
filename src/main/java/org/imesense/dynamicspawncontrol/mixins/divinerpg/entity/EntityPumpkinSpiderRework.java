package org.imesense.dynamicspawncontrol.mixins.divinerpg.entity;

import divinerpg.objects.entities.entity.vanilla.EntityPumpkinSpider;
import divinerpg.registry.LootTableRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import java.util.UUID;

@Mixin(value = EntityPumpkinSpider.class, remap = false)
public abstract class EntityPumpkinSpiderRework extends EntityMob
{
    private int provokedLevel = 0;
    private UUID provokedByUUID = null;

    private static final DataParameter<Boolean> CUSTOM_PROVOKED =
            EntityDataManager.createKey(EntityPumpkinSpider.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> CUSTOM_CLIMBING =
            EntityDataManager.createKey(EntityPumpkinSpider.class, DataSerializers.BOOLEAN);

    public EntityPumpkinSpiderRework(World worldIn)
    {
        super(worldIn);
        this.setSize(1.25F, 1.0F);
    }

    @Overwrite
    public float getEyeHeight()
    {
        return 0.5F;
    }

    @Overwrite
    public void entityInit()
    {
        super.entityInit();

        this.dataManager.register(CUSTOM_CLIMBING, false);
        this.dataManager.register(CUSTOM_PROVOKED, false);
    }

    @Overwrite
    protected PathNavigate createNavigator(World worldIn)
    {
        return new PathNavigateClimber(this, worldIn);
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();

        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
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
    }

    protected void addAttackingAI()
    {
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));

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
    public void addVelocity(double x, double y, double z)
    {
        if (this.getProvoked())
        {
            super.addVelocity(x, y, z);
        }
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
            EntityPlayer player = this.world.getNearestAttackablePlayer(this, 6.0F, 6.0F);

            if (player != null)
            {
                this.setProvoked(player);
            }
        }

        if (this.getProvoked())
        {
            this.setBesideClimbableBlock(this.collidedHorizontally);
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
    public void setBesideClimbableBlock(boolean climbing)
    {
        this.dataManager.set(CUSTOM_CLIMBING, climbing);
    }

    @Overwrite
    public boolean isBesideClimbableBlock()
    {
        return this.dataManager.get(CUSTOM_CLIMBING);
    }

    @Overwrite
    public boolean isOnLadder()
    {
        return this.getProvoked() && this.isBesideClimbableBlock();
    }

    @Overwrite
    public void setInWeb() { }

    @Overwrite
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    @Overwrite
    protected boolean canTriggerWalking()
    {
        return false;
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

            if (player.isCreative())
            {
                return super.attackEntityFrom(source, amount);
            }

            if (!this.getProvoked())
            {
                this.setProvoked(player);
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
    protected boolean isValidLightLevel()
    {
        return true;
    }

    @Overwrite
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return SoundEvents.ENTITY_SPIDER_HURT;
    }

    @Overwrite
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SPIDER_DEATH;
    }

    @Overwrite
    protected ResourceLocation getLootTable()
    {
        return LootTableRegistry.ENTITIES_PUMPKIN_SPIDER;
    }

    @Overwrite
    public boolean getCanSpawnHere()
    {
        return this.world.provider.getDimension() == 0 &&
                this.world.getBlockState(this.getPosition().down()).getBlock() == Blocks.GRASS &&
                super.getCanSpawnHere();
    }

    @Overwrite
    public void writeEntityToNBT(NBTTagCompound tag)
    {
        super.writeEntityToNBT(tag);

        tag.setBoolean("Provoked", this.getProvoked());

        if (this.provokedByUUID != null)
        {
            tag.setString("ProvokedBy", this.provokedByUUID.toString());
        }

        tag.setInteger("ProvokedLevel", this.provokedLevel);
    }

    @Overwrite
    public void readEntityFromNBT(NBTTagCompound tag)
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
}