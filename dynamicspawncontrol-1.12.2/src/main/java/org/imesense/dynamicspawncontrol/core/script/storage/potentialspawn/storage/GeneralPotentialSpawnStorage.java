package org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.storage;

import net.minecraft.world.biome.Biome;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.data.PotentialSpawnStruct;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

@InitLog
public final class GeneralPotentialSpawnStorage
{
    private static volatile GeneralPotentialSpawnStorage _INSTANCE;

    public static GeneralPotentialSpawnStorage getInstance()
    {
        return CodeGeneric.getInstance(GeneralPotentialSpawnStorage.class);
    }

    public GeneralPotentialSpawnStorage()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }

        this.spawnEntries = new ArrayList<>();
        this.potentialSpawnStruct = new ArrayList<>();
    }

    public List<Biome.SpawnListEntry> spawnEntries;
    public List<PotentialSpawnStruct.Data> potentialSpawnStruct;
}
