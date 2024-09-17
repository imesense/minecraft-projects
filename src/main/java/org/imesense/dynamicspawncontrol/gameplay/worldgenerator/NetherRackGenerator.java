package org.imesense.dynamicspawncontrol.gameplay.worldgenerator;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.imesense.dynamicspawncontrol.technical.configs.SettingsWorldGenerator;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.util.Objects;
import java.util.Random;

/**
 *
 * OldSerpskiStalker:
 * <a href="https://forum.mcmodding.ru/threads/1-12-2-generacija.21375/">...</a>
 */
public final class NetherRackGenerator implements IWorldGenerator
{
    /**
     *
     */
    private final WorldGenerator classGenerateBlockNetherRack;

    /**
     *
     * @param nameClass
     */
    public NetherRackGenerator(final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);

        classGenerateBlockNetherRack = new WorldGenMinable(
                Objects.requireNonNull(Block.getBlockFromName("netherrack")).getDefaultState(), 9);
    }

    /**
     *
     * @param gen
     * @param world
     * @param rand
     * @param chunkX
     * @param chunkZ
     * @param chance
     * @param minHeight
     * @param maxHeight
     */
    private void run(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, int chance, int minHeight, int maxHeight)
    {
        int heightDiff = maxHeight - minHeight + 1;

        for (int i = 0; i < chance; i++)
        {
            int x = chunkX * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunkZ * 16 + rand.nextInt(16);

            x += rand.nextInt(10) - 6 / 2;
            z += rand.nextInt(10) - 6 / 2;

            gen.generate(world, rand, new BlockPos(x, y, z));
        }
    }

    /**
     *
     * @param random
     * @param chunkX
     * @param chunkZ
     * @param world
     * @param chunkGenerator
     * @param chunkProvider
     */
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        switch (world.provider.getDimension())
        {
            case 0:
            {
                run(
                        classGenerateBlockNetherRack, world, random, chunkX, chunkZ,
                        SettingsWorldGenerator.BlockNetherrackChanceSpawn,
                        SettingsWorldGenerator.GetBlockNetherrackMinHeight,
                        SettingsWorldGenerator.GetBlockNetherrackMaxHeight
                );
                break;
            }

            case 1: case -1: default: break;
        }
    }
}
