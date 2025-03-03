package org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.storage;

import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.data.PotionEffect;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public final class SupportCheckSpawnStorage
{
    private static volatile SupportCheckSpawnStorage _INSTANCE;

    public static SupportCheckSpawnStorage getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (SupportCheckSpawnStorage.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new SupportCheckSpawnStorage();
                }
            }
        }

        return _INSTANCE;
    }

    public SupportCheckSpawnStorage()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        this.dataSupportList = new ArrayList<>();;
    }

    public static class DataSupport
    {
        public Boolean seeSky;
        public String entityType;
        public List<PotionEffect.Data> potion;
    }

    public List<DataSupport> dataSupportList;
}
