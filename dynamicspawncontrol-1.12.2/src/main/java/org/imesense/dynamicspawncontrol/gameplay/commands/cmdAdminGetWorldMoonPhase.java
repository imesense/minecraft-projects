package org.imesense.dynamicspawncontrol.gameplay.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldProvider;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.*;

import javax.annotation.Nonnull;

/**
 *
 */
public final class cmdAdminGetWorldMoonPhase extends CommandBase
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public cmdAdminGetWorldMoonPhase()
    {
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;

        CodeGenericUtils.printInitClassToLog(cmdAdminGetWorldMoonPhase.class);
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
