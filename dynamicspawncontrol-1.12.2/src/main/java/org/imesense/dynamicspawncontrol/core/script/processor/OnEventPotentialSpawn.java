package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.storage.GeneralPotentialSpawnStorage;

import java.util.List;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventPotentialSpawn
{
    @SubscribeEvent
    public void onWorldEventPotentialSpawns_0(WorldEvent.PotentialSpawns potentialSpawns)
    {
        if (potentialSpawns.getWorld().isRemote)
        {
            return;
        }

        List<GeneralPotentialSpawnStorage.SpawnParameters> spawnParametersList = GeneralPotentialSpawnStorage.getInstance().spawnParametersList;

        for (GeneralPotentialSpawnStorage.SpawnParameters params : spawnParametersList)
        {
            EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(params.entityType));

            if (entityEntry == null)
            {
                Log.writeDataToLogFile(2, "Cannot find mob '" + params.entityType + "'");
                continue;
            }

            Class<? extends Entity> entityClass = entityEntry.getEntityClass();

            int eventY = potentialSpawns.getPos().getY();

            if (eventY >= params.minHeight && eventY <= params.maxHeight && UniqueField.RANDOM.nextFloat() < params.spawnChance)
            {
                Biome.SpawnListEntry entry =
                        new Biome.SpawnListEntry((Class<? extends EntityLiving>) entityClass,
                                params.frequency, params.groupCountMin, params.groupCountMax);

                potentialSpawns.getList().add(entry);
            }
        }
    }
}
