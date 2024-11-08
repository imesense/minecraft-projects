package org.imesense.dynamicspawncontrol.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldProvider;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.collection.CmdCallTypeCollection;
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
        return "dsc_moon_phase";
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
        return "/dsc_moon_phase";
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
        WorldProvider worldProvider = iCommandSender.getEntityWorld().provider;
        int moonPhase = worldProvider.getMoonPhase(iCommandSender.getEntityWorld().getWorldTime());

        iCommandSender.sendMessage(new TextComponentString(
                   EnumUnicodeCharacter.SECTION.getCharacter() +
                        EnumTextColor.AQUA.getCode() +
                        CmdCallTypeCollection.instance.getDescription(1) +
                        EnumUnicodeCharacter.WHITE_SPACE.getCharacter() +
                        "-> The current phase of the moon: " +
                        moonPhase));
    }
}
