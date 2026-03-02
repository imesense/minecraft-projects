package org.imesense.dynamicspawncontrol.core.event.living;

import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventDropExperience;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventLivingExperienceDropEvent extends BaseOnEventInstance
{
    public OnEventLivingExperienceDropEvent()
    {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnLivingExperienceDropEvent(LivingExperienceDropEvent event)
    {
        if (!event.getEntity().world.isRemote)
        {
            OnEventDropExperience.getInstance().handleUpdateLivingExperienceDrop(event);
        }
    }
}
