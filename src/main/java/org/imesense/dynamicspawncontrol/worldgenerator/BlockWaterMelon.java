package org.imesense.dynamicspawncontrol.worldgenerator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.Random;

@InitLog
public final class BlockWaterMelon implements IWorldGenerator
{
    public BlockWaterMelon()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ,
                         World world, IChunkGenerator iChunkGenerator, IChunkProvider iChunkProvider)
    {
        if (world.provider.getDimension() == 0)
        {
            generateMelonsNearWater(world, random, chunkX * 16, chunkZ * 16);
        }
    }

    private void generateMelonsNearWater(World world, Random random, int x, int z)
    {
        int attempts = 1 + random.nextInt(5);

        for (int i = 0; i < attempts; i++)
        {
            int posX = x + random.nextInt(16);
            int posZ = z + random.nextInt(16);
            int posY = world.getHeight(posX, posZ);

            BlockPos pos = new BlockPos(posX, posY, posZ);
            BlockPos belowPos = pos.down();

            if (world.getBlockState(belowPos).getBlock() == Blocks.GRASS && isNextToWater(world, belowPos))
            {
                if (random.nextFloat() < 0.08f)
                {
                    IBlockState melon = Blocks.MELON_BLOCK.getDefaultState();
                    world.setBlockState(belowPos.up(), melon);
                }
            }
        }
    }

    private boolean isNextToWater(World world, BlockPos blockPos)
    {
        return world.getBlockState(blockPos.north()).getBlock() == Blocks.WATER ||
                world.getBlockState(blockPos.south()).getBlock() == Blocks.WATER ||
                world.getBlockState(blockPos.east()).getBlock() == Blocks.WATER ||
                world.getBlockState(blockPos.west()).getBlock() == Blocks.WATER;
    }
}
