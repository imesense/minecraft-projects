package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.data.SecondaryParameters1;
import org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.storage.GeneralPopulationChunkSpawn;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.data.SecondaryParameters;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.storage.GeneralPotentialSpawnStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OnEventPopulationChunk
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPopulateChunk(PopulateChunkEvent.Pre event)
    {

    }

    @SubscribeEvent
    public void onPotentialSpawn(PopulateChunkEvent.Pre event)
    {
        for (Biome biome : Biome.REGISTRY)
        {
            if (event.getWorld().rand.nextFloat() < 0.1F)
            {
                biome.getSpawnableList(EnumCreatureType.CREATURE).add(
                        new Biome.SpawnListEntry(EntityIronGolem.class, 10, 1, 3)
                );
            }
        }
    }
}