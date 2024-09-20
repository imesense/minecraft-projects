package org.imesense.dynamicspawncontrol.technical.worldcache;

import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.util.List;

/**
 *
 */
public class CacheStorage
{
    /**
     *
     */
    private static CacheStorage instance;

    /**
     *
     * @return
     */
    public static CacheStorage getInstance()
    {
        return instance;
    }

    /**
     *
     * @param nameClass
     */
    public CacheStorage(final String nameClass)
    {
        Log.writeDataToLogFile(0, String.format("Create object [%s]", nameClass));

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
        private final int MAX_COUNT;
        private final ResourceLocation ENTITY;

        public EntityData(ResourceLocation entity, int maxCount)
        {
            this.ENTITY = entity;
            this.MAX_COUNT = maxCount;
        }

        public ResourceLocation getEntity()
        {
            return this.ENTITY;
        }

        public int getMaxCount()
        {
            return this.MAX_COUNT;
        }
    }
}
