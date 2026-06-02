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
import org.imesense.dynamicspawncontrol.core.taskmanager.Task;
import org.imesense.dynamicspawncontrol.core.taskmanager.TaskManager;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@InitLog
@TODO(value = "Fix 'scheduleGrassChecks' and 'processGrowthTask' for Nether World in 0.2 ver", showOnce = false, priority = TODO.TodoPriority.HIGH)
public final class OvergrowingGrass
{
    private static volatile OvergrowingGrass _INSTANCE;
    private static final AtomicInteger TICK_COUNTER = new AtomicInteger(0);
    private final TaskManager taskManager;

    public static OvergrowingGrass getInstance()
    {
        return CodeGeneric.getInstance(OvergrowingGrass.class);
    }

    public OvergrowingGrass()
    {
        this.taskManager = TaskManager.getInstance();

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

                processGrowthTaskAsync(world, x, z, random.nextLong());
            }
        }
    }

    private void processGrowthTaskAsync(World world, int x, int z, long randomSeed)
    {
        Task<Void> growthTask = new Task<Void>("GrassGrowth", TaskManager.TaskPriority.LOW)
        {
            @Override
            public Void execute() throws Exception
            {
                if (world.provider.getDimension() != 0)
                {
                    return null;
                }

                int y = getHeightSafely(world, x, z) - 1;

                if (y < 0)
                {
                    return null;
                }

                BlockPos pos = new BlockPos(x, y, z);

                CompletableFuture<Void> future = new CompletableFuture<>();

                world.getMinecraftServer().addScheduledTask(() ->
                {
                    try
                    {
                        IBlockState state = world.getBlockState(pos);
                        if (state.getBlock() != Blocks.GRASS)
                        {
                            future.complete(null);
                            return;
                        }

                        BlockPos abovePos = pos.up();
                        IBlockState aboveState = world.getBlockState(abovePos);

                        GrowthType growthType = determineGrowthType(world, abovePos, aboveState, randomSeed);
                        if (growthType != GrowthType.NONE)
                        {
                            applyGrowth(world, pos, abovePos, growthType);
                        }

                        future.complete(null);
                    }
                    catch (Exception exception)
                    {
                        future.completeExceptionally(exception);
                    }
                });

                future.get();
                return null;
            }
        };

        taskManager.submitTask(growthTask).exceptionally(throwable ->
        {
            System.err.println("[OvergrowingGrass] Error processing growth task: " + throwable.getMessage());
            return null;
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

    private enum GrowthType
    {
        NONE, SINGLE_GRASS, DOUBLE_GRASS
    }
}
