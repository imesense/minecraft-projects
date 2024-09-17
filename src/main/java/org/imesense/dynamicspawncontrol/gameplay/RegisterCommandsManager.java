package org.imesense.dynamicspawncontrol.gameplay;

import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
public class RegisterCommandsManager
{
    /**
     *
     */
    private static final Class<?>[] COMMAND_CLASSES =
    {

    };

    /**
     *
     * @param nameClass
     */
    public RegisterCommandsManager(final String nameClass)
    {

    }

    /**
     *
     * @param event
     */
    public static void registerCommands(FMLServerStartingEvent event)
    {
        for (Class<?> cmdClass : COMMAND_CLASSES)
        {
            try
            {
                Object commandInstance = cmdClass.getConstructor(String.class).newInstance(cmdClass.getSimpleName());
                event.registerServerCommand((ICommand)commandInstance);
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(Log.TypeLog[2], "Exception in class: " + cmdClass.getName() + " - " + exception.getMessage());
            }
        }
    }
}
