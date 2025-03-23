package org.imesense.dynamicspawncontrol.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.collection.TextColorCollection;
import org.imesense.dynamicspawncontrol.core.collection.UnicodeCharacterCollection;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import javax.annotation.Nonnull;

@InitLog

public final class CmdAdminGameMode extends CommandBase
{
    public CmdAdminGameMode()
    {

    }

    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_gm";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender)
    {
        return "/dsc_gm";
    }

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

            iCommandSender.sendMessage(new TextComponentString(UnicodeCharacterCollection.instance.getDescription('\u00A7') +
                    TextColorCollection.instance.getCode("RED") +
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
