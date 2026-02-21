package org.imesense.dynamicspawncontrol.mixins.divinerpg.entity;

import divinerpg.objects.entities.entity.EntityDivineTameable;
import divinerpg.objects.entities.entity.nether.EntityHellPig;
import divinerpg.registry.LootTableRegistry;
import lombok.NonNull;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import java.util.List;

@Mixin(value = EntityHellPig.class, remap = false)
public abstract class EntityHellPigFix extends EntityDivineTameable
{
    private static final DataParameter<Float> HEALTH;
    private static final DataParameter<Boolean> ANGRY;

    static
    {
        HEALTH = EntityDataManager.createKey(EntityHellPig.class, DataSerializers.FLOAT);
        ANGRY = EntityDataManager.createKey(EntityHellPig.class, DataSerializers.BOOLEAN);
    }

    public EntityHellPigFix(World worldIn, EntityPlayer player)
    {
        super(worldIn);
        this.setSize(1.0F, 0.9F);
        this.setOwnerId(player.getUniqueID());
    }

    public float getEyeHeight()
    {
        return 0.8F;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.increaseHealthIfTimable();
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(HEALTH, this.getHealth());
        this.dataManager.register(ANGRY, Boolean.FALSE);
    }

    protected void updateAITasks()
    {
        super.updateAITasks();
        this.dataManager.set(HEALTH, this.getHealth());
    }

    @Override
    public void setAttackTarget(EntityLivingBase attackTarget)
    {
        if (attackTarget != null && this.getRevengeTarget() != null &&
                attackTarget.equals(this.getRevengeTarget()))
        {
            this.setAngry(true);
        }
        else if (attackTarget == null)
        {
            this.setAngry(false);
        }

        super.setAttackTarget(attackTarget);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }

        Entity entity = source.getTrueSource();

        if (this.aiSit != null)
        {
            this.aiSit.setSitting(false);
        }

        if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
        {
            amount = (amount + 1.0F) / 2.0F;
        }

        if (entity instanceof EntityLivingBase && !this.isTamed())
        {
            if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative())
            {
                return super.attackEntityFrom(source, amount);
            }

            EntityLivingBase attacker = (EntityLivingBase) entity;

            this.setAngry(true);

            if (!this.world.isRemote)
            {
                List<EntityHellPigFix> nearbyPigs = this.world.getEntitiesWithinAABB(
                        EntityHellPigFix.class,
                        this.getEntityBoundingBox().grow(16.0D, 4.0D, 16.0D));

                for (EntityHellPigFix pig : nearbyPigs)
                {
                    if (!pig.isTamed() && !pig.isAngry() && pig != this)
                    {
                        pig.setAngry(true);

                        if (pig.getAttackTarget() == null)
                        {
                            pig.setAttackTarget(attacker);
                        }
                    }
                }
            }
        }

        return super.attackEntityFrom(source, amount);
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        if (this.isTamed())
        {
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemFood)
            {
                ItemFood food = (ItemFood)itemstack.getItem();

                if (food.isWolfsFavoriteMeat() && this.dataManager.get(HEALTH) < 20.0F)
                {
                    if (!player.capabilities.isCreativeMode)
                    {
                        itemstack.shrink(1);
                    }

                    this.heal((float)food.getHealAmount(itemstack));

                    return true;
                }
            }
        }
        else if (!itemstack.isEmpty() && itemstack.getItem() == Items.BLAZE_POWDER && !this.isAngry())
        {
            if (!player.capabilities.isCreativeMode)
            {
                itemstack.shrink(1);
            }

            if (!this.world.isRemote)
            {
                if (this.rand.nextInt(3) == 0)
                {
                    this.setTamedBy(player);
                    this.navigator.clearPath();

                    this.setAttackTarget(null);
                    this.aiSit.setSitting(true);

                    this.setHealth(20.0F);
                    this.playTameEffect(true);

                    this.world.setEntityState(this, (byte)7);
                }
                else
                {
                    this.playTameEffect(false);
                    this.world.setEntityState(this, (byte)6);
                }
            }

            return true;
        }

        return super.processInteract(player, hand);
    }

    public void setTamed(boolean tamed)
    {
        super.setTamed(tamed);
        this.increaseHealthIfTimable();
    }

    public EntityAgeable createChild(@NonNull EntityAgeable entityAgeable)
    {
        return null;
    }

    public void writeEntityToNBT(@NonNull NBTTagCompound nbtTagCompound)
    {
        super.writeEntityToNBT(nbtTagCompound);

        nbtTagCompound.setBoolean("Angry", this.isAngry());
    }

    public void readEntityFromNBT(@NonNull NBTTagCompound nbtTagCompound)
    {
        super.readEntityFromNBT(nbtTagCompound);

        this.setAngry(nbtTagCompound.getBoolean("Angry"));
    }

    public boolean isAngry()
    {
        return this.dataManager.get(ANGRY);
    }

    public void setAngry(boolean angry)
    {
        this.dataManager.set(ANGRY, angry);
    }

    protected boolean canDespawn()
    {
        return !this.isTamed() && this.ticksExisted > 2400;
    }

    protected void playStepSound(@NonNull BlockPos pos, @NonNull Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }

    public boolean shouldAttackEntity(@NonNull EntityLivingBase target, @NonNull EntityLivingBase owner)
    {
        if (!(target instanceof EntityCreeper) && !(target instanceof EntityGhast))
        {
            if (target instanceof EntityHellPig)
            {
                EntityHellPig pig = (EntityHellPig)target;

                if (pig.isTamed() && pig.getOwner() == owner)
                {
                    return false;
                }
            }

            return (!(target instanceof EntityPlayer) || !(owner instanceof EntityPlayer) || ((EntityPlayer)owner).canAttackPlayer((EntityPlayer)target)) && (!(target instanceof EntityHorse) || !((EntityHorse)target).isTame());
        }
        else
        {
            return false;
        }
    }

    public boolean getCanSpawnHere()
    {
        return this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.world.getBlockState(this.getPosition().down()).getBlock() == Blocks.SOUL_SAND;
    }

    protected ResourceLocation getLootTable()
    {
        return LootTableRegistry.ENTITIES_HELL_PIG;
    }
}