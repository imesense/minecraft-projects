package org.imesense.dynamicspawncontrol.entity.feralzombie;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public final class EntityFeralZombie extends EntityZombie
{
    public EntityFeralZombie(World world)
    {
        super(world);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();

        double randomHealth = 20.0D + this.rand.nextInt(21);

        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH)
                .setBaseValue(randomHealth);

        this.setHealth((float) randomHealth);

        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
                .setBaseValue(0.33D);

        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
                .setBaseValue(7.5D);
    }

    @Override
    protected boolean shouldBurnInDay()
    {
        return false;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn)
    {
        boolean result = super.attackEntityAsMob(entityIn);

        if (!this.world.isRemote && entityIn instanceof EntityLivingBase)
        {
            EntityLivingBase target = (EntityLivingBase) entityIn;

            ResourceLocation registryName = EntityList.getKey(target);

            if (registryName != null && "srparasites".equals(registryName.getResourceDomain()))
            {
                if (this.rand.nextFloat() < 0.25F)
                {
                    float critMultiplier = 2.5F;

                    float baseDamage = (float)this.getEntityAttribute
                            (SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();

                    float extraDamage = baseDamage * (critMultiplier - 1.0F);

                    target.attackEntityFrom(DamageSource.causeMobDamage(this), extraDamage);

                    this.world.setEntityState(target, (byte)4);
                }
            }
        }

        return result;
    }
}
