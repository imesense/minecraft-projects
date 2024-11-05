package org.imesense.dynamicspawncontrol.core.register;

import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.imesense.dynamicspawncontrol.command.*;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

/**
 *
 * OldSerpskiStalker:
 * Console commands are loaded every time you enter the world.
 * A constructor with protection is not required!
 */
public final class RegisterCommandClass
{
    /**
     *
     */
    private static final Class<?>[] COMMAND_CLASSES =
    {
        CmdAdminDumpBlock.class,
        CmdAdminDumpEntity.class,
        CmdAdminDumpItem.class,
        CmdAdminGameMode.class,
        CmdAdminGetWorldMoonPhase.class,
        CmdAdminLaunchFireball.class,
        CmdAdminScanEntities.class,
        CmdAdminSwitchVanish.class,
        CmdServerJsonScriptReload.class,
        CmdServerMobsKiller.class,
        CmdServerSingleScriptReload.class
    };

    /**
     *
     */
    public RegisterCommandClass()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    /**
     *
     * @param fmlServerStartingEvent
     */
    public static void registerCommands(FMLServerStartingEvent fmlServerStartingEvent)
    {
        for (Class<?> cmdClass : COMMAND_CLASSES)
        {
            try
            {
                Object object =
                        cmdClass.getConstructor().newInstance();

                fmlServerStartingEvent.registerServerCommand((ICommand) object);
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(2, "Exception in class: " + cmdClass.getName() + " - " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
    }
}
