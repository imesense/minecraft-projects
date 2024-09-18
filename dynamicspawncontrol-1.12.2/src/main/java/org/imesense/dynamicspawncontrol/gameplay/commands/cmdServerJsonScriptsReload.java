package org.imesense.dynamicspawncontrol.gameplay.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.imesense.dynamicspawncontrol.technical.customlibrary.AuxFunctions;
import org.imesense.dynamicspawncontrol.technical.customlibrary.CmdCalledType;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.customlibrary.TextEnumColors;

import javax.annotation.Nonnull;

/**
 *
 */
public final class cmdServerJsonScriptsReload extends CommandBase
{
    /**
     *
     * @param nameClass
     */
    public cmdServerJsonScriptsReload(final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);
    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_json_scripts_reload";
    }

    /**
     *
     * @param sender
     * @return
     */
    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender)
    {
        return "/dsc_json_scripts_reload";
    }

    /**
     *
     * @param server
     * @param sender
     * @param args
     */
    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String... args)
    {
        if (args.length > 0)
        {
            this.getUsage(sender);

            sender.sendMessage(new TextComponentString(
                    AuxFunctions.UNICODE.SECTION +
                            TextEnumColors.RED.getCode() +
                            CmdCalledType.CMD.getDescription() +
                            AuxFunctions.UNICODE.WHITE_SPACE +
                            "The command does not accept arguments"));
        }
        else
        {
            //ParserJsonScripts.reloadRules();

            sender.sendMessage(new TextComponentString(
                    AuxFunctions.UNICODE.SECTION +
                            TextEnumColors.GREEN.getCode() +
                            CmdCalledType.CMD.getDescription() +
                            AuxFunctions.UNICODE.WHITE_SPACE +
                            "Configurations have been reloaded"));
        }
    }
}
