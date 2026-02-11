package org.imesense.dynamicspawncontrol.satietymanager;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;

public class CommandSetHunger extends CommandBase
{
    @Override
    public String getName()
    {
        return "sethunger";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "/sethunger <0-20> [player]";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2; // OP
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
            throw new CommandException(getUsage(sender));

        int hunger = MathHelper.clamp(parseInt(args[0]), 0, 20);

        EntityPlayer target;

        if (args.length >= 2)
        {
            target = getPlayer(server, sender, args[1]);
        }
        else
        {
            target = getCommandSenderAsPlayer(sender);
        }

        target.getFoodStats().setFoodLevel(hunger);
        target.getFoodStats().setFoodSaturationLevel(0.0F);

        notifyCommandListener(sender, this,
                "Hunger of %s set to %s",
                target.getName(),
                hunger
        );
    }
}