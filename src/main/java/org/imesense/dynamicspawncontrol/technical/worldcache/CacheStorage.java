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
    private static CacheStorage instanceClass;

    /**
     *
     * @return
     */
    public static CacheStorage getInstance()
    {
        return instanceClass;
    }

    /**
     *
     * @param nameClass
     */
    public CacheStorage(final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], String.format("Create object [%s]", nameClass));

        instanceClass = this;
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
        return EntityCacheMobs;
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
        private final int maxCount;
        private final ResourceLocation entity;

        public EntityData(ResourceLocation entity, int maxCount)
        {
            this.entity = entity;
            this.maxCount = maxCount;
        }

        public ResourceLocation getEntity()
        {
            return entity;
        }

        public int getMaxCount()
        {
            return maxCount;
        }
    }
}
