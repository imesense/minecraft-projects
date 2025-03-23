package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.concurrent.atomic.AtomicInteger;

@InitLog
public final class OvergrowingGrass
{
    public static final AtomicInteger TICK_COUNTER = new AtomicInteger(0);

    private static volatile OvergrowingGrass _INSTANCE;

    public static OvergrowingGrass getInstance()
    {
        return CodeGeneric.getInstance(OvergrowingGrass.class);
    }

    public OvergrowingGrass()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void handleWorldTick(World world)
    {
        for (EntityPlayer player : world.playerEntities)
        {
            BlockPos playerPos = player.getPosition();

            int x = UniqueField.RANDOM.nextInt(16) + playerPos.getX() - 8;
            int z = UniqueField.RANDOM.nextInt(16) + playerPos.getZ() - 8;

            int y = world.getHeight(x, z) - 1;

            BlockPos groundPos = new BlockPos(x, y, z);
            Block block = world.getBlockState(groundPos).getBlock();

            if (block == Blocks.GRASS)
            {
                BlockPos abovePos = groundPos.up();
                IBlockState stateAbove = world.getBlockState(abovePos);

                if (stateAbove.getBlock() == Blocks.TALLGRASS)
                {
                    if (stateAbove.getValue(BlockTallGrass.TYPE) == BlockTallGrass.EnumType.GRASS)
                    {
                        BlockPos upperPos = abovePos.up();

                        if (world.isAirBlock(upperPos) && UniqueField.RANDOM.nextInt(100) < 95)
                        {
                            IBlockState doubleTallGrassLower = Blocks.DOUBLE_PLANT.getDefaultState()
                                    .withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS)
                                    .withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER);

                            IBlockState doubleTallGrassUpper = Blocks.DOUBLE_PLANT.getDefaultState()
                                    .withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS)
                                    .withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER);

                            world.setBlockState(abovePos, doubleTallGrassLower, 3);
                            world.setBlockState(upperPos, doubleTallGrassUpper, 3);
                        }
                    }
                }
                else if (world.isAirBlock(abovePos))
                {
                    if (UniqueField.RANDOM.nextInt(100) < 95)
                    {
                        IBlockState smallGrassState = Blocks.TALLGRASS.getDefaultState()
                                .withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);

                        world.setBlockState(abovePos, smallGrassState, 3);
                    }
                }
            }
        }
    }
}
