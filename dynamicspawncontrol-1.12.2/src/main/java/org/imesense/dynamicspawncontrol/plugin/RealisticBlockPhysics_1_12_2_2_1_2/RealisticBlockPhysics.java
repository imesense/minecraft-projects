package org.imesense.dynamicspawncontrol.plugin.RealisticBlockPhysics_1_12_2_2_1_2;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.plugin.RealisticBlockPhysics_1_12_2_2_1_2.entity.EntityRBPFallingBlock;

import java.util.HashMap;

public class RealisticBlockPhysics
{
    public static final EntityRBPFallingBlock summonFallingBlock(World worldIn, BlockPos pos, IBlockState blockState) {
        return summonFallingBlock(worldIn, pos, blockState, 0.0D, 0.0D, 0.0D);
    }

    public static final EntityRBPFallingBlock summonFallingBlock(World worldIn, BlockPos pos, IBlockState blockState, double motionX, double motionY, double motionZ) {
        if (getBlockDefinition(worldIn, blockState) == null) {
            return null;
        } else {
            EntityRBPFallingBlock entity = new EntityRBPFallingBlock(worldIn, (double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), blockState);
            entity.motionX = motionX;
            entity.motionY = motionY;
            entity.motionZ = motionZ;
            return worldIn.spawnEntity(entity) ? entity : null;
        }
    }

    public static final BlockDefinition getBlockDefinition(World worldIn, IBlockState blockState) {
        WorldDefinition worldDef = getWorldDefinition(worldIn);
        if (worldDef == null) {
            return null;
        } else {
            BlockDefinition physicsDefMeta = (BlockDefinition)((HashMap)blockDefinitions.get(worldDef)).get(GeneralUtil.getBlockRegistryName(blockState, true));
            BlockDefinition physicsDefAll = (BlockDefinition)((HashMap)blockDefinitions.get(worldDef)).get(GeneralUtil.getBlockRegistryName(blockState, false));
            return physicsDefMeta != null ? physicsDefMeta : physicsDefAll;
        }
    }
}
