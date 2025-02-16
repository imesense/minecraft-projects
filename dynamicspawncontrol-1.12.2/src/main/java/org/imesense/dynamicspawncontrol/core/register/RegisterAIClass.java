package org.imesense.dynamicspawncontrol.core.register;

import net.minecraftforge.common.MinecraftForge;
import org.imesense.dynamicspawncontrol.ai.spider.event.OnEventAvoidLight;
import org.imesense.dynamicspawncontrol.ai.zombie.event.OnEventBreakTorch;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.handler.FogEventHandler;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.capability.EventHandler;

/**
 * 
 */
public final class RegisterAIClass
{
    /**
     *
     */
    private static final Class<?>[] EVENT_CLASSES =
    {
        EventHandler.class,
        OnEventBreakTorch.class,
        OnEventAvoidLight.class//,
           // FogEventHandler.class
    };

    /**
     *
     */
    public RegisterAIClass()
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
