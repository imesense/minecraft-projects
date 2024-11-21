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
        this.setMutexBits(1); //-' Только движение
    }

    @Override
    public boolean shouldExecute()
    {
        if (spider.getAttackTarget() != null)
        {
            return false; //-' Не пугаемся, если есть цель
        }

        World world = spider.world;
        BlockPos pos = spider.getPosition();

        // Проверяем освещение
        if (pos.getY() >= 50 || world.getLight(pos) <= lightThreshold)
        {
            Log.writeDataToLogFile(0, "shouldExecute == false");
            return false; //-' Условия не выполнены
        }

        //-' Ищем менее освещённое место
        this.targetPosition = findDarkerSpot(pos, world);
        return true; //-' Всегда пытаемся что-то сделать
    }

    /*
    @Override
    public boolean shouldExecute()
    {
        if (spider.getAttackTarget() != null)
        {
            return false;
        }

        World world = spider.world;
        BlockPos pos = spider.getPosition();

        // Проверяем, готов ли уровень света
        if (!world.isAreaLoaded(pos, 1))
        {
            return false; // Область ещё не загружена
        }

        if (pos.getY() >= 50 || world.getLight(pos) <= lightThreshold)
        {
            return false;
        }

        this.targetPosition = findDarkerSpot(pos, world);
        return true;
    }
     */

    @Override
    public boolean shouldContinueExecuting()
    {
        if (spider.getAttackTarget() != null)
        {
            return false; //-' Прекращаем, если появилась цель
        }

        World world = spider.world;
        BlockPos pos = spider.getPosition();

        //-' Проверяем текущий свет
        return pos.getY() < 50 && world.getLight(pos) > lightThreshold;
    }

    @Override
    public void startExecuting()
    {
        if (this.targetPosition != null)
        {
            Log.writeDataToLogFile(0, "0");
            moveAwayFromLight();
        }
        else
        {
            Log.writeDataToLogFile(0, "1");
            moveRandomly(); //-' Если нет цели, двигаемся случайно
        }
    }

    private void moveAwayFromLight()
    {
        if (this.targetPosition != null)
        {
            spider.getNavigator().tryMoveToXYZ(
                    this.targetPosition.getX(),
                    this.targetPosition.getY(),
                    this.targetPosition.getZ(),
                    speed
            );
        }
    }

    private void moveRandomly()
    {
        double randomX = spider.posX + (spider.getRNG().nextDouble() - 0.5) * 10;
        double randomZ = spider.posZ + (spider.getRNG().nextDouble() - 0.5) * 10;
        spider.getNavigator().tryMoveToXYZ(randomX, spider.posY, randomZ, speed);
    }

    private BlockPos findDarkerSpot(BlockPos pos, World world)
    {
        BlockPos darkerSpot = null;
        int lowestLight = Integer.MAX_VALUE;

        // Проверяем более широкий радиус
        for (int dx = -15; dx <= 15; dx++)
        {
            for (int dz = -15; dz <= 15; dz++)
            {
                BlockPos newPos = pos.add(dx, 0, dz);
                int lightLevel = world.getLight(newPos);

                // Ищем место с минимальным светом
                if (lightLevel < lowestLight && world.isAirBlock(newPos))
                {
                    lowestLight = lightLevel;
                    darkerSpot = newPos;
                }
            }
        }

        return darkerSpot;
    }
}

