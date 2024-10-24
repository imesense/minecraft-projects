package org.imesense.dynamicspawncontrol.gameplay.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.enumeration.EnumCmdCallType;
import org.imesense.dynamicspawncontrol.technical.customlibrary.enumeration.EnumTextColor;
import org.imesense.dynamicspawncontrol.technical.customlibrary.enumeration.EnumUnicodeCharacter;
import org.imesense.dynamicspawncontrol.technical.parser.ParserGenericJsonScript;

import javax.annotation.Nonnull;

/**
 *
 */
public final class CmdServerJsonScriptReload extends CommandBase
{
    /**
     *
     */
    public CmdServerJsonScriptReload()
    {
        CodeGenericUtil.printInitClassToLog(this.getClass());
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
                       EnumUnicodeCharacter.SECTION.getCharacter() +
                            EnumTextColor.RED.getCode() +
                            EnumCmdCallType.CMD.getDescription() +
                            EnumUnicodeCharacter.WHITE_SPACE.getCharacter() +
                            "The command does not accept arguments"));
        }
        else
        {
            ParserGenericJsonScript.reloadRules();

            sender.sendMessage(new TextComponentString(
                       EnumUnicodeCharacter.SECTION.getCharacter() +
                            EnumTextColor.GREEN.getCode() +
                            EnumCmdCallType.CMD.getDescription() +
                            EnumUnicodeCharacter.WHITE_SPACE.getCharacter() +
                            "Configurations have been reloaded"));
        }
    }
}
