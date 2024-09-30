package org.imesense.dynamicspawncontrol.technical.initializer;

import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import org.imesense.dynamicspawncontrol.gameplay.commands.*;

/**
 *
 * OldSerpskiStalker:
 * Console commands are loaded every time you enter the world.
 * A constructor with protection is not required!
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
        cmdServerSingleScriptsReload.class
    };

    /**
     *
     */
    public RegisterCommandsClasses()
    {
        CodeGenericUtils.printInitClassToLog(this.getClass());
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
                Object commandInstance =
                        cmdClass.getConstructor().newInstance();

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
