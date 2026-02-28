package org.imesense.dynamicspawncontrol.entity.explosionzombie;

import lombok.NonNull;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

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

        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);

        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33D);

        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }

    @Override
    public void onDeath(@NonNull DamageSource cause)
    {
        if (!this.world.isRemote && !this.dead)
        {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 2.f, false);
        }
    }
}
