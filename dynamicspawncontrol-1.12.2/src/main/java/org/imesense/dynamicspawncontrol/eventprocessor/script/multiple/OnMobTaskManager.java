package org.imesense.dynamicspawncontrol.eventprocessor.script.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericMobTaskManager;
import org.imesense.dynamicspawncontrol.technical.parser.ParserGenericJsonScript;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
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
		CodeGeneric.printInitClassToLog(this.getClass());
		
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    /**
     *
     * @param entityJoinWorldEvent
     */
    @SubscribeEvent
    public synchronized void onUpdateEntityJoinWorld_0(EntityJoinWorldEvent entityJoinWorldEvent)
    {
        if (!(entityJoinWorldEvent.getEntity() instanceof EntityLiving))
        {
            return;
        }

        AtomicInteger atomicInteger = new AtomicInteger();

        for (GenericMobTaskManager rule : ParserGenericJsonScript.GENERIC_MOBS_TASK_MANAGER_LIST)
        {
            if (rule.match(entityJoinWorldEvent))
            {
                rule.action(entityJoinWorldEvent);
            }

            atomicInteger.getAndIncrement();
        }
    }
}
