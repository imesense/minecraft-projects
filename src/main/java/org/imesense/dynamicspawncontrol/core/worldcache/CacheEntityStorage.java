package org.imesense.dynamicspawncontrol.core.worldcache;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

@InitLog
@TODO(
        value = "Добавить поддержку entityNameComment и entityDescriptionComment. Эти параметры " +
                "будут отвечать за компиляцию данного скрипта в html документ для просмотра итогового отчета." +
                "Сущность зомби: ... Описание: ... (на основе включенных параметров)",
        priority = TODO.TodoPriority.HIGH,
        showOnce = false
)
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
        public Long idNode = null;

        public Integer idDimension;
        public ResourceLocation entity;
        ///
        public String entityNameComment;
        public String entityDescriptionComment;
        ///
        public Class<?> check_instanceof;
        public Boolean per_player;
        public Boolean per_chunk;
        public Integer max_entity_count;
        public Boolean isContinue;
        public Event.Result result;
    }

    public List<EntityData> entityData;
}
