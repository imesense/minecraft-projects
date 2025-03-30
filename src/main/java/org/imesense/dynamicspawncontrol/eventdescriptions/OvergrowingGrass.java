package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.threads.GrassThreadMonitor;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.jline.utils.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@InitLog
public final class OvergrowingGrass
{
    private static final int TICKS_BETWEEN_CHECKS = 20;
    private static final int CHECKS_PER_PLAYER = 5;
    private static final double GROWTH_CHANCE = 0.95;
    private static final int PLAYER_RADIUS = 8;

    private static final GrassThreadMonitor grassMonitor = GrassThreadMonitor.getInstance();

    private static final AtomicInteger TICK_COUNTER = new AtomicInteger(0);
    private static volatile OvergrowingGrass _INSTANCE;

    private static final ExecutorService WORKER = Executors.newFixedThreadPool(1, r ->
    {
        Thread t = new Thread(r, "OvergrowingGrass Worker");
        t.setDaemon(true);
        return t;
    });

    private final BlockingQueue<GrowthTask> taskQueue = new LinkedBlockingQueue<>();

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

    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END || event.world.isRemote)
        {
            return;
        }

        if (TICK_COUNTER.incrementAndGet() < TICKS_BETWEEN_CHECKS)
        {
            return;
        }

        TICK_COUNTER.set(0);
        scheduleGrassChecks(event.world);
    }

    private void scheduleGrassChecks(World world)
    {
        List<EntityPlayerMP> players = new ArrayList<>(world.getMinecraftServer().getPlayerList().getPlayers());

        for (EntityPlayerMP player : players)
        {
            BlockPos playerPos = player.getPosition();
            Random random = new Random(world.getWorldTime());

            for (int i = 0; i < CHECKS_PER_PLAYER; i++)
            {
                int x = playerPos.getX() + random.nextInt(PLAYER_RADIUS * 2) - PLAYER_RADIUS;
                int z = playerPos.getZ() + random.nextInt(PLAYER_RADIUS * 2) - PLAYER_RADIUS;
                taskQueue.offer(new GrowthTask(world, x, z, random.nextLong()));
            }
        }

        startProcessingTasks(world);
    }

    private void startProcessingTasks(World world)
    {
        grassMonitor.recordTaskStart(taskQueue.size());
        WORKER.submit(() ->
        {
            try
            {
                GrowthTask task;

                while ((task = taskQueue.poll()) != null)
                {
                    long startTime = System.currentTimeMillis();
                    processGrowthTask(task, world);
                    grassMonitor.recordTaskCompletion(System.currentTimeMillis() - startTime);
                }
            }
            catch (Exception exception)
            {
                grassMonitor.recordError();
                Log.error("Error in grass growth processing", exception);
            }
            finally
            {
                grassMonitor.recordTaskEnd();
            }
        });
    }

    private void processGrowthTask(GrowthTask task, World world)
    {
        int y = getHeightSafely(world, task.x, task.z) - 1;

        if (y < 0)
        {
            return;
        }

        BlockPos pos = new BlockPos(task.x, y, task.z);

        world.getMinecraftServer().addScheduledTask(() ->
        {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() != Blocks.GRASS) return;

            BlockPos abovePos = pos.up();
            IBlockState aboveState = world.getBlockState(abovePos);

            GrowthType growthType = determineGrowthType(world, abovePos, aboveState, task.randomSeed);
            if (growthType == GrowthType.NONE) return;

            applyGrowth(world, pos, abovePos, growthType);
        });
    }

    private int getHeightSafely(World world, int x, int z)
    {
        Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
        return chunk.getHeightValue(x & 15, z & 15);
    }

    private GrowthType determineGrowthType(World world, BlockPos abovePos, IBlockState aboveState, long randomSeed)
    {
        Random random = new Random(randomSeed ^ world.getWorldTime());

        if (aboveState.getBlock() == Blocks.TALLGRASS &&
                aboveState.getValue(BlockTallGrass.TYPE) == BlockTallGrass.EnumType.GRASS)
        {
            BlockPos upperPos = abovePos.up();

            if (world.isAirBlock(upperPos) && random.nextDouble() < GROWTH_CHANCE)
            {
                return GrowthType.DOUBLE_GRASS;
            }
        }
        else if (world.isAirBlock(abovePos) && random.nextDouble() < GROWTH_CHANCE)
        {
            return GrowthType.SINGLE_GRASS;
        }

        return GrowthType.NONE;
    }

    private void applyGrowth(World world, BlockPos groundPos, BlockPos abovePos, GrowthType type)
    {
        switch (type)
        {
            case SINGLE_GRASS:
                world.setBlockState(abovePos,
                        Blocks.TALLGRASS.getDefaultState()
                                .withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS),
                        3);
                break;

            case DOUBLE_GRASS:
                BlockPos upperPos = abovePos.up();
                world.setBlockState(abovePos,
                        Blocks.DOUBLE_PLANT.getDefaultState()
                                .withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS)
                                .withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER),
                        3);

                world.setBlockState(upperPos,
                        Blocks.DOUBLE_PLANT.getDefaultState()
                                .withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS)
                                .withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER),
                        3);
                break;
        }
    }

    private static class GrowthTask
    {
        final World world;
        final int x, z;
        final long randomSeed;

        protected GrowthTask(World world, int x, int z, long randomSeed)
        {
            this.world = world;
            this.x = x;
            this.z = z;
            this.randomSeed = randomSeed;
        }
    }

    private enum GrowthType
    {
        NONE, SINGLE_GRASS, DOUBLE_GRASS
    }
}
