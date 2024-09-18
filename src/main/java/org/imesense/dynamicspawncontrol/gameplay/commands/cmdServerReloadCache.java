package org.imesense.dynamicspawncontrol.gameplay.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.imesense.dynamicspawncontrol.technical.customlibrary.AuxFunctions;
import org.imesense.dynamicspawncontrol.technical.customlibrary.CmdCalledType;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.customlibrary.TextEnumColors;
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
                    AuxFunctions.UNICODE.SECTION +
                            TextEnumColors.RED.getCode() +
                            CmdCalledType.CMD.getDescription() +
                            AuxFunctions.UNICODE.WHITE_SPACE +
                            "The command does not accept arguments"));
        }
        else
        {
            CacheConfig.getClassInstance().reloadConfig();

            sender.sendMessage(new TextComponentString(
                    AuxFunctions.UNICODE.SECTION +
                            TextEnumColors.GREEN.getCode() +
                            CmdCalledType.CMD.getDescription() +
                            AuxFunctions.UNICODE.WHITE_SPACE +
                            "Configurations have been reloaded"));
        }
    }
}
