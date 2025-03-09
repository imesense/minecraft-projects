package org.imesense.dynamicspawncontrol.core.register.command;

import org.imesense.dynamicspawncontrol.command.*;
import org.imesense.dynamicspawncontrol.core.api.BaseCommandRegister;

public class RegisterCommand extends BaseCommandRegister
{
    private static volatile RegisterCommand _INSTANCE;

    public static RegisterCommand getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (RegisterCommand.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new RegisterCommand();
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
