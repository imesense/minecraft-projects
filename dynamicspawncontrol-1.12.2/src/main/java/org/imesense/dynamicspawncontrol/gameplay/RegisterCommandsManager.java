package org.imesense.dynamicspawncontrol.gameplay;

import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import org.imesense.dynamicspawncontrol.gameplay.commands.*;

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
        cmdAdminDumpBlock.class,
        cmdAdminDumpEntity.class,
        cmdAdminDumpItem.class,
        cmdAdminGameMode.class,
        cmdAdminGetWorldMoonPhase.class,
        cmdAdminLaunchFireball.class,
        cmdAdminScanEntities.class,
        cmdAdminSwitchVanish.class,
        cmdServerJsonScriptsReload.class,
        cmdServerMobsKiller.class,
        cmdServerReloadCache.class,
        cmdServerSingleScriptsReload.class
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
