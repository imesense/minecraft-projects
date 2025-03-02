package org.imesense.dynamicspawncontrol.core.script.storage;

import org.imesense.dynamicspawncontrol.core.script.storage.datadescription.PotionEffect;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public class SupportStorageScriptData
{
    private static volatile SupportStorageScriptData _INSTANCE;

    public static SupportStorageScriptData getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (SupportStorageScriptData.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new SupportStorageScriptData();
                }
            }
        }

        return _INSTANCE;
    }

    public SupportStorageScriptData()
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
