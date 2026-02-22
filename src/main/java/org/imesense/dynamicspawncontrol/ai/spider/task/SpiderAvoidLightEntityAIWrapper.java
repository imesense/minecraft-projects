package org.imesense.dynamicspawncontrol.ai.spider.task;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;

public final class SpiderAvoidLightEntityAIWrapper extends EntityAIBase
{
    private int delay;
    private final EntityAIBase ORIGINAL_TASK;

    public SpiderAvoidLightEntityAIWrapper(EntityCreature entityCreature, EntityAIBase entityAIBase, int delay)
    {
        this.ORIGINAL_TASK = entityAIBase;
        this.delay = delay;
    }

    @Override
    public boolean shouldExecute()
    {
        if (delay > 0)
        {
            delay--;
            return false;
        }

        return ORIGINAL_TASK.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return ORIGINAL_TASK.shouldContinueExecuting();
    }

    @Override
    public void startExecuting()
    {
        ORIGINAL_TASK.startExecuting();
    }

    @Override
    public void updateTask()
    {
        ORIGINAL_TASK.updateTask();
    }
}
