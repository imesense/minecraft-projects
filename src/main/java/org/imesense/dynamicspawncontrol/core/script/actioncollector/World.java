package org.imesense.dynamicspawncontrol.core.script.actioncollector;

import net.minecraft.entity.Entity;

public final class World
{
    private static volatile World _INSTANCE;

    public static World getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (World.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new World();
                }
            }
        }

        return _INSTANCE;
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
