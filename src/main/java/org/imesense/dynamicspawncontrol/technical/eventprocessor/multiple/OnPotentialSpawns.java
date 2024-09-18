package org.imesense.dynamicspawncontrol.technical.eventprocessor.multiple;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.entity.EntityList;
import net.minecraft.world.biome.Biome;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
public final class OnPotentialSpawns
{
    /**
     *
     * @param nameClass
     */
    public OnPotentialSpawns(final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdatePotentialSpawns_0(WorldEvent.PotentialSpawns event)
    {
        if (event.getWorld().isRemote)
        {
            return;
        }

        /*
        AtomicInteger i = new AtomicInteger();

        for (GenericOverrideSpawn rule : ParserJsonScripts._genericOverrideSpawn)
        {
            if (rule.match(event))
            {
                for (Class<?> _class : rule.getToRemoveMobs())
                {
                    for (int idx = event.getList().size() - 1; idx >= 0; idx--)
                    {
                        if (event.getList().get(idx).entityClass == _class)
                        {
                            event.getList().remove(idx);
                        }
                    }
                }

                List<Biome.SpawnListEntry> spawnEntries = rule.getSpawnEntries();

                for (Biome.SpawnListEntry entry : spawnEntries)
                {
                    ResourceLocation entityKey = EntityList.getKey(entry.entityClass);
                    INFCacheStorage.EntityData entityData = INFCacheStorage.getInstance().getEntityDataByResourceLocation(entityKey);

                    if (entityData != null)
                    {
                        int currentCount = INFCache.getEntitiesByResourceLocation(entityKey).size();
                        int maxCount = entityData.getMaxCount();

                        if (currentCount >= maxCount)
                        {
                            continue;
                        }
                    }

                    float spawnChance = rule.getSpawnChance(entry.entityClass);
                    float minHeight = rule.getMinHeightChance(entry.entityClass);
                    float maxHeight = rule.getMaxHeightChance(entry.entityClass);
                    int eventY = event.getPos().getY();

                    if (Math.random() < spawnChance && eventY >= minHeight && eventY <= maxHeight)
                    {
                        event.getList().add(entry);
                    }

                    if (INFConfigDebug._debugGenericPotentialSpawns)
                    {
                        Log.writeDataToLogFile(Log._typeLog[0], "ConfigsParser._GenericOverrideSpawn. List: " + event.getList());
                    }
                }
            }

            i.getAndIncrement();
        }
         */
    }
}

