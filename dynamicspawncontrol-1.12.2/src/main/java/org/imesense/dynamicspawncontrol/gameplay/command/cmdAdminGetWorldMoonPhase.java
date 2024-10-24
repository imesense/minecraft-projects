package org.imesense.dynamicspawncontrol.gameplay.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldProvider;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.enumeration.EnumCmdCallType;
import org.imesense.dynamicspawncontrol.technical.customlibrary.enumeration.EnumTextColor;
import org.imesense.dynamicspawncontrol.technical.customlibrary.enumeration.EnumUnicodeCharacter;

import javax.annotation.Nonnull;

/**
 *
 */
public final class CmdAdminGetWorldMoonPhase extends CommandBase
{
    /**
     *
     */
    public CmdAdminGetWorldMoonPhase()
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
                   EnumUnicodeCharacter.SECTION.getCharacter() +
                        EnumTextColor.AQUA.getCode() +
                        EnumCmdCallType.CMD.getDescription() +
                        EnumUnicodeCharacter.WHITE_SPACE.getCharacter() +
                        "-> The current phase of the moon: " +
                        moonPhase));
    }
}
