package org.imesense.dynamicspawncontrol.core.worldcache;

import net.minecraft.util.ResourceLocation;

import java.util.List;

public class CacheStorage
{
    public static CacheStorage Instance;

    public CacheStorage()
    {
        //CodeGenericUtil.printInitClassToLog(this.getClass());

        Instance = this;
    }

    public List<EntityData> EntityCacheMobs;

    public List<EntityData> getEntityCacheMobs()
    {
        return this.EntityCacheMobs;
    }

    public CacheStorage.EntityData getEntityDataByResourceLocation(ResourceLocation resourceLocation)
    {
        for (CacheStorage.EntityData entityData : getEntityCacheMobs())
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
