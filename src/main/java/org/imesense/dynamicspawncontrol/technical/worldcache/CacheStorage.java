package org.imesense.dynamicspawncontrol.technical.worldcache;

import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;

import java.util.List;

/**
 *
 */
public final class CacheStorage
{
    /**
     *
     */
    public static CacheStorage instance;

    /**
     *
     */
    public CacheStorage()
    {
		CodeGenericUtil.printInitClassToLog(this.getClass());
		
        instance = this;
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
        private final ResourceLocation ENTITY;

        /**
         *
         * @param entity
         * @param maxCount
         */
        public EntityData(ResourceLocation entity, int maxCount)
        {
            this.ENTITY = entity;
            this.MAX_COUNT = maxCount;
        }

        /**
         *
         * @return
         */
        public ResourceLocation getEntity()
        {
            return this.ENTITY;
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
