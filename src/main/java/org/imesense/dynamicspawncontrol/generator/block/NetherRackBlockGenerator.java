package org.imesense.dynamicspawncontrol.generator.block;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.config.blockworldgenerator.BlockWorldGeneratorData;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.Objects;
import java.util.Random;

@InitLog
public final class NetherRackBlockGenerator implements IWorldGenerator
{
    private final WorldGenerator CLASS_NETHER_RACK_GENERATOR;

    public NetherRackBlockGenerator()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }

        CLASS_NETHER_RACK_GENERATOR = new WorldGenMinable(
                Objects.requireNonNull(Block.getBlockFromName("netherrack")).getDefaultState(), 5);
    }

    private void run(WorldGenerator worldGenerator,
                     World world, Random random,
                     int chunkX, int chunkZ, int chance,
                     int minHeight, int maxHeight)
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

    @Override
    public void generate(Random random, int chunkX, int chunkZ,
                         World world, IChunkGenerator iChunkGenerator, IChunkProvider iChunkProvider)
    {
        switch (world.provider.getDimension())
        {
            case 0:
                this.run(
                        CLASS_NETHER_RACK_GENERATOR, world, random, chunkX, chunkZ,
                        BlockWorldGeneratorData.NETHER_RACK.getChanceSpawn(),
                        BlockWorldGeneratorData.NETHER_RACK.getMinHeight(),
                        BlockWorldGeneratorData.NETHER_RACK.getMaxHeight()
                );

                break;

            case 1: case -1: default: break;
        }
    }
}
