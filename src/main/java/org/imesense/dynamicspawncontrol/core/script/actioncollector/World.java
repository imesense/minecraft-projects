package org.imesense.dynamicspawncontrol.core.script.actioncollector;

import net.minecraft.entity.Entity;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

public final class World
{
    private static volatile World _INSTANCE;

    public static World getInstance()
    {
        return CodeGeneric.getInstance(World.class);
    }

    public boolean checkHeight(Entity entity, Integer minHeight, Integer maxHeight)
    {
        if (entity == null)
        {
            return false;
        }

        int entityY = (int) entity.posY;

        if (minHeight != null && entityY < minHeight)
        {
            return false;
        }

        if (maxHeight != null && entityY > maxHeight)
        {
            return false;
        }

        return true;
    }
}
