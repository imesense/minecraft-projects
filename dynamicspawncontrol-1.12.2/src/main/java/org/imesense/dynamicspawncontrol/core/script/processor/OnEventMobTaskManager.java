package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.functional.FunctionalMobTaskManager;
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

        FunctionalMobTaskManager.getInstance().processAddEnemyData(event);
        FunctionalMobTaskManager.getInstance().processAddEnemyIdData(event);
        FunctionalMobTaskManager.getInstance().processAddPanicToIdData(event);
        FunctionalMobTaskManager.getInstance().processAddEnemyToIdThemToIdData(event);
    }
}
