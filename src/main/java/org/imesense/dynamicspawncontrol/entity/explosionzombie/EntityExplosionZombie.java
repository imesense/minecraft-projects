package org.imesense.dynamicspawncontrol.entity.explosionzombie;

import lombok.NonNull;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.entity.base.DSCEntityZombie;

import java.util.List;

public final class EntityExplosionZombie extends DSCEntityZombie
{
    public EntityExplosionZombie(World world)
    {
        super(world);
        this.setZombieType(ZombieType.EXPLOSION);
    }

    public ZombieType getZombieType()
    {
        return this.zombieType;
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
