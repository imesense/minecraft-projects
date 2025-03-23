package org.imesense.dynamicspawncontrol.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.collection.TextColorCollection;
import org.imesense.dynamicspawncontrol.core.collection.UnicodeCharacterCollection;
import org.imesense.dynamicspawncontrol.core.register.parser.ParserRegister;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.collection.CmdCallTypeCollection;

import javax.annotation.Nonnull;

@InitLog
public final class CmdServerScriptReload extends CommandBase
{
    public CmdServerScriptReload()
    {

    }

    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_reload_scripts";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender)
    {
        return "/dsc_reload_scripts";
    }

    @Override
    public void execute(@Nonnull MinecraftServer minecraftServer, @Nonnull ICommandSender iCommandSender, @Nonnull String... args)
    {
        if (args.length > 0)
        {
            iCommandSender.sendMessage(new TextComponentString(
                       UnicodeCharacterCollection.instance.getDescription('\u00A7') +
                            TextColorCollection.instance.getCode("RED") +
                            CmdCallTypeCollection.instance.getDescription(1) +
                            UnicodeCharacterCollection.instance.getDescription(' ') +
                            "The command does not accept arguments"));
        }
        else
        {
            ParserRegister.getInstance().reloadAllConfigs();

            iCommandSender.sendMessage(new TextComponentString(
                       UnicodeCharacterCollection.instance.getDescription('\u00A7') +
                            TextColorCollection.instance.getCode("GREEN") +
                            CmdCallTypeCollection.instance.getDescription(1) +
                            UnicodeCharacterCollection.instance.getDescription(' ') +
                            "Configurations have been reloaded"));
        }
    }
}
