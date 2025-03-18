package org.imesense.dynamicspawncontrol.core.plugin.mod.webslinger_1_12_2_2_2_4.webbing;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.pluginconfig.webslinger.PluginWebslingerConfig;

public final class AIWebbingAttack extends EntityAIBase
{
    private Double attackTimer;

    private final EntityLiving entityLiving;

    public AIWebbingAttack(EntityLiving entityLiving)
    {
        this.entityLiving = entityLiving;
    }

    @Override
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.entityLiving != null ?
                this.entityLiving.getAttackTarget() : null;

        return entitylivingbase != null &&
                entitylivingbase.getDistanceSq(this.entityLiving) >= 4.00;
    }

    @Override
    public void startExecuting()
    {
        this.attackTimer = 0.00;
    }

    @Override
    public void resetTask()
    {
        super.resetTask();
    }

    @Override
    public void updateTask()
    {
        EntityLivingBase entitylivingbase =
                this.entityLiving != null ? this.entityLiving.getAttackTarget() : null;

        if (this.entityLiving != null && entitylivingbase != null &&
                entitylivingbase.getDistanceSq(this.entityLiving) < 256.00 &&
                    this.entityLiving.canEntityBeSeen(entitylivingbase))
        {
            World world = this.entityLiving.world;

            ++this.attackTimer;

            if (this.attackTimer >= PluginWebslingerConfig.getInstance(PluginWebslingerConfig.class).getSlingCoolDown())
            {
                EntityWebbing.sling(world, this.entityLiving);

                double coolDown =
                        PluginWebslingerConfig.getInstance(PluginWebslingerConfig.class).getSlingCoolDown() +
                                (PluginWebslingerConfig.getInstance(PluginWebslingerConfig.class).getSlingCoolDown() *
                                        world.rand.nextDouble()) * PluginWebslingerConfig.getInstance(PluginWebslingerConfig.class).getSlingVariance();

                this.attackTimer = this.attackTimer - coolDown;
            }
        }
        else if (this.attackTimer > 0.00)
        {
            --this.attackTimer;
        }
    }
}
