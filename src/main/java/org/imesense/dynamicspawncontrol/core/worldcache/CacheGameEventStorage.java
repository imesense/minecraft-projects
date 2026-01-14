package org.imesense.dynamicspawncontrol.core.worldcache;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

public class CacheGameEventStorage
{
    private static volatile CacheGameEventStorage _INSTANCE;

    public static CacheGameEventStorage getInstance()
    {
        return CodeGeneric.getInstance(CacheGameEventStorage.class);
    }

    public CacheGameEventStorage()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public static final class ObjectNodeEventGameEvents
    {
        public static final class EventCacheGameEvent
        {
            public Long correctDay;
            public Boolean repeatEvent;
        }

        public static class EventEntityDataCacheGameEvent
        {
            public Integer idDimension;
            public ResourceLocation entity;

            public Integer max_entity_count;
            public Event.Result result;
        }

        public static final class EventGameCacheEventElse extends EventEntityDataCacheGameEvent
        {
            public Short idNode;
        }

        // Записываем объект, который будет обрабатывать хранение 'ObjectNodeEventGameEvents'
    }
}
