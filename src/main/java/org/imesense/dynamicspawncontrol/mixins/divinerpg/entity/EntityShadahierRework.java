package org.imesense.dynamicspawncontrol.mixins.divinerpg.entity;

import com.google.common.base.Predicate;
import divinerpg.config.Config; // не забыть
import divinerpg.objects.entities.entity.vethea.EntityShadahier;
import divinerpg.registry.LootTableRegistry;
import divinerpg.registry.SoundRegistry;
import lombok.NonNull;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = EntityShadahier.class, remap = false)
public abstract class EntityShadahierRework extends EntityMob
{
    public EntityShadahierRework(World worldIn)
    {
        super(worldIn);

        this.setSize(0.8F, 1.3F);
        this.addAttackingAI();
    }

    protected void addAttackingAI()
    {
        this.tasks.addTask(1, new EntityAIAttackMelee(this, (double)1.0F, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate)null));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue((double)20.0F);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.27);
        Config.initEntityAttributes(this);
    }

    @Override
    public boolean attackEntityAsMob(@NonNull Entity target)
    {
        if (super.attackEntityAsMob(target))
        {
            if (!this.world.isRemote && target instanceof EntityLivingBase)
            {
                ((EntityLivingBase)target).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 200, 0));
            }

            return true;
        }

        return false;
    }

    public int getSpawnLayer()
    {
        return 1;
    }

    public boolean getCanSpawnHere()
    {
        return this.posY < (double)48.0F * (double)this.getSpawnLayer() && this.posY > (double)48.0F * (double)(this.getSpawnLayer() - 1) && super.getCanSpawnHere();
    }

    protected ResourceLocation getLootTable()
    {
        return LootTableRegistry.ENTITIES_SHADAHIER;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundRegistry.SHADAHIER;
    }

    @NonNull
    protected SoundEvent getHurtSound(@NonNull DamageSource source)
    {
        return SoundRegistry.SHADAHIER_HURT;
    }

    @NonNull
    protected SoundEvent getDeathSound()
    {
        return SoundRegistry.SHADAHIER_HURT;
    }
}