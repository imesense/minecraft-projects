package org.imesense.dynamicspawncontrol.entity.feralzombie;

import net.minecraft.entity.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.entity.zombie.ZombieBaseEntity;

public final class FeralZombieEntity extends ZombieBaseEntity
{
    public FeralZombieEntity(World world)
    {
        super(world);
        this.setZombieType(ZombieType.FERAL);
    }

    public ZombieType getZombieType()
    {
        return this.zombieType;
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
