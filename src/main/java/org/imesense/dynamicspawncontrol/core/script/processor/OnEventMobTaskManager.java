package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.storage.GeneralMobTaskManager;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

public final class OnEventMobTaskManager
{
    private static volatile OnEventMobTaskManager _INSTANCE;

    public static OnEventMobTaskManager getInstance()
    {
        return CodeGeneric.getInstance(OnEventMobTaskManager.class);
    }

    public void handleUpdateEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (!(event.getEntity() instanceof EntityLiving))
        {
            return;
        }

        GeneralMobTaskManager.getInstance().processAddEnemyData(event);
        GeneralMobTaskManager.getInstance().processAddEnemyIdData(event);
        GeneralMobTaskManager.getInstance().processAddPanicToIdData(event);
        GeneralMobTaskManager.getInstance().processAddEnemyToIdThemToIdData(event);
    }
}
