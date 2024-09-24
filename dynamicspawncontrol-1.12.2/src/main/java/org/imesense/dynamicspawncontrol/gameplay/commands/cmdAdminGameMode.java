package org.imesense.dynamicspawncontrol.gameplay.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.EnumTextColors;
import org.imesense.dynamicspawncontrol.technical.customlibrary.EnumUnicodeCharacters;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import javax.annotation.Nonnull;

/**
 *
 */
public final class cmdAdminGameMode extends CommandBase
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public cmdAdminGameMode()
    {
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;

        CodeGenericUtils.printInitClassToLog(cmdAdminGameMode.class);
    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_gm";
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
        return "/dsc_gm";
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
        if (args.length != 1)
        {
            sender.sendMessage(new TextComponentString(getUsage(sender)));
            return;
        }

        int mode;

        try
        {
            mode = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException exception)
        {
            Log.writeDataToLogFile(2, String.format("Error parsing game mode: %s. Exception: %s", args[0], exception.getMessage()));

            sender.sendMessage(new TextComponentString(EnumUnicodeCharacters.SECTION.getCharacter() +
                    EnumTextColors.RED.getCode() +
                    "Invalid game mode: " + args[0]));

            return;
        }

        if (sender instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            player.setGameType(GameType.getByID(mode));
        }
    }
}
