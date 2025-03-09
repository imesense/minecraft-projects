package org.imesense.dynamicspawncontrol.core.register.command;

import org.imesense.dynamicspawncontrol.command.*;
import org.imesense.dynamicspawncontrol.core.baseregister.BaseCommandRegister;

public class Register extends BaseCommandRegister
{
    private static volatile Register _INSTANCE;

    public static Register getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (Register.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new Register();
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
