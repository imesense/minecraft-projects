package org.imesense.dynamicspawncontrol.core.raytrace;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class RayTrace
{
    public RayTrace()
    {

    }

    public static RayTraceResult getMovingObjectPositionFromPlayer(World worldIn, EntityPlayerMP entityPlayerMP, boolean useLiquids)
    {
        float pitch = entityPlayerMP.rotationPitch;
        float yaw = entityPlayerMP.rotationYaw;

        Vec3d vec3 = getPlayerEyes(entityPlayerMP);

        float f2 = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-pitch * 0.017453292F);
        float f5 = MathHelper.sin(-pitch * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;

        @Deprecated
        double reach = entityPlayerMP.interactionManager.getBlockReachDistance();

        Vec3d vec31 = vec3.addVector(f6 * reach, f5 * reach, f7 * reach);

        return worldIn.rayTraceBlocks(vec3, vec31, useLiquids, !useLiquids, false);
    }

    private static Vec3d getPlayerEyes(EntityPlayerMP entityPlayerMP)
    {
        double x = entityPlayerMP.posX;
        double y = entityPlayerMP.posY + entityPlayerMP.getEyeHeight();
        double z = entityPlayerMP.posZ;

        return new Vec3d(x, y, z);
    }

    public static boolean isPlayerStandingOnBlock(World world, EntityPlayerMP entityPlayerMP)
    {
        double posX = entityPlayerMP.posX;
        double posY = entityPlayerMP.posY;
        double posZ = entityPlayerMP.posZ;

        double adjustedY = posY - 0.10;

        BlockPos blockPos = new BlockPos(posX, adjustedY, posZ);

        IBlockState blockStateBelow = world.getBlockState(blockPos);
        Block block = blockStateBelow.getBlock();

        return block != Blocks.AIR;
    }

    public static BlockPos getBlockPosBelowPlayer(EntityPlayerMP entityPlayerMP)
    {
        double posX = entityPlayerMP.posX;
        double posY = entityPlayerMP.posY - 0.10;
        double posZ = entityPlayerMP.posZ;

        return new BlockPos(posX, posY, posZ);
    }
}
