package org.imesense.dynamicspawncontrol.command;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.imesense.dynamicspawncontrol.command.base.AnnotatedCommand;
import org.imesense.dynamicspawncontrol.command.gameplay.WorldSeedCommand;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;

import java.util.ArrayList;
import java.util.List;

@TODO(value = "In development. A completely new concept of using console commands to separate the client and server", showOnce = false, priority = TODO.TodoPriority.HIGH)
public class CommandManager
{
    private static final List<AnnotatedCommand> commands = new ArrayList<>();

    static
    {
        registerCommand(new WorldSeedCommand());
    }

    private static void registerCommand(AnnotatedCommand command)
    {
        commands.add(command);
    }

    public static void registerCommands(FMLServerStartingEvent event)
    {
        for (AnnotatedCommand command : commands)
        {
            event.registerServerCommand(command);
            LogManager.info("[Dynamic Spawn Control] Зарегистрирована команда: " + command.getName());
        }
    }

    public static List<AnnotatedCommand> getCommands()
    {
        return new ArrayList<>(commands);
    }
}
