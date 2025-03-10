package org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.storage;

import net.minecraft.world.biome.Biome;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.data.SecondaryParameters;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public final class GeneralPotentialSpawnStorage
{
    private static volatile GeneralPotentialSpawnStorage _INSTANCE;

    public static GeneralPotentialSpawnStorage getInstance()
    {
        return CodeGeneric.getInstance(GeneralPotentialSpawnStorage.class);
    }

    public GeneralPotentialSpawnStorage()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        this.spawnEntries = new ArrayList<>();
        this.secondaryParameters = new ArrayList<>();
    }

    public List<Biome.SpawnListEntry> spawnEntries;
    public List<SecondaryParameters.Data> secondaryParameters;
}
