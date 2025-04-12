package org.imesense.dynamicspawncontrol.core.event.living;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.eventdescriptions.DropHeadMob;
import org.imesense.dynamicspawncontrol.eventdescriptions.NickNameEntity;
import org.imesense.dynamicspawncontrol.eventdescriptions.UpdateFire;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventLivingDeathEvent extends BaseOnEventInstance
{
    public OnEventLivingDeathEvent()
    {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnLivingDeathEvent(LivingDeathEvent event)
    {
        DropHeadMob.getInstance().handleEntityDeath(event);

        NickNameEntity.getInstance().handleZombieDeath(event);
        NickNameEntity.getInstance().handleVillagerDeath(event);

        UpdateFire.getInstance().handleLivingDeath(event);
    }
}
