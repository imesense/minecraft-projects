package org.imesense.dynamicspawncontrol.ai.spider.task;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public final class SpiderAvoidLightEntityAI extends EntityAIBase
{
    private final double SPEED;
    private BlockPos targetPosition;
    private final int LIGHT_THRESHOLD;
    private final EntityCreature SPIDER;

    public SpiderAvoidLightEntityAI(EntityCreature entityCreature, double speed, int lightThreshold)
    {
        this.SPIDER = entityCreature;
        this.SPEED = speed;
        this.LIGHT_THRESHOLD = lightThreshold;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    {
        if (SPIDER.getAttackTarget() != null)
        {
            return false;
        }

        World world = SPIDER.world;
        BlockPos blockPos = SPIDER.getPosition();

        if (blockPos.getY() >= 50 || world.getLight(blockPos) <= LIGHT_THRESHOLD)
        {
            return false;
        }

        this.targetPosition = findDarkerSpot(blockPos, world);

        return this.targetPosition != null;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        if (SPIDER.getAttackTarget() != null)
        {
            return false;
        }

        World world = SPIDER.world;
        BlockPos blockPos = SPIDER.getPosition();

        return blockPos.getY() < 50 && world.getLight(blockPos) > LIGHT_THRESHOLD;
    }

    @Override
    public void startExecuting()
    {
        if (this.targetPosition != null)
        {
            BlockPos adjustedPos = this.targetPosition.add(
                    SPIDER.world.rand.nextInt(3) - 1,
                    0,
                    SPIDER.world.rand.nextInt(3) - 1
            );

            SPIDER.getNavigator().tryMoveToXYZ(
                    adjustedPos.getX(),
                    adjustedPos.getY(),
                    adjustedPos.getZ(),
                    SPEED
            );
        }
    }

    private BlockPos findDarkerSpot(BlockPos blockPos, World world)
    {
        List<BlockPos> darkSpots = new ArrayList<>();

        for (int dy = -1; dy <= 1; dy++)
        {
            for (int dx = -15; dx <= 15; dx++)
            {
                for (int dz = -15; dz <= 15; dz++)
                {
                    BlockPos newBlockPos = blockPos.add(dx, dy, dz);
                    int lightLevel = world.getLight(newBlockPos);

                    if (lightLevel < LIGHT_THRESHOLD && isNavigable(newBlockPos, world))
                    {
                        darkSpots.add(newBlockPos);
                    }
                }
            }
        }

        if (!darkSpots.isEmpty())
        {
            return darkSpots.get(world.rand.nextInt(darkSpots.size()));
        }

        return null;
    }

    private boolean isNavigable(BlockPos blockPos, World world)
    {
        return world.isAirBlock(blockPos) || world.getBlockState(blockPos).getMaterial().isReplaceable();
    }
}