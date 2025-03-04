package org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.storage;

import net.minecraft.world.biome.Biome;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public final class GeneralPotentialSpawnStorage
{
    private static volatile GeneralPotentialSpawnStorage _INSTANCE;

    public static GeneralPotentialSpawnStorage getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (GeneralPotentialSpawnStorage.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new GeneralPotentialSpawnStorage();
                }
            }
        }
        return _INSTANCE;
    }

    public GeneralPotentialSpawnStorage()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        this.spawnEntries = new ArrayList<>();
    }

    public List<Biome.SpawnListEntry> spawnEntries;
}
