package org.imesense.dynamicspawncontrol.core.event.living;

import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventLivingHurt;
import org.imesense.dynamicspawncontrol.eventdescriptions.UpdateTorch;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventLivingHurtEvent extends BaseOnEventInstance
{
    public OnEventLivingHurtEvent()
    {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnLivingHurtEvent(LivingHurtEvent event)
    {
        UpdateTorch.getInstance().handleEntityHit(event);

        if (!event.getEntity().world.isRemote)
        {
            OnEventLivingHurt.getInstance().handleUpdateLivingHurtEvent(event);
        }
    }
}
