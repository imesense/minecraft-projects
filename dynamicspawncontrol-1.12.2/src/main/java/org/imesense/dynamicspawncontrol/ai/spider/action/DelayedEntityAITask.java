package org.imesense.dynamicspawncontrol.ai.spider.action;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;

/**
 * Почти оптимально работает логика боязни свет для паука под землей
 */
public class DelayedEntityAITask extends EntityAIBase
{
    private final EntityAIBase originalTask;
    private int delay;

    public DelayedEntityAITask(EntityCreature entity, EntityAIBase originalTask, int delay)
    {
        this.originalTask = originalTask;
        this.delay = delay;
    }

    @Override
    public boolean shouldExecute()
    {
        if (delay > 0) {
            delay--;
            return false;
        }
        return originalTask.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return originalTask.shouldContinueExecuting();
    }

    @Override
    public void startExecuting()
    {
        originalTask.startExecuting();
    }

    @Override
    public void updateTask()
    {
        originalTask.updateTask();
    }
}
