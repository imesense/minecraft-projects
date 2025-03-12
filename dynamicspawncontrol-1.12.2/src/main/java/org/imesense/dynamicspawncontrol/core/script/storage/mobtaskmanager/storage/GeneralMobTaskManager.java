package org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.fixes.EntityId;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddEnemyListEnemiesToEnemyId;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddPanicListPanicToPanicId;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddEnemyListEnemyIdThemId;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddEnemyListEnemiesToToThem;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
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

    public List<AddEnemyListEnemiesToToThem.Data> listEnemiesToToThemData;
    public List<AddEnemyListEnemiesToEnemyId.Data> listEnemiesToEnemyIdData;
    public List<AddPanicListPanicToPanicId.Data> listPanicToPanicIdData;
    public List<AddEnemyListEnemyIdThemId.Data> listEnemyIdThemIdData;

    public void addEnemy(EntityJoinWorldEvent event)
    {

    }

    public void addEnemyId(EntityJoinWorldEvent event)
    {

    }

    public void addPanicToId(EntityJoinWorldEvent event)
    {

    }

    public void addEnemyToIdThemToId(EntityJoinWorldEvent event)
    {

    }
}
