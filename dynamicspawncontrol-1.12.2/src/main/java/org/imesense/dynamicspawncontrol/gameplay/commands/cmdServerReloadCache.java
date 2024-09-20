package org.imesense.dynamicspawncontrol.gameplay.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.imesense.dynamicspawncontrol.technical.customlibrary.*;
import org.imesense.dynamicspawncontrol.technical.worldcache.CacheConfig;

import javax.annotation.Nonnull;

/**
 *
 */
public final class cmdServerReloadCache extends CommandBase
{
    /**
     *
     * @param nameClass
     */
    public cmdServerReloadCache(final String nameClass)
    {
        Log.writeDataToLogFile(0, nameClass);
    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_reload_cache";
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
        return "/dsc_reload_cache";
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
                    EnumUnicodeCharacters.SECTION.getCharacter() +
                            EnumTextColors.RED.getCode() +
                            EnumCmdCalledType.CMD.getDescription() +
                            EnumUnicodeCharacters.WHITE_SPACE.getCharacter() +
                            "The command does not accept arguments"));
        }
        else
        {
            CacheConfig.getClassInstance().reloadConfig();

            sender.sendMessage(new TextComponentString(
                    EnumUnicodeCharacters.SECTION.getCharacter() +
                            EnumTextColors.GREEN.getCode() +
                            EnumCmdCalledType.CMD.getDescription() +
                            EnumUnicodeCharacters.WHITE_SPACE.getCharacter() +
                            "Configurations have been reloaded"));
        }
    }
}
