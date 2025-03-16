package org.imesense.dynamicspawncontrol.core.worldcache;

import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.List;

public final class CacheEntityStorage
{
    public static CacheEntityStorage Instance;

    public CacheEntityStorage()
    {
		CodeGeneric.printInitClassToLog(this.getClass());
		
        Instance = this;
    }

    public List<EntityData> EntityCacheMobs;

    public List<EntityData> getEntityCacheMobs()
    {
        return this.EntityCacheMobs;
    }

    public CacheEntityStorage.EntityData getEntityDataByResourceLocation(ResourceLocation resourceLocation)
    {
        for (CacheEntityStorage.EntityData entityData : getEntityCacheMobs())
        {
            if (entityData.getEntity().equals(resourceLocation))
            {
                return entityData;
            }
        }

        return null;
    }

    public static class EntityData
    {
        private final int MAX_COUNT;

        private final ResourceLocation RESOURCE_LOCATION;

        public EntityData(ResourceLocation entity, int maxCount)
        {
            this.RESOURCE_LOCATION = entity;
            this.MAX_COUNT = maxCount;
        }

        public ResourceLocation getEntity()
        {
            return this.RESOURCE_LOCATION;
        }

        public int getMaxCount()
        {
            return this.MAX_COUNT;
        }
    }
}
