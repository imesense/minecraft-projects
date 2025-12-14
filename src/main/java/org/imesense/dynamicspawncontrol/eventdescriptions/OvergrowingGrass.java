package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.config.synchronization.SynchronizationConfig;
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
@TODO(value = "Fix 'scheduleGrassChecks' for Nether World in 0.2 ver", showOnce = false, priority = TODO.TodoPriority.HIGH)
public final class OvergrowingGrass
{
    private static volatile OvergrowingGrass _INSTANCE;
    private static final GrassThreadMonitor grassMonitor = GrassThreadMonitor.getInstance();

    private static final AtomicInteger TICK_COUNTER = new AtomicInteger(0);

    private static final ExecutorService WORKER = Executors.newFixedThreadPool(1, r ->
    {
        Thread thread = new Thread(r, "OvergrowingGrass Worker");
        thread.setDaemon(true);
        return thread;
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

    public void handleWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END || event.world.isRemote)
        {
            return;
        }

        if (TICK_COUNTER.incrementAndGet() < SynchronizationConfig.getInstance(SynchronizationConfig.class).getTicksBetweenChecks())
        {
            return;
        }

        TICK_COUNTER.set(0);
        scheduleGrassChecks(event.world);
    }

    private void scheduleGrassChecks(World world)
    {
        if (world.provider.getDimension() != 0) // 0 is the overworld dimension
        {
            return; // Don't schedule any growth tasks in the Nether, End, etc.
        }

        List<EntityPlayerMP> players = new ArrayList<>(world.getMinecraftServer().getPlayerList().getPlayers());

        for (EntityPlayerMP player : players)
        {
            BlockPos playerPos = player.getPosition();
            Random random = new Random(world.getWorldTime());

            for (int i = 0; i < SynchronizationConfig.getInstance(SynchronizationConfig.class).getChecksPerPlayer(); i++)
            {
                int x = playerPos.getX() + random.nextInt(SynchronizationConfig.getInstance(
                        SynchronizationConfig.class).getPlayerRadius() * 2) -
                        SynchronizationConfig.getInstance(SynchronizationConfig.class).getPlayerRadius();

                int z = playerPos.getZ() + random.nextInt(SynchronizationConfig.getInstance(
                        SynchronizationConfig.class).getPlayerRadius() * 2) -
                        SynchronizationConfig.getInstance(SynchronizationConfig.class).getPlayerRadius();

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

    private void processGrowthTask(GrowthTask growthTask, World world)
    {
        int y = getHeightSafely(world, growthTask.X, growthTask.Z) - 1;

        if (y < 0)
        {
            return;
        }

        BlockPos pos = new BlockPos(growthTask.X, y, growthTask.Z);

        world.getMinecraftServer().addScheduledTask(() ->
        {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() != Blocks.GRASS) return;

            BlockPos abovePos = pos.up();
            IBlockState aboveState = world.getBlockState(abovePos);

            GrowthType growthType = determineGrowthType(world, abovePos, aboveState, growthTask.randomSeed);
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

            if (world.isAirBlock(upperPos) && random.nextDouble() <
                    SynchronizationConfig.getInstance(SynchronizationConfig.class).getGrowthChance())
            {
                return GrowthType.DOUBLE_GRASS;
            }
        }
        else if (world.isAirBlock(abovePos) && random.nextDouble() <
                SynchronizationConfig.getInstance(SynchronizationConfig.class).getGrowthChance())
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
        final World WORLD;
        final int X, Z;
        final long randomSeed;

        protected GrowthTask(World world, int x, int z, long randomSeed)
        {
            this.WORLD = world;
            this.X = x;
            this.Z = z;
            this.randomSeed = randomSeed;
        }
    }

    private enum GrowthType
    {
        NONE, SINGLE_GRASS, DOUBLE_GRASS
    }
}
