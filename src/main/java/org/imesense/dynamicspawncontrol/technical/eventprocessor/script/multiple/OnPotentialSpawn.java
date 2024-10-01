package org.imesense.dynamicspawncontrol.technical.eventprocessor.script.multiple;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.entity.EntityList;
import net.minecraft.world.biome.Biome;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.config.gamedebugger.DataGameDebugger;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericPotentialSpawn;
import org.imesense.dynamicspawncontrol.technical.parsers.ParserGenericJsonScripts;
import org.imesense.dynamicspawncontrol.technical.worldcache.Cache;
import org.imesense.dynamicspawncontrol.technical.worldcache.CacheStorage;

/**
 *
 */
@Mod.EventBusSubscriber
public final class OnPotentialSpawn
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnPotentialSpawn()
    {
		CodeGenericUtils.printInitClassToLog(this.getClass());
		
        if (instanceExists)
			
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }
		
		instanceExists = true;
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

        AtomicInteger i = new AtomicInteger();

        for (GenericPotentialSpawn rule : ParserGenericJsonScripts.GENERIC_POTENTIAL_SPAWN_LIST)
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
                    CacheStorage.EntityData entityData = CacheStorage.instance.getEntityDataByResourceLocation(entityKey);

                    if (entityData != null)
                    {
                        assert entityKey != null;

                        int currentCount = Cache.instance.getEntitiesByResourceLocation(entityKey).size();
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

                    if (DataGameDebugger.DebugEvent.instance.getDebugOnPotentialSpawn())
                    {
                        Log.writeDataToLogFile(0, "ConfigsParser._GenericOverrideSpawn. List: " + event.getList());
                    }
                }
            }

            i.getAndIncrement();
        }
    }
}

