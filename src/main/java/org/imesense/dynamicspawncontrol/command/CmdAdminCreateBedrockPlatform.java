package org.imesense.dynamicspawncontrol.command;

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
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender iCommandSender, @Nonnull String... args)
    {
        if (!(iCommandSender instanceof EntityPlayerMP))
        {
            iCommandSender.sendMessage(new TextComponentString("This command can only be used by players!"));
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) iCommandSender;
        World world = player.getEntityWorld();

        BlockPos center = player.getPosition();
        int platformSize = 72;
        int halfSize = platformSize / 2;
        int y = 254;

        for (int dx = -halfSize; dx <= halfSize; dx++)
        {
            for (int dz = -halfSize; dz <= halfSize; dz++)
            {
                BlockPos pos = new BlockPos(center.getX() + dx, y, center.getZ() + dz);
                world.setBlockState(pos, Blocks.BEDROCK.getDefaultState(), 2);
            }
        }

        iCommandSender.sendMessage(new TextComponentString("Generated bedrock platform at Y = 255"));
    }
}