package org.imesense.dynamicspawncontrol.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.collection.CmdCallTypeCollection;
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
        CodeGeneric.printInitClassToLog(this.getClass());
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
     * @param iCommandSender
     * @return
     */
    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender)
    {
        return "/dsc_json_scripts_reload";
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
                            CmdCallTypeCollection.instance.getDescription(1) +
                            EnumUnicodeCharacter.WHITE_SPACE.getCharacter() +
                            "The command does not accept arguments"));
        }
        else
        {
            ParserGenericJsonScript.reloadRules();

            iCommandSender.sendMessage(new TextComponentString(
                       EnumUnicodeCharacter.SECTION.getCharacter() +
                            EnumTextColor.GREEN.getCode() +
                            CmdCallTypeCollection.instance.getDescription(1) +
                            EnumUnicodeCharacter.WHITE_SPACE.getCharacter() +
                            "Configurations have been reloaded"));
        }
    }
}
