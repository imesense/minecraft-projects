package org.imesense.dynamicspawncontrol.ai.spider.action;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

/**
 *
 */
public class EntityAISpiderAvoidLight extends EntityAIBase
{
    /**
     *
     */
    private final double SPEED;

    /**
     *
     */
    private BlockPos targetPosition;

    /**
     *
     */
    private final int LIGHT_THRESHOLD;

    /**
     *
     */
    private final EntityCreature SPIDER;

    /**
     *
     * @param spider
     * @param speed
     * @param lightThreshold
     */
    public EntityAISpiderAvoidLight(EntityCreature spider, double speed, int lightThreshold)
    {
        this.SPIDER = spider;
        this.SPEED = speed;
        this.LIGHT_THRESHOLD = lightThreshold;
        this.setMutexBits(1);
    }

    /**
     *
     * @return
     */
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

        return true;
    }

    /**
     *
     * @return
     */
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

    /**
     *
     */
    @Override
    public void startExecuting()
    {
        if (this.targetPosition != null)
        {
            moveAwayFromLight();
        }
    }

    /**
     *
     */
    private void moveAwayFromLight()
    {
        if (this.targetPosition != null)
        {
            SPIDER.getNavigator().tryMoveToXYZ(
                    this.targetPosition.getX(),
                    this.targetPosition.getY(),
                    this.targetPosition.getZ(),
                    SPEED
            );
        }
    }

    /**
     *
     * @param blockPos
     * @param world
     * @return
     */
    private BlockPos findDarkerSpot(BlockPos blockPos, World world)
    {
        BlockPos darkerSpot = null;

        int lowestLight = Integer.MAX_VALUE;

        for (int dy = -1; dy <= 1; dy++)
        {
            for (int dx = -7; dx <= 7; dx++)
            {
                for (int dz = -7; dz <= 7; dz++)
                {
                    BlockPos newBlockPos = blockPos.add(dx, dy, dz);
                    int lightLevel = world.getLight(newBlockPos);

                    if (lightLevel < lowestLight && isNavigable(newBlockPos, world))
                    {
                        lowestLight = lightLevel;
                        darkerSpot = newBlockPos;
                    }
                }
            }
        }

        return darkerSpot;
    }

    /**
     *
     * @param blockPos
     * @param world
     * @return
     */
    private boolean isNavigable(BlockPos blockPos, World world)
    {
        return world.isAirBlock(blockPos) || world.getBlockState(blockPos).getMaterial().isReplaceable();
    }
}


