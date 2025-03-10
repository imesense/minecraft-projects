package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.world.WorldEvent;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.data.SecondaryParameters;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.storage.GeneralPotentialSpawnStorage;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class OnEventPotentialSpawn
{
    private static volatile OnEventPotentialSpawn _INSTANCE;

    public static OnEventPotentialSpawn getInstance()
    {
        return CodeGeneric.getInstance(OnEventPotentialSpawn.class);
    }

    public void handlePotentialSpawns(WorldEvent.PotentialSpawns event)
    {
        GeneralPotentialSpawnStorage storage = GeneralPotentialSpawnStorage.getInstance();

        List<Biome.SpawnListEntry> spawnEntries = storage.spawnEntries;
        List<SecondaryParameters.Data> secondaryParameters = storage.secondaryParameters;

        if (spawnEntries.isEmpty() || secondaryParameters.isEmpty())
        {
            return;
        }

        Integer eventY = event.getPos().getY();

        List<Biome.SpawnListEntry> filteredEntries = IntStream.range(0, spawnEntries.size())
                .filter(i -> UniqueField.RANDOM.nextFloat() < secondaryParameters.get(i).spawnChance &&
                        eventY >= secondaryParameters.get(i).minHeight &&
                        eventY <= secondaryParameters.get(i).maxHeight)
                .mapToObj(spawnEntries::get)
                .collect(Collectors.toList());

        List<Biome.SpawnListEntry> tempList = new ArrayList<>(event.getList());

        tempList.addAll(filteredEntries);

        event.getList().clear();
        event.getList().addAll(tempList);
    }
}
