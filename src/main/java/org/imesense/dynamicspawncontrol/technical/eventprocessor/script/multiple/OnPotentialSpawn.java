package org.imesense.dynamicspawncontrol.technical.eventprocessor.script.multiple;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.entity.EntityList;
import net.minecraft.world.biome.Biome;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.config.data.GameDebuggerData;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericPotentialSpawn;
import org.imesense.dynamicspawncontrol.technical.parser.ParserGenericJsonScript;
import org.imesense.dynamicspawncontrol.core.worldcache.Cache;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheStorage;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
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
		CodeGeneric.printInitClassToLog(this.getClass());
		
        if (instanceExists)
			
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }
		
		instanceExists = true;
    }

    /**
     *
     * @param potentialSpawns
     */
    @SubscribeEvent
    public synchronized void onUpdatePotentialSpawns_0(WorldEvent.PotentialSpawns potentialSpawns)
    {
        if (potentialSpawns.getWorld().isRemote)
        {
            return;
        }

        AtomicInteger atomicInteger = new AtomicInteger();

        for (GenericPotentialSpawn rule : ParserGenericJsonScript.GENERIC_POTENTIAL_SPAWN_LIST)
        {
            if (rule.match(potentialSpawns))
            {
                for (Class<?> _class : rule.getToRemoveMobs())
                {
                    for (int idx = potentialSpawns.getList().size() - 1; idx >= 0; idx--)
                    {
                        if (potentialSpawns.getList().get(idx).entityClass == _class)
                        {
                            potentialSpawns.getList().remove(idx);
                        }
                    }
                }

                List<Biome.SpawnListEntry> spawnEntries = rule.getSpawnEntries();

                for (Biome.SpawnListEntry entry : spawnEntries)
                {
                    ResourceLocation resourceLocation = EntityList.getKey(entry.entityClass);
                    CacheStorage.EntityData entityData = CacheStorage.Instance.getEntityDataByResourceLocation(resourceLocation);

                    if (entityData != null)
                    {
                        assert resourceLocation != null;

                        int currentCount = Cache.Instance.getEntitiesByResourceLocation(resourceLocation).size();
                        int maxCount = entityData.getMaxCount();

                        if (currentCount >= maxCount)
                        {
                            continue;
                        }
                    }

                    float spawnChance = rule.getSpawnChance(entry.entityClass);
                    float minHeight = rule.getMinHeightChance(entry.entityClass);
                    float maxHeight = rule.getMaxHeightChance(entry.entityClass);
                    int eventY = potentialSpawns.getPos().getY();

                    if (UniqueField.RANDOM.nextFloat() < spawnChance && eventY >= minHeight && eventY <= maxHeight)
                    {
                        potentialSpawns.getList().add(entry);
                    }

                    if (GameDebuggerData.ConfigDataEvent.Instance.getDebugSetting("debug_on_potential_spawn"))
                    {
                        Log.writeDataToLogFile(0, "ConfigsParser._GenericOverrideSpawn. List: " + potentialSpawns.getList());
                    }
                }
            }

            atomicInteger.getAndIncrement();
        }
    }
}

