package org.imesense.dynamicspawncontrol.event;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventOvergrowingGrass
{
    private static boolean instanceExists = false;

    private static final AtomicInteger TICK_COUNTER = new AtomicInteger(0);

    public OnEventOvergrowingGrass()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    @SubscribeEvent
    public static void onWorldTick_0(TickEvent.WorldTickEvent worldTickEvent)
    {
        if (worldTickEvent.phase == TickEvent.Phase.END || worldTickEvent.world.isRemote)
        {
            return;
        }

        if (TICK_COUNTER.incrementAndGet() < 20)
        {
            return;
        }

        TICK_COUNTER.set(0);

        World world = worldTickEvent.world;

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
