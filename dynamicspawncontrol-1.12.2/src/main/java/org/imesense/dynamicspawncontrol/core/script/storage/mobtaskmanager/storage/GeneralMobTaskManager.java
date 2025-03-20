package org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.storage;

import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddEnemy;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddPanicToId;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddEnemyId;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddEnemyToIdThemToId;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.*;

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
        this.addEnemyData = new ArrayList<>();
        this.addEnemyIdData = new ArrayList<>();
        this.addPanicToIdData = new ArrayList<>();
        this.addEnemyToIdThemToIdData = new ArrayList<>();
    }

    public List<AddEnemy.Data> addEnemyData;
    public List<AddEnemyId.Data> addEnemyIdData;
    public List<AddPanicToId.Data> addPanicToIdData;
    public List<AddEnemyToIdThemToId.Data> addEnemyToIdThemToIdData;
}
