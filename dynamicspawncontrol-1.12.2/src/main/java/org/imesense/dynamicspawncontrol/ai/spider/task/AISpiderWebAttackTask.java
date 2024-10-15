package org.imesense.dynamicspawncontrol.ai.spider.task;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.ai.spider.utils.attackweb.EntityThrowableWeb;
import org.imesense.dynamicspawncontrol.technical.config.spiderattackweb.DataSpiderAttackWeb;

public class AISpiderWebAttackTask extends EntityAIBase
{
    private int attackTimer;
    private final EntityLiving parentEntity;

    public AISpiderWebAttackTask(EntityLiving entity)
    {
        this.parentEntity = entity;
    }

    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.parentEntity != null ? this.parentEntity.getAttackTarget() : null;
        return entitylivingbase != null ? entitylivingbase.getDistanceSq(this.parentEntity) >= 4.0D : false;
    }

    public void startExecuting()
    {
        this.attackTimer = 0;
    }

    public void resetTask()
    {
        super.resetTask();
    }

    public void updateTask()
    {
        EntityLivingBase entitylivingbase = this.parentEntity != null ? this.parentEntity.getAttackTarget() : null;

        if (this.parentEntity != null && entitylivingbase != null && entitylivingbase.getDistanceSq(this.parentEntity) < 256.0D && this.parentEntity.canEntityBeSeen(entitylivingbase))
        {
            World world = this.parentEntity.world;

            ++this.attackTimer;

            if (this.attackTimer >= DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getSlingCooldown())
            {
                EntityThrowableWeb.sling(world, this.parentEntity);

                double cooldown =
                        (double)DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getSlingCooldown() +
                                (double)((float)DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getSlingCooldown() *
                                        world.rand.nextFloat()) * DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getSlingVariance();

                this.attackTimer = (int)((double)this.attackTimer - cooldown);
            }
        }
        else if (this.attackTimer > 0)
        {
            --this.attackTimer;
        }
    }
}
