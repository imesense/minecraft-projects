package org.imesense.dynamicspawncontrol.managercommands;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@TODO(value = "In development. A completely new concept of using console commands to separate the client and server", showOnce = false, priority = TODO.TodoPriority.HIGH)
public class CommandManager
{
    private static final List<AnnotatedCommand> commands = new ArrayList<>();

    static
    {
        registerCommand(new CmdWorldSeed());
    }

    private static void registerCommand(AnnotatedCommand command)
    {
        commands.add(command);
    }

    /**
     *
     * @param event
     */
    public static void registerCommands(FMLServerStartingEvent event)
    {
        for (AnnotatedCommand command : commands)
        {
            event.registerServerCommand(command);
            Log.write(0, "[Dynamic Spawn Control] Зарегистрирована команда: " + command.getName());
        }
    }

    /**
     *
     * @return
     */
    public static List<AnnotatedCommand> getCommands()
    {
        return new ArrayList<>(commands);
    }
}