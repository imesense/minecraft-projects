package org.imesense.dynamicspawncontrol.technical.customlibrary;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 *
 */
public final class RayTrace
{
    /**
     *
     * @param worldIn
     * @param playerIn
     * @param useLiquids
     * @return
     */
    public static RayTraceResult getMovingObjectPositionFromPlayer(World worldIn, EntityPlayer playerIn, boolean useLiquids)
    {
        float pitch = playerIn.rotationPitch;
        float yaw = playerIn.rotationYaw;

        Vec3d vec3 = getPlayerEyes(playerIn);

        float f2 = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-pitch * 0.017453292F);
        float f5 = MathHelper.sin(-pitch * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;

        double reach = 5.00;

        if (playerIn instanceof net.minecraft.entity.player.EntityPlayerMP)
        {
            reach = ((EntityPlayerMP)playerIn).interactionManager.getBlockReachDistance();
        }

        Vec3d vec31 = vec3.addVector(f6 * reach, f5 * reach, f7 * reach);

        return worldIn.rayTraceBlocks(vec3, vec31, useLiquids, !useLiquids, false);
    }

    /**
     *
     * @param playerIn
     * @return
     */
    private static Vec3d getPlayerEyes(EntityPlayer playerIn)
    {
        double x = playerIn.posX;
        double y = playerIn.posY + playerIn.getEyeHeight();
        double z = playerIn.posZ;

        return new Vec3d(x, y, z);
    }

    /**
     *
     * @param world
     * @param player
     * @return
     */
    public static boolean isPlayerStandingOnBlock(World world, EntityPlayer player)
    {
        double posX = player.posX;
        double posY = player.posY;
        double posZ = player.posZ;

        double adjustedY = posY - 0.1;

        BlockPos blockPosBelow = new BlockPos(posX, adjustedY, posZ);

        IBlockState blockStateBelow = world.getBlockState(blockPosBelow);
        Block blockBelow = blockStateBelow.getBlock();

        return blockBelow != Blocks.AIR;
    }

    /**
     *
     * @param player
     * @return
     */
    public static BlockPos getBlockPosBelowPlayer(EntityPlayer player)
    {
        double posX = player.posX;
        double posY = player.posY - 0.1;
        double posZ = player.posZ;

        return new BlockPos(posX, posY, posZ);
    }
}
