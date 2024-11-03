package org.imesense.dynamicspawncontrol.gameplay.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.enumeration.EnumTextColor;
import org.imesense.dynamicspawncontrol.technical.customlibrary.enumeration.EnumUnicodeCharacter;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import javax.annotation.Nonnull;

/**
 *
 */
public final class CmdAdminGameMode extends CommandBase
{
    /**
     *
     */
    public CmdAdminGameMode()
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
        return "dsc_gm";
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
        return "/dsc_gm";
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
        if (args.length != 1)
        {
            iCommandSender.sendMessage(new TextComponentString(getUsage(iCommandSender)));
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

            iCommandSender.sendMessage(new TextComponentString(EnumUnicodeCharacter.SECTION.getCharacter() +
                    EnumTextColor.RED.getCode() +
                    "Invalid game mode: " + args[0]));

            return;
        }

        if (iCommandSender instanceof EntityPlayerMP)
        {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) iCommandSender;
            entityPlayerMP.setGameType(GameType.getByID(mode));
        }
    }
}
