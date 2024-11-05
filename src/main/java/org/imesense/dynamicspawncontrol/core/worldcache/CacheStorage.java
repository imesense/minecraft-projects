package org.imesense.dynamicspawncontrol.core.worldcache;

import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.List;

/**
 *
 */
public final class CacheStorage
{
    /**
     *
     */
    public static CacheStorage Instance;

    /**
     *
     */
    public CacheStorage()
    {
		CodeGeneric.printInitClassToLog(this.getClass());
		
        Instance = this;
    }

    /**
     *
     */
    public List<EntityData> EntityCacheMobs;

    /**
     *
     * @return
     */
    public List<EntityData> getEntityCacheMobs()
    {
        return this.EntityCacheMobs;
    }

    /**
     *
     * @param resourceLocation
     * @return
     */
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

    /**
     *
     */
    public static class EntityData
    {
        /**
         *
         */
        private final int MAX_COUNT;

        /**
         *
         */
        private final ResourceLocation RESOURCE_LOCATION;

        /**
         *
         * @param entity
         * @param maxCount
         */
        public EntityData(ResourceLocation entity, int maxCount)
        {
            this.RESOURCE_LOCATION = entity;
            this.MAX_COUNT = maxCount;
        }

        /**
         *
         * @return
         */
        public ResourceLocation getEntity()
        {
            return this.RESOURCE_LOCATION;
        }

        /**
         *
         * @return
         */
        public int getMaxCount()
        {
            return this.MAX_COUNT;
        }
    }
}
