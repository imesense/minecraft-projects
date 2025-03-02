package org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.storage;

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

        this.spawnParametersList = new ArrayList<>();
    }

    public static class SpawnParameters
    {
        String entityType;
        Integer frequency;
        Integer groupCountMin;
        Integer groupCountMax;
        Float spawnChance;
        Integer maxHeight;
        Integer minHeight;
    }

    public List<SpawnParameters> spawnParametersList = null;
}
