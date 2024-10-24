package org.imesense.dynamicspawncontrol.technical.register;

import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import org.imesense.dynamicspawncontrol.gameplay.command.*;

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
        CodeGenericUtil.printInitClassToLog(this.getClass());
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
