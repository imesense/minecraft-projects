package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.world.WorldEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
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
public final class OnEventPotentialSpawnOld
{
    private static volatile OnEventPotentialSpawnOld _INSTANCE;

    public static OnEventPotentialSpawnOld getInstance()
    {
        return CodeGeneric.getInstance(OnEventPotentialSpawnOld.class);
    }

    public OnEventPotentialSpawnOld()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    static boolean SPAWN_DEBUG_LOGGING = false;

    public void handlePotentialSpawns(WorldEvent.PotentialSpawns event)
    {
        if (DisableEventBooleansTest.test)
            return;

        GeneralPotentialSpawnStorage generalPotentialSpawnStorage = GeneralPotentialSpawnStorage.getInstance();
        List<Biome.SpawnListEntry> spawnEntries = generalPotentialSpawnStorage.spawnEntries;
        List<PotentialSpawnStruct.Data> secondaryParameters = generalPotentialSpawnStorage.potentialSpawnStruct;

        if (spawnEntries.isEmpty() || secondaryParameters.isEmpty())
        {
            if (SPAWN_DEBUG_LOGGING)
            {
                Log.write(1, "╔═══════════════════════════════════");
                Log.write(1, "║ No spawn entries or parameters found");
                Log.write(1, "╚═══════════════════════════════════");
            }

            return;
        }

        Integer eventY = event.getPos().getY();

        if (SPAWN_DEBUG_LOGGING)
        {
            Log.write(1, "╔═══════════════════════════════════");
            Log.write(1, "║ SPAWN PROCESSING STARTED");
            Log.write(1, "╠═ Y Level: " + eventY);
            Log.write(1, "╠═ Registered custom mobs (" + spawnEntries.size() + "):");

            spawnEntries.forEach(entry ->
                    Log.write(1, "║   " + entry.entityClass.getName() +
                            " (weight=" + entry.itemWeight + ")")
            );

            Log.write(1, "╠═ Original spawn list (" + event.getList().size() + " mobs):");

            event.getList().forEach(entry ->
                    Log.write(1, "║   " + entry.entityClass.getName())
            );
        }

        List<Biome.SpawnListEntry> filteredEntries = IntStream.range(0, spawnEntries.size())
        .filter(i ->
        {
            PotentialSpawnStruct.Data data = secondaryParameters.get(i);
            Biome.SpawnListEntry entry = spawnEntries.get(i);

            float effectiveChance = data.spawnChance * entry.itemWeight / 100f;
            boolean heightValid = eventY >= data.minHeight && eventY <= data.maxHeight;
            boolean chanceValid = UniqueField.RANDOM.nextFloat() < effectiveChance;

            if (SPAWN_DEBUG_LOGGING)
            {
                String status = (heightValid && chanceValid) ? "✓" : "✗";
                String details = String.format(
                        "║ %s %s: chance=%.1f%% (%.1f*%d/100), height=%d [%.0f-%.0f] %s",
                        status,
                        entry.entityClass.getSimpleName(),
                        effectiveChance * 100,
                        data.spawnChance,
                        entry.itemWeight,
                        eventY,
                        data.minHeight,
                        data.maxHeight,
                        heightValid ? "(H)" : "(h)"
                );
                Log.write(1, details);
            }

            return heightValid && chanceValid;
        })
        .mapToObj(spawnEntries::get)
        .collect(Collectors.toList());

        if (SPAWN_DEBUG_LOGGING)
        {
            Log.write(1, "╠═ Filtered mobs to add (" + filteredEntries.size() + "):");

            filteredEntries.forEach(entry ->
                    Log.write(1, "║   + " + entry.entityClass.getName())
            );
        }

        List<Biome.SpawnListEntry> tempList = new ArrayList<>(event.getList());

        tempList.addAll(filteredEntries);
        event.getList().clear();
        event.getList().addAll(tempList);

        if (SPAWN_DEBUG_LOGGING)
        {
            Log.write(1, "╠═ Final spawn list (" + event.getList().size() + " mobs):");

            event.getList().forEach(entry ->
                    Log.write(1, "║   • " + entry.entityClass.getName())
            );

            Log.write(1, "╚═══════════════════════════════════");
        }
    }
}
