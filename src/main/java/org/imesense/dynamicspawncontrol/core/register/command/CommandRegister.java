package org.imesense.dynamicspawncontrol.core.register.command;

import org.imesense.dynamicspawncontrol.command.*;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseregister.BaseCommandRegister;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

public final class CommandRegister extends BaseCommandRegister
{
    private static volatile CommandRegister _INSTANCE;

    public static CommandRegister getInstance()
    {
        return CodeGeneric.getInstance(CommandRegister.class);
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
        CmdAdminGetDimension.class,
        CmdClientCleanMemory.class
    };

    @Override
    protected Class<?>[] getCommandClasses()
    {
        return COMMAND_CLASSES;
    }
}
