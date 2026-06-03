package org.imesense.dynamicspawncontrol.core.register.command;

import org.imesense.dynamicspawncontrol.command.admin.*;
import org.imesense.dynamicspawncontrol.command.client.*;
import org.imesense.dynamicspawncontrol.command.server.*;
import org.imesense.dynamicspawncontrol.core.base.BaseCommandRegister;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

public final class CommandRegister extends BaseCommandRegister
{
    private static volatile CommandRegister _INSTANCE;

    public static CommandRegister getInstance()
    {
        return CodeGeneric.getInstance(CommandRegister.class);
    }

    public CommandRegister()
    {

    }

    private static final Class<?>[] COMMAND_CLASSES =
    {
        DumpBlockAdminCommand.class,
        DumpEntityAdminCommand.class,
        DumpItemAdminCommand.class,
        GameModeAdminCommand.class,
        GetWorldMoonPhaseAdminCommand.class,
        LaunchFireballAdminCommand.class,
        ScanEntitiesAdminCommand.class,
        SwitchVanishAdminCommand.class,
        MobKillerServerCommand.class,
        ReloadScriptServerCommand.class,
        GetDimensionAdminCommand.class,
        CleanMemoryClientCommand.class,
        CopyWorldSeedAdminCommand.class,
        GiveBowAdminCommand.class,
        GiveSwordAdminCommand.class,
        SetTimeAdminCommand.class,
        CreateBedrockPlatformAdminCommand.class
    };

    @Override
    protected Class<?>[] getCommandClasses()
    {
        return COMMAND_CLASSES;
    }
}
