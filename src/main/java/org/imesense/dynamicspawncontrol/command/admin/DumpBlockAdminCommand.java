package org.imesense.dynamicspawncontrol.command.admin;

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
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.core.raytrace.RayTrace;

import javax.annotation.Nonnull;
import java.util.Objects;

@InitLog
public final class DumpBlockAdminCommand extends CommandBase
{
    public DumpBlockAdminCommand()
    {

    }

    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_dump_block";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender)
    {
        return "/dsc_dump_block";
    }

    @Override
    public void execute(@Nonnull MinecraftServer minecraftServer, @Nonnull ICommandSender iCommandSender, @Nonnull String... args)
    {
        if (iCommandSender instanceof EntityPlayerMP)
        {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) iCommandSender;
            RayTraceResult rayTraceResult =
                    RayTrace.getMovingObjectPositionFromPlayer(entityPlayerMP.getEntityWorld(), entityPlayerMP, false);

            if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                BlockPos blockPos = rayTraceResult.getBlockPos();
                IBlockState iBlockState = entityPlayerMP.getEntityWorld().getBlockState(blockPos);
                int blockId = Block.getIdFromBlock(iBlockState.getBlock());

                iCommandSender.sendMessage(new TextComponentString(TextFormatting.GOLD + "Block ID: " + blockId));

                LogManager.info("Block ID: " + blockId);

                iCommandSender.sendMessage(new TextComponentString(TextFormatting.GOLD +
                        Objects.requireNonNull(iBlockState.getBlock().getRegistryName()).toString()));

                LogManager.info(Objects.requireNonNull(iBlockState.getBlock().getRegistryName()).toString());

                for (IProperty<?> key : iBlockState.getPropertyKeys())
                {
                    String getString = iBlockState.getValue(key).toString();
                    iCommandSender.sendMessage(new TextComponentString("State: " + key.getName() + " = " + getString));
                    LogManager.info("State: " + key.getName() + " = " + getString);
                }

                TileEntity tileEntity = entityPlayerMP.getEntityWorld().getTileEntity(blockPos);

                if (tileEntity != null)
                {
                    NBTTagCompound nbtTagCompound = tileEntity.writeToNBT(new NBTTagCompound());
                    iCommandSender.sendMessage(new TextComponentString("NBT Tags: " + nbtTagCompound));
                    LogManager.info("NBT Tags: " + nbtTagCompound);
                }
            }
        }
        else
        {
            iCommandSender.sendMessage(new TextComponentString(TextFormatting.RED + "This command can only be used by players!"));
        }
    }
}
