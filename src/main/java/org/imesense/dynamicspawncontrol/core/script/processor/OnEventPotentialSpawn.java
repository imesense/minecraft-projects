package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.world.WorldEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.data.PotentialSpawnStruct;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.storage.GeneralPotentialSpawnStorage;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@InitLog
@TODO(
        value = "Add logging and an additional option in the config + Bkeak optimization and fix diagram",
        priority = TODO.TodoPriority.HIGH,
        showOnce = false
)
public final class OnEventPotentialSpawn
{
    private static volatile OnEventPotentialSpawn _INSTANCE;

    public static OnEventPotentialSpawn getInstance()
    {
        return CodeGeneric.getInstance(OnEventPotentialSpawn.class);
    }

    public OnEventPotentialSpawn()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void handlePotentialSpawns(WorldEvent.PotentialSpawns event)
    {
        GeneralPotentialSpawnStorage generalPotentialSpawnStorage = GeneralPotentialSpawnStorage.getInstance();
        List<Biome.SpawnListEntry> spawnEntries = generalPotentialSpawnStorage.spawnEntries;
        List<PotentialSpawnStruct.Data> secondaryParameters = generalPotentialSpawnStorage.potentialSpawnStruct;

        if (spawnEntries.isEmpty() || secondaryParameters.isEmpty())
        {
            return;
        }

        Integer eventY = event.getPos().getY();
        int currentDimension = event.getWorld().provider.getDimension();

        List<Biome.SpawnListEntry> filteredEntries = IntStream.range(0, spawnEntries.size())
        .filter(i ->
        {
            PotentialSpawnStruct.Data data = secondaryParameters.get(i);
            Biome.SpawnListEntry entry = spawnEntries.get(i);

            if (data.idDimension != null)
            {
                if (currentDimension != data.idDimension)
                {
                    return false;
                }
            }

            float effectiveChance = data.spawnChance * entry.itemWeight / 100f;
            boolean heightValid = eventY >= data.minHeight && eventY <= data.maxHeight;
            boolean chanceValid = UniqueField.RANDOM.nextFloat() < effectiveChance;

            return heightValid && chanceValid;
        })
        .mapToObj(spawnEntries::get)
        .collect(Collectors.toList());

        List<Biome.SpawnListEntry> tempList = new ArrayList<>(event.getList());

        tempList.addAll(filteredEntries);
        event.getList().clear();
        event.getList().addAll(tempList);
    }
}
