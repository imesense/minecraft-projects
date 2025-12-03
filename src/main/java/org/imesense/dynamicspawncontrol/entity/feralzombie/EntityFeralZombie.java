package org.imesense.dynamicspawncontrol.entity.feralzombie;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.World;

/**
 *
 */
public final class EntityFeralZombie extends EntityZombie
{
    /**
     *
     * @param world
     */
    public EntityFeralZombie(World world)
    {
        super(world);
        this.setSize(0.6F, 1.95F);
    }

    /**
     *
     */
    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();

        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D); // 20 * 1.5 = 30

        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.46D); // 0.23 * 2 = 0.46

        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D); // 3.0D + 4.0D
    }
}
