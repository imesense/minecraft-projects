package org.imesense.dynamicspawncontrol.technical.eventprocessor.script.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.config.gamedebugger.DataGameDebugger;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericExperience;
import org.imesense.dynamicspawncontrol.technical.parsers.ParserGenericJsonScripts;

/**
 *
 */
@Mod.EventBusSubscriber
public final class OnLivingExperienceDrop
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnLivingExperienceDrop()
    {
		CodeGenericUtils.printInitClassToLog(this.getClass());
		
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdateLivingExperienceDrop_0(LivingExperienceDropEvent event)
    {
        AtomicInteger i = new AtomicInteger();

        for (GenericExperience rule : ParserGenericJsonScripts.GENERIC_EXPERIENCE_LIST)
        {
            if (rule.match(event))
            {
                Event.Result result = rule.getResult();

                if (result != Event.Result.DENY)
                {
                    int modifyXp = rule.modifyXp(event.getDroppedExperience());
                    event.setDroppedExperience(modifyXp);

                    if (DataGameDebugger.DebugEvent.instance.getDebugOnLivingExperienceDrop())
                    {
                        Log.writeDataToLogFile(0, "ConfigsParser._GenericExperience. ID Rule: " + i + ": "
                                + result
                                + " entity: " + event.getEntity().getName()
                                + " y: " + event.getEntity().getPosition().getY() + " new xp: " + modifyXp);
                    }
                }
                else
                {
                    event.setCanceled(true);
                }

                return;
            }

            i.getAndIncrement();
        }
    }
}

