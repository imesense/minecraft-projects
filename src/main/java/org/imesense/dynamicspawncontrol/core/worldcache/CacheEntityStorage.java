package org.imesense.dynamicspawncontrol.core.worldcache;

import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventCheckSpawn;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.List;
import java.util.Optional;

public final class CacheEntityStorage
{
    private static volatile CacheEntityStorage _INSTANCE;

    public static CacheEntityStorage getInstance()
    {
        return CodeGeneric.getInstance(CacheEntityStorage.class);
    }

    public CacheEntityStorage()
    {
		CodeGeneric.printInitClassToLog(this.getClass());
    }

    public List<EntityData> EntityCacheMobs;

    public Optional<EntityData> getEntityDataByResourceLocation(ResourceLocation resourceLocation)
    {
        return EntityCacheMobs.stream()
                .filter(entityData -> entityData.RESOURCE_LOCATION.equals(resourceLocation))
                .findFirst();
    }

    public static class EntityData
    {
        public final int MAX_COUNT;

        public final ResourceLocation RESOURCE_LOCATION;

        public EntityData(ResourceLocation entity, int maxCount)
        {
            this.RESOURCE_LOCATION = entity;
            this.MAX_COUNT = maxCount;
        }
    }
}
