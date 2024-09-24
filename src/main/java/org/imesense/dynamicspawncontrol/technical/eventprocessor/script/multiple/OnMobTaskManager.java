package org.imesense.dynamicspawncontrol.technical.eventprocessor.script.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericMobTaskManager;
import org.imesense.dynamicspawncontrol.technical.parsers.ParserGenericJsonScripts;

/**
 *
 */
@Mod.EventBusSubscriber
public final class OnMobTaskManager
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnMobTaskManager()
    {
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;

        CodeGenericUtils.printInitClassToLog(OnMobTaskManager.class);
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdateEntityJoinWorld_0(EntityJoinWorldEvent event)
    {
        if (!(event.getEntity() instanceof EntityLiving))
        {
            return;
        }

        AtomicInteger i = new AtomicInteger();

        for (GenericMobTaskManager rule : ParserGenericJsonScripts.GENERIC_MOBS_TASK_MANAGER_LIST)
        {
            if (rule.match(event))
            {
                rule.action(event);
            }

            i.getAndIncrement();
        }
    }
}
