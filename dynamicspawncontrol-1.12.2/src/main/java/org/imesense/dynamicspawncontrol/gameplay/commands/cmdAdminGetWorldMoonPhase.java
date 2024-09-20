package org.imesense.dynamicspawncontrol.gameplay.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldProvider;
import org.imesense.dynamicspawncontrol.technical.customlibrary.*;

import javax.annotation.Nonnull;

/**
 *
 */
public final class cmdAdminGetWorldMoonPhase extends CommandBase
{
    /**
     *
     * @param nameClass
     */
    public cmdAdminGetWorldMoonPhase(final String nameClass)
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
        return "dsc_moon_phase";
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
        return "/dsc_moon_phase";
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
        WorldProvider worldProvider = sender.getEntityWorld().provider;
        int moonPhase = worldProvider.getMoonPhase(sender.getEntityWorld().getWorldTime());

        sender.sendMessage(new TextComponentString(
                   EnumUnicodeCharacters.SECTION.getCharacter() +
                        EnumTextColors.AQUA.getCode() +
                        EnumCmdCalledType.CMD.getDescription() +
                        EnumUnicodeCharacters.WHITE_SPACE.getCharacter() +
                        "-> The current phase of the moon: " +
                        moonPhase));
    }
}
