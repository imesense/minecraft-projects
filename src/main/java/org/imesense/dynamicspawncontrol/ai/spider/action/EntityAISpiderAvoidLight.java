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
    private final EntityCreature spider;
    private final double speed;
    private final int lightThreshold;
    private BlockPos targetPosition;

    public EntityAISpiderAvoidLight(EntityCreature spider, double speed, int lightThreshold)
    {
        this.spider = spider;
        this.speed = speed;
        this.lightThreshold = lightThreshold;
        this.setMutexBits(1);

        Log.writeDataToLogFile(0, "EntityAISpiderAvoidLight initialized with speed: " + speed + " and lightThreshold: " + lightThreshold);
    }

    @Override
    public boolean shouldExecute()
    {
        Log.writeDataToLogFile(1, "shouldExecute called");
        if (spider.getAttackTarget() != null)
        {
            Log.writeDataToLogFile(1, "shouldExecute: Spider has an attack target, execution aborted.");
            return false;
        }

        World world = spider.world;
        BlockPos pos = spider.getPosition();

        if (pos.getY() >= 50 || world.getLight(pos) <= lightThreshold)
        {
            Log.writeDataToLogFile(1, "shouldExecute: Position too high or light level acceptable. Execution aborted.");
            return false;
        }

        this.targetPosition = findDarkerSpot(pos, world);
        Log.writeDataToLogFile(1, "shouldExecute: Darker spot found at " + targetPosition);
        return true;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        Log.writeDataToLogFile(2, "shouldContinueExecuting called");
        if (spider.getAttackTarget() != null)
        {
            Log.writeDataToLogFile(2, "shouldContinueExecuting: Spider has an attack target, execution aborted.");
            return false;
        }

        World world = spider.world;
        BlockPos pos = spider.getPosition();

        boolean continueExecuting = pos.getY() < 50 && world.getLight(pos) > lightThreshold;
        Log.writeDataToLogFile(2, "shouldContinueExecuting: Continue executing: " + continueExecuting);
        return continueExecuting;
    }

    @Override
    public void startExecuting()
    {
        Log.writeDataToLogFile(3, "startExecuting called");
        if (this.targetPosition != null)
        {
            Log.writeDataToLogFile(3, "startExecuting: Moving towards " + targetPosition);
            moveAwayFromLight();
        }
        else
        {
            Log.writeDataToLogFile(3, "startExecuting: No target position found.");
        }
    }

    private void moveAwayFromLight()
    {
        Log.writeDataToLogFile(4, "moveAwayFromLight called");
        if (this.targetPosition != null)
        {
            spider.getNavigator().tryMoveToXYZ(
                    this.targetPosition.getX(),
                    this.targetPosition.getY(),
                    this.targetPosition.getZ(),
                    speed
            );
            Log.writeDataToLogFile(4, "moveAwayFromLight: Attempting to move to " + targetPosition);
        }
    }

    private BlockPos findDarkerSpot(BlockPos pos, World world)
    {
        Log.writeDataToLogFile(5, "findDarkerSpot called");
        BlockPos darkerSpot = null;

        int lowestLight = Integer.MAX_VALUE;

        for (int dy = -1; dy <= 1; dy++) //-' Проверка уровня выше и ниже
        {
            for (int dx = -7; dx <= 7; dx++) //-' Сокращенный радиус
            {
                for (int dz = -7; dz <= 7; dz++) //-' Сокращенный радиус
                {
                    BlockPos newPos = pos.add(dx, dy, dz);
                    int lightLevel = world.getLight(newPos);

                    if (lightLevel < lowestLight && isNavigable(newPos, world))
                    {
                        lowestLight = lightLevel;
                        darkerSpot = newPos;
                    }
                }
            }
        }

        Log.writeDataToLogFile(5, "findDarkerSpot: Darker spot found at " + darkerSpot + " with light level " + lowestLight);
        return darkerSpot;
    }

    // Проверяем, может ли паук пройти через точку
    private boolean isNavigable(BlockPos pos, World world)
    {
        return world.isAirBlock(pos) || world.getBlockState(pos).getMaterial().isReplaceable();
    }
}


