package org.imesense.dynamicspawncontrol.core.worldcache;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

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

    public List<GameEventData> eventData = new ArrayList<>();

    public static class GameEventData
    {
        public Integer day = 0;
        public Boolean repeat = false;

        public ResourceLocation entity;
        public Integer max_entity_count = 0;
        public Integer idDimension = null;
        public Event.Result result = Event.Result.DEFAULT;

        @Override
        public String toString()
        {
            return String.format("GameEventData { day = %d, repeat = %s, entity = %s, maxCount = %d, dim = %s, result = %s }",
                    day, repeat, entity, max_entity_count,
                    idDimension != null ? idDimension.toString() : "any",
                    result);
        }
    }
}
