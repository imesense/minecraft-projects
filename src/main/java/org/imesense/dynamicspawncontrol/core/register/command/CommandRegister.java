package org.imesense.dynamicspawncontrol.core.register.command;

import org.imesense.dynamicspawncontrol.command.*;
import org.imesense.dynamicspawncontrol.core.baseregister.BaseCommandRegister;

public class CommandRegister extends BaseCommandRegister
{
    private static volatile CommandRegister _INSTANCE;

    public static CommandRegister getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (CommandRegister.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new CommandRegister();
                }
            }
        }

        return _INSTANCE;
    }

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
        CmdServerMobsKiller.class,
        CmdServerScriptReload.class,
        CmdAdminGetDimension.class
    };

    @Override
    protected Class<?>[] getCommandClasses()
    {
        return COMMAND_CLASSES;
    }
}
