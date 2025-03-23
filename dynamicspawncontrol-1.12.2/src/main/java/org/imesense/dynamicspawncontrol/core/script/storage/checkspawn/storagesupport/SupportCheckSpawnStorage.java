package org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.storagesupport;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.datasupport.AdditionalChecks;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

@InitLog
public final class SupportCheckSpawnStorage
{
    private static volatile SupportCheckSpawnStorage _INSTANCE;

    public static SupportCheckSpawnStorage getInstance()
    {
        return CodeGeneric.getInstance(SupportCheckSpawnStorage.class);
    }

    public SupportCheckSpawnStorage()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }

        this.dataSupportList = new ArrayList<>();;
    }

    public List<AdditionalChecks.Data> dataSupportList;
}
