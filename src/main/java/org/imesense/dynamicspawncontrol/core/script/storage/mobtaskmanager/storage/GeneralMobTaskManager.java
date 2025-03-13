package org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.storage;

import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddEnemy;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddPanicToId;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddEnemyId;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddEnemyToIdThemToId;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.List;

public final class GeneralMobTaskManager
{
    private static volatile GeneralMobTaskManager _INSTANCE;

    public static GeneralMobTaskManager getInstance()
    {
        return CodeGeneric.getInstance(GeneralMobTaskManager.class);
    }

    public GeneralMobTaskManager()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

    }

    public List<AddEnemyToIdThemToId.Data> listEnemiesToToThemData;
    public List<AddEnemy.Data> listEnemiesToEnemyIdData;
    public List<AddPanicToId.Data> listPanicToPanicIdData;
    public List<AddEnemyId.Data> listEnemyIdThemIdData;
}
