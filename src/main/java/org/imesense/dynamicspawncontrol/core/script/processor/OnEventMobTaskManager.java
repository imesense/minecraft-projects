package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.functional.FunctionalMobTaskManager;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
@TODO(value = "Поломана оптимизация, к тому же переделать класс на схеме", showOnce = false, priority = TODO.TodoPriority.HIGH)
public final class OnEventMobTaskManager
{
    private static volatile OnEventMobTaskManager _INSTANCE;

    public static OnEventMobTaskManager getInstance()
    {
        return CodeGeneric.getInstance(OnEventMobTaskManager.class);
    }

    public OnEventMobTaskManager()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void handleUpdateEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (!(event.getEntity() instanceof EntityLiving))
        {
            return;
        }

        if (!event.getEntity().world.isRemote)
        {
            FunctionalMobTaskManager.getInstance().processAddEnemyData(event);
            FunctionalMobTaskManager.getInstance().processAddEnemyIdData(event);
            FunctionalMobTaskManager.getInstance().processAddPanicToIdData(event);
            FunctionalMobTaskManager.getInstance().processAddEnemyToIdThemToIdData(event);
        }
    }
}
