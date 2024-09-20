package org.imesense.dynamicspawncontrol.technical.initializer;

import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import org.imesense.dynamicspawncontrol.gameplay.commands.*;

import static org.imesense.dynamicspawncontrol.debug.CodeGenericUtils.hasConstructorWithParameter;

/**
 *
 */
public final class RegisterCommandsClasses
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
    public RegisterCommandsClasses(final String nameClass)
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
                Log.writeDataToLogFile(0, "Reading class: " + cmdClass.getName());

                Object commandInstance = cmdClass.getConstructor(String.class).newInstance(cmdClass.getSimpleName());
                event.registerServerCommand((ICommand)commandInstance);
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(2, "Exception in class: " + cmdClass.getName() + " - " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
    }
}
