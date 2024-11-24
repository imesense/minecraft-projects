package org.imesense.dynamicspawncontrol.ai.spider.auxaction;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;

/**
 *
 */
public class AuxEntityAISpiderAvoidLight extends EntityAIBase
{
    /**
     *
     */
    private int delay;

    /**
     *
     */
    private final EntityAIBase ORIGINAL_TASK;

    /**
     *
     * @param entity
     * @param originalTask
     * @param delay
     */
    public AuxEntityAISpiderAvoidLight(EntityCreature entity, EntityAIBase originalTask, int delay)
    {
        this.ORIGINAL_TASK = originalTask;
        this.delay = delay;
    }

    /**
     *
     * @return
     */
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

    /**
     *
     * @return
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        return ORIGINAL_TASK.shouldContinueExecuting();
    }

    /**
     *
     */
    @Override
    public void startExecuting()
    {
        ORIGINAL_TASK.startExecuting();
    }

    /**
     *
     */
    @Override
    public void updateTask()
    {
        ORIGINAL_TASK.updateTask();
    }
}
