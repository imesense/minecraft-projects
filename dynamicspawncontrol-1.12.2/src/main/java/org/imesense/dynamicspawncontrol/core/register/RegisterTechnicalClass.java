package org.imesense.dynamicspawncontrol.core.register;

import net.minecraftforge.common.MinecraftForge;
import org.imesense.dynamicspawncontrol.core.script.processor.*;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.event.OnEventRenderFPS;
import org.imesense.dynamicspawncontrol.event.OnEventPlayer;
import org.imesense.dynamicspawncontrol.event.OnEventWindowTitle;

/**
 *
 */
public final class RegisterTechnicalClass
{
    /**
     *
     */
    private static final Class<?>[] EVENT_CLASSES =
    {
        //OnEventWorldCache.class,
        //OnEventSandBox.class, //-' TODO: Перенести девелоп класс в отдельный регистр, положил половину игры на релизу
        //OnEventWindowTitle.class,
        //OnEventPlayer.class,
        //OnEventCheckSpawn.class,
        //OnEventDropExperience.class,
        //OnEventDropItem.class,
        //OnEventMobTaskManager.class,
        //OnEventPopulationChunk.class,
        //OnEventPotentialSpawn.class,
        //OnEventRenderFPS.class
    };

    /**
     *
     */
    public RegisterTechnicalClass()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    /**
     *
     */
    public static void registerClasses()
    {
        for (Class<?> _class : EVENT_CLASSES)
        {
            try
            {
                Object object =
                        _class.getConstructor().newInstance();

                MinecraftForge.EVENT_BUS.register(object);
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(2, "Exception in class: " + _class.getName() + " - " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
    }
}
