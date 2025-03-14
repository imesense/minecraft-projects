package org.imesense.dynamicspawncontrol.core.script.storage.EntityWorldCache.storage;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.data.DropItem;
import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.storage.GeneralDropItem;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public final class GeneralEntityWorldCache
{
    private static volatile GeneralEntityWorldCache _INSTANCE;

    public static GeneralEntityWorldCache getInstance()
    {
        return CodeGeneric.getInstance(GeneralEntityWorldCache.class);
    }

    public GeneralEntityWorldCache()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        this.entityWorldCacheDataList = new ArrayList<>();
    }

    public static final class EntityWorldCacheData
    {
        public String entity;
        public Boolean per_player;
        public Boolean per_chunk;
        public Integer max_entity_count;
        public Integer min_entity_count;
        public String result;
    }

    public List<EntityWorldCacheData> entityWorldCacheDataList;
}
