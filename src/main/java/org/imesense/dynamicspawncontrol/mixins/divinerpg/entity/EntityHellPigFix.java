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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityHellPig.class, remap = false)
public abstract class EntityHellPigFix extends EntityDivineTameable
{
    private static final DataParameter<Float> HEALTH;
    private static final DataParameter<Boolean> ANGRY;

    public EntityHellPigFix(World worldIn, EntityPlayer player)
    {
        this(worldIn);
        this.setOwnerId(player.getUniqueID());
    }

    public EntityHellPigFix(World worldIn)
    {
        super(worldIn);
        this.setSize(1.0F, 0.9F);
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

    @Inject(method = "setAttackTarget", at = @At("HEAD"), cancellable = true)
    private void preventPassiveAggro(EntityLivingBase target, CallbackInfo ci)
    {
        if (!this.isTamed() && target != null)
        {
            if (this.getRevengeTarget() != target)
            {
                ci.cancel();
            }
        }
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        if (this.isTamed())
        {
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemFood)
            {
                ItemFood food = (ItemFood)itemstack.getItem();
                if (food.isWolfsFavoriteMeat() && (Float)this.dataManager.get(HEALTH) < 20.0F)
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

    public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner)
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

    static
    {
        HEALTH = EntityDataManager.createKey(EntityHellPig.class, DataSerializers.FLOAT);
        ANGRY = EntityDataManager.createKey(EntityHellPig.class, DataSerializers.BOOLEAN);
    }
}