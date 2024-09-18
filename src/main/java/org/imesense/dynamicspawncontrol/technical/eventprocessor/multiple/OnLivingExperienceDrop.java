package org.imesense.dynamicspawncontrol.technical.eventprocessor.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
public final class OnLivingExperienceDrop
{
    /**
     *
     * @param nameClass
     */
    public OnLivingExperienceDrop(final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdateLivingExperienceDrop_0(LivingExperienceDropEvent event)
    {
        /*
        AtomicInteger i = new AtomicInteger();

        for (GenericExperience rule : ParserJsonScripts._genericExperience)
        {
            if (rule.match(event))
            {
                Event.Result result = rule.getResult();

                if (result != Event.Result.DENY)
                {
                    int modifyXp = rule.modifyXp(event.getDroppedExperience());
                    event.setDroppedExperience(modifyXp);

                    if (INFConfigDebug._debugGenericLivingExperienceDrop)
                    {
                        Log.writeDataToLogFile(Log._typeLog[0], "ConfigsParser._GenericExperience. ID Rule: " + i + ": "
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
         */
    }
}

