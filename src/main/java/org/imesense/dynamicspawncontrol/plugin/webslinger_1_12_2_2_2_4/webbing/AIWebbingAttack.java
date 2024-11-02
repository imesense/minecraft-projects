package org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.webbing;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.config.DataWebSlinger;

/**
 *
 */
public final class AIWebbingAttack extends EntityAIBase
{
    /**
     *
     */
    private Double attackTimer;

    /**
     *
     */
    private final EntityLiving parentEntity;

    /**
     *
     * @param entity
     */
    public AIWebbingAttack(EntityLiving entity)
    {
        CodeGenericUtil.printInitClassToLog(this.getClass());

        this.parentEntity = entity;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.parentEntity != null ?
                this.parentEntity.getAttackTarget() : null;

        return entitylivingbase != null &&
                entitylivingbase.getDistanceSq(this.parentEntity) >= 4.0;
    }

    /**
     *
     */
    @Override
    public void startExecuting()
    {
        this.attackTimer = 0.0;
    }

    /**
     *
     */
    @Override
    public void resetTask()
    {
        super.resetTask();
    }

    /**
     *
     */
    @Override
    public void updateTask()
    {
        EntityLivingBase entitylivingbase =
                this.parentEntity != null ? this.parentEntity.getAttackTarget() : null;

        if (this.parentEntity != null && entitylivingbase != null &&
                entitylivingbase.getDistanceSq(this.parentEntity) < 256.0 &&
                    this.parentEntity.canEntityBeSeen(entitylivingbase))
        {
            World world = this.parentEntity.world;

            ++this.attackTimer;

            if (this.attackTimer >= DataWebSlinger.ConfigDataSpiderAttackWeb.instance.getSlingCoolDown())
            {
                EntityWebbing.sling(world, this.parentEntity);

                double coolDown =
                        DataWebSlinger.ConfigDataSpiderAttackWeb.instance.getSlingCoolDown() +
                                (DataWebSlinger.ConfigDataSpiderAttackWeb.instance.getSlingCoolDown() *
                                        world.rand.nextDouble()) * DataWebSlinger.ConfigDataSpiderAttackWeb.instance.getSlingVariance();

                this.attackTimer = this.attackTimer - coolDown;
            }
        }
        else if (this.attackTimer > 0.0)
        {
            --this.attackTimer;
        }
    }
}
