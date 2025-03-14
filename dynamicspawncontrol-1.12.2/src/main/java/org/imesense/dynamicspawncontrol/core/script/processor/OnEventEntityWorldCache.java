package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

public final class OnEventEntityWorldCache
{
    private static volatile OnEventPopulationChunk _INSTANCE;

    public static OnEventEntityWorldCache getInstance()
    {
        return CodeGeneric.getInstance(OnEventEntityWorldCache.class);
    }

    public void handleLivingSpawnEventCheckSpawn(LivingSpawnEvent.CheckSpawn event)
    {

    }
}
