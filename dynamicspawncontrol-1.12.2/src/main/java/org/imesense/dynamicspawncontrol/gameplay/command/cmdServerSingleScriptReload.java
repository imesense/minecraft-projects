package org.imesense.dynamicspawncontrol.gameplay.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.enumeration.EnumCmdCallType;
import org.imesense.dynamicspawncontrol.technical.customlibrary.enumeration.EnumTextColor;
import org.imesense.dynamicspawncontrol.technical.customlibrary.enumeration.EnumUnicodeCharacter;
import org.imesense.dynamicspawncontrol.technical.parser.ParserManager;

import javax.annotation.Nonnull;

/**
 *
 */
public final class CmdServerSingleScriptReload extends CommandBase
{
    /**
     *
     */
    public CmdServerSingleScriptReload()
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
        return "dsc_single_scripts_reload";
    }

    /**
     *
     * @param iCommandSender
     * @return
     */
    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender)
    {
        return "/dsc_single_scripts_reload";
    }

    /**
     *
     * @param minecraftServer
     * @param iCommandSender
     * @param args
     */
    @Override
    public void execute(@Nonnull MinecraftServer minecraftServer, @Nonnull ICommandSender iCommandSender, @Nonnull String... args)
    {
        if (args.length > 0)
        {
            iCommandSender.sendMessage(new TextComponentString(
                    EnumUnicodeCharacter.SECTION.getCharacter() +
                            EnumTextColor.RED.getCode() +
                            EnumCmdCallType.CMD.getDescription() +
                            EnumUnicodeCharacter.WHITE_SPACE.getCharacter() +
                            "The command does not accept arguments"));
        }
        else
        {
            ParserManager.reloadAllConfigs();

            iCommandSender.sendMessage(new TextComponentString(
                    EnumUnicodeCharacter.SECTION.getCharacter() +
                            EnumTextColor.GREEN.getCode() +
                            EnumCmdCallType.CMD.getDescription() +
                            EnumUnicodeCharacter.WHITE_SPACE.getCharacter() +
                            "Configurations have been reloaded"));
        }
    }
}
