package org.imesense.dynamicspawncontrol.entity.explosionzombie;

import lombok.NonNull;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;

public final class EntityExplosionZombie extends EntityZombie
{
    public EntityExplosionZombie(World world)
    {
        super(world);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();

        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25.0D);

        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);

        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.5D);
    }

    @Override
    public void onDeath(@NonNull DamageSource cause)
    {
        if (!this.world.isRemote && !this.dead)
        {
            float radius = 2.0F;

            this.world.createExplosion(this, this.posX, this.posY, this.posZ, radius, false);

            List<EntityLivingBase> entities = this.world.getEntitiesWithinAABB(
                    EntityLivingBase.class,
                    this.getEntityBoundingBox().grow(radius)
            );

            for (EntityLivingBase entity : entities)
            {
                if (entity == this)
                    continue;

                ResourceLocation registryName = EntityList.getKey(entity);

                if (registryName != null && "srparasites".equals(registryName.getResourceDomain()))
                {
                    entity.attackEntityFrom(
                            DamageSource.causeExplosionDamage(this),
                            Float.MAX_VALUE
                    );
                }
            }
        }

        super.onDeath(cause);
    }
}
