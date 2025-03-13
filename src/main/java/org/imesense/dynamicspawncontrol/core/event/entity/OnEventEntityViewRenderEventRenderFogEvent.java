package org.imesense.dynamicspawncontrol.core.event.entity;

import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.handler.FogEventHandler;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public class OnEventEntityViewRenderEventRenderFogEvent extends BaseOnEventInstance
{
    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnEntityViewRenderEventRenderFogEvent(EntityViewRenderEvent.RenderFogEvent event)
    {
        //FogEventHandler.getInstance().handleRenderFog(event);
    }
}
