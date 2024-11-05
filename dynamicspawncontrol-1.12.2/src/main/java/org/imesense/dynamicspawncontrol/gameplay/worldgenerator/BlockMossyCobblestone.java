package org.imesense.dynamicspawncontrol.gameplay.worldgenerator;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.config.data.BlockWorldGeneratorData;

import java.util.Objects;
import java.util.Random;

/**
 *
 */
public final class BlockMossyCobblestone implements IWorldGenerator
{
    /**
     *
     */
    private final WorldGenerator CLASS_MOSSY_COBBLESTONE_GENERATOR;

    /**
     *
     */
    public BlockMossyCobblestone()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        CLASS_MOSSY_COBBLESTONE_GENERATOR = new WorldGenMinable(
                Objects.requireNonNull(Block.getBlockFromName("mossy_cobblestone")).getDefaultState(), 5);
    }

    /**
     *
     * @param worldGenerator
     * @param world
     * @param random
     * @param chunkX
     * @param chunkZ
     * @param chance
     * @param minHeight
     * @param maxHeight
     */
    private void run(WorldGenerator worldGenerator, World world, Random random, int chunkX, int chunkZ, int chance, int minHeight, int maxHeight)
    {
        int heightDiff = maxHeight - minHeight + 1;

        for (int i = 0; i < chance; i++)
        {
            int x = chunkX * 16 + random.nextInt(16);
            int y = minHeight + random.nextInt(heightDiff);
            int z = chunkZ * 16 + random.nextInt(16);

            x += random.nextInt(10) - 6 / 2;
            z += random.nextInt(10) - 6 / 2;

            worldGenerator.generate(world, random, new BlockPos(x, y, z));
        }
    }

    /**
     *
     * @param random
     * @param chunkX
     * @param chunkZ
     * @param world
     * @param iChunkGenerator
     * @param iChunkProvider
     */
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator iChunkGenerator, IChunkProvider iChunkProvider)
    {
        switch (world.provider.getDimension())
        {
            case 0:
            {
                run(
                        CLASS_MOSSY_COBBLESTONE_GENERATOR, world, random, chunkX, chunkZ,
                        BlockWorldGeneratorData.InfoDataBlockMossyCobblestone.Instance.getChanceSpawn(),
                        BlockWorldGeneratorData.InfoDataBlockMossyCobblestone.Instance.getMinHeight(),
                        BlockWorldGeneratorData.InfoDataBlockMossyCobblestone.Instance.getMaxHeight()
                );

                break;
            }

            case 1: case -1: default: break;
        }
    }
}
