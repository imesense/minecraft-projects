package org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.storage;

import net.minecraft.world.biome.Biome;
import org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.data.SecondaryParameters1;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.data.SecondaryParameters;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.storage.GeneralPotentialSpawnStorage;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public class GeneralPopulationChunkSpawn
{
    private static volatile GeneralPopulationChunkSpawn _INSTANCE;

    public static GeneralPopulationChunkSpawn getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (GeneralPopulationChunkSpawn.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new GeneralPopulationChunkSpawn();
                }
            }
        }
        return _INSTANCE;
    }

    public GeneralPopulationChunkSpawn()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }
}
