package org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.webbing;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
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
     * @param entityLiving
     */
    public AIWebbingAttack(EntityLiving entityLiving)
    {
        this.parentEntity = entityLiving;
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
                entitylivingbase.getDistanceSq(this.parentEntity) >= 4.00;
    }

    /**
     *
     */
    @Override
    public void startExecuting()
    {
        this.attackTimer = 0.00;
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
                entitylivingbase.getDistanceSq(this.parentEntity) < 256.00 &&
                    this.parentEntity.canEntityBeSeen(entitylivingbase))
        {
            World world = this.parentEntity.world;

            ++this.attackTimer;

            if (this.attackTimer >= DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getSlingCoolDown())
            {
                EntityWebbing.sling(world, this.parentEntity);

                double coolDown =
                        DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getSlingCoolDown() +
                                (DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getSlingCoolDown() *
                                        world.rand.nextDouble()) * DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getSlingVariance();

                this.attackTimer = this.attackTimer - coolDown;
            }
        }
        else if (this.attackTimer > 0.00)
        {
            --this.attackTimer;
        }
    }
}
