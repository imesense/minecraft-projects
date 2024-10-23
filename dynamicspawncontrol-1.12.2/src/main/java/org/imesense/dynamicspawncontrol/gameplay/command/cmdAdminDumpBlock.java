package org.imesense.dynamicspawncontrol.gameplay.command;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.customlibrary.RayTrace;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 *
 */
public final class cmdAdminDumpBlock extends CommandBase
{
    /**
     *
     */
    public cmdAdminDumpBlock()
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
        return "dsc_dump_block";
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
        return "/dsc_dump_block";
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
        if (sender instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            RayTraceResult result = RayTrace.getMovingObjectPositionFromPlayer(player.getEntityWorld(), player, false);

            if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                BlockPos blockPos = result.getBlockPos();
                IBlockState state = player.getEntityWorld().getBlockState(blockPos);
                int blockId = Block.getIdFromBlock(state.getBlock());

                sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "Block ID: " + blockId));
                Log.writeDataToLogFile(0, "Block ID: " + blockId);
                sender.sendMessage(new TextComponentString(TextFormatting.GOLD + Objects.requireNonNull(state.getBlock().getRegistryName()).toString()));
                Log.writeDataToLogFile(0, Objects.requireNonNull(state.getBlock().getRegistryName()).toString());

                for (IProperty<?> key : state.getPropertyKeys())
                {
                    String value = state.getValue(key).toString();
                    sender.sendMessage(new TextComponentString("State: " + key.getName() + " = " + value));
                    Log.writeDataToLogFile(0, "State: " + key.getName() + " = " + value);
                }

                TileEntity tileEntity = player.getEntityWorld().getTileEntity(blockPos);

                if (tileEntity != null)
                {
                    NBTTagCompound nbt = tileEntity.writeToNBT(new NBTTagCompound());
                    sender.sendMessage(new TextComponentString("NBT Tags: " + nbt));
                    Log.writeDataToLogFile(0, "NBT Tags: " + nbt);
                }
            }
        }
        else
        {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "This command can only be used by players!"));
        }
    }
}
