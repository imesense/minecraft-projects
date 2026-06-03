package org.imesense.dynamicspawncontrol.command.admin;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;

import javax.annotation.Nonnull;

@InitLog
@TODO(value = "New concept for 0.2 update", showOnce = false, priority = TODO.TodoPriority.HIGH)
public final class CmdAdminCreateBedrockPlatform extends CommandBase
{
    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_test";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender)
    {
        return "/dsc_test";
    }

    @Override
    public void execute(@Nonnull MinecraftServer minecraftServer,
                        @Nonnull ICommandSender iCommandSender,
                        @Nonnull String... args)
    {
        if (!(iCommandSender instanceof EntityPlayerMP))
        {
            iCommandSender.sendMessage(new TextComponentString("This command can only be used by players!"));
            return;
        }

        if (args.length < 3)
        {
            iCommandSender.sendMessage(new TextComponentString(
                    "Usage: /dsc_test <north|south|east|west> <length> <width>"
            ));
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) iCommandSender;
        World world = player.getEntityWorld();

        String direction = args[0].toLowerCase();

        int length;
        int width;

        try
        {
            length = Integer.parseInt(args[1]);
            width  = Integer.parseInt(args[2]);
        }
        catch (NumberFormatException e)
        {
            iCommandSender.sendMessage(new TextComponentString("Length and width must be numbers!"));
            return;
        }

        if (length <= 0 || width <= 0)
        {
            iCommandSender.sendMessage(new TextComponentString("Length and width must be positive values!"));
            return;
        }

        int halfWidth = width / 2;
        int y = 254;

        BlockPos base = player.getPosition();
        int baseX = base.getX();
        int baseZ = base.getZ();

        int dxStart = 0, dxEnd = 0;
        int dzStart = 0, dzEnd = 0;

        switch (direction)
        {
            case "north":
                dzStart = -length;
                dzEnd   = 0;
                dxStart = -halfWidth;
                dxEnd   = halfWidth;
                break;

            case "south":
                dzStart = 0;
                dzEnd   = length;
                dxStart = -halfWidth;
                dxEnd   = halfWidth;
                break;

            case "west":
                dxStart = -length;
                dxEnd   = 0;
                dzStart = -halfWidth;
                dzEnd   = halfWidth;
                break;

            case "east":
                dxStart = 0;
                dxEnd   = length;
                dzStart = -halfWidth;
                dzEnd   = halfWidth;
                break;

            default:
                iCommandSender.sendMessage(new TextComponentString(
                        "Invalid direction. Use north, south, east or west."
                ));
                return;
        }

        for (int dx = dxStart; dx <= dxEnd; dx++)
        {
            for (int dz = dzStart; dz <= dzEnd; dz++)
            {
                BlockPos pos = new BlockPos(baseX + dx, y, baseZ + dz);
                world.setBlockState(pos, Blocks.BEDROCK.getDefaultState(), 2);
            }
        }

        iCommandSender.sendMessage(new TextComponentString(
                "Generated bedrock platform: direction=" + direction +
                        ", length=" + length +
                        ", width=" + width +
                        ", Y=255"
        ));
    }
}