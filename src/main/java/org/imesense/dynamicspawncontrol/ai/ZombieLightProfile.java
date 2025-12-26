package org.imesense.dynamicspawncontrol.ai;

import net.minecraft.entity.monster.EntityZombie;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;

@TODO(value = "Add on diagram project", showOnce = false, priority = TODO.TodoPriority.HIGH)
public final class ZombieLightProfile implements ILightReactiveMob
{
    private final EntityZombie zombie;

    public ZombieLightProfile(EntityZombie zombie)
    {
        this.zombie = zombie;
    }

    @Override
    public boolean canReactToLight()
    {
        return !zombie.isChild(); // тестово
    }

    @Override
    public float getLightReactionChance()
    {
        return 0.15f; // 15%
    }

    @Override
    public int getLightSearchRadius()
    {
        return 6;
    }
}
