package org.imesense.dynamicspawncontrol.core.worldcache;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventCheckSpawn;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@InitLog
public final class CacheEntityStorage
{
    private static volatile CacheEntityStorage _INSTANCE;

    public static CacheEntityStorage getInstance()
    {
        return CodeGeneric.getInstance(CacheEntityStorage.class);
    }

    public CacheEntityStorage()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }

        this.entityData = new ArrayList<>();
    }

    public static final class EntityData
    {
        public ResourceLocation entity;
        public Class<?> check_instanceof;
        public Boolean per_player;
        public Boolean per_chunk;
        public Integer max_entity_count;
        public Event.Result result;
    }

    public List<EntityData> entityData;
}
