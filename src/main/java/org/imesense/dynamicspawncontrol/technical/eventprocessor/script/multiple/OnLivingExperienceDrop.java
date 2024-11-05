package org.imesense.dynamicspawncontrol.technical.eventprocessor.script.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.config.data.GameDebuggerData;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericExperience;
import org.imesense.dynamicspawncontrol.technical.parser.ParserGenericJsonScript;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
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
     * @param livingExperienceDropEvent
     */
    @SubscribeEvent
    public synchronized void onUpdateLivingExperienceDrop_0(LivingExperienceDropEvent livingExperienceDropEvent)
    {
        AtomicInteger atomicInteger = new AtomicInteger();

        for (GenericExperience rule : ParserGenericJsonScript.GENERIC_EXPERIENCE_LIST)
        {
            if (rule.match(livingExperienceDropEvent))
            {
                Event.Result result = rule.getResult();

                if (result != Event.Result.DENY)
                {
                    int modifyXp = rule.modifyXp(livingExperienceDropEvent.getDroppedExperience());
                    livingExperienceDropEvent.setDroppedExperience(modifyXp);

                    if (GameDebuggerData.ConfigDataEvent.Instance.getDebugSetting("debug_on_living_experience_drop"))
                    {
                        Log.writeDataToLogFile(0, "ConfigsParser._GenericExperience. ID Rule: " + atomicInteger + ": "
                                + result
                                + " entity: " + livingExperienceDropEvent.getEntity().getName()
                                + " y: " + livingExperienceDropEvent.getEntity().getPosition().getY() + " new xp: " + modifyXp);
                    }
                }
                else
                {
                    livingExperienceDropEvent.setCanceled(true);
                }

                return;
            }

            atomicInteger.getAndIncrement();
        }
    }
}

