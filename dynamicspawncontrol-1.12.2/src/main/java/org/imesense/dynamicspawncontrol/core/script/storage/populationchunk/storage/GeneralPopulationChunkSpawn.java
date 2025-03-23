package org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.storage;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.data.PopulationChunkStruct;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.List;

@InitLog
public final class GeneralPopulationChunkSpawn
{
    private static volatile GeneralPopulationChunkSpawn _INSTANCE;

    public static GeneralPopulationChunkSpawn getInstance()
    {
        return CodeGeneric.getInstance(GeneralPopulationChunkSpawn.class);
    }

    public GeneralPopulationChunkSpawn()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public List<PopulationChunkStruct.Data> populationChunkStruct;
}
