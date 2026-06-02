package org.imesense.dynamicspawncontrol.core.event.entity;

import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.renderer.fog.BedrockVoidFog;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventEntityViewRenderEventRenderFogEvent extends BaseOnEventInstance
{
    public OnEventEntityViewRenderEventRenderFogEvent()
    {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnEntityViewRenderEventRenderFogEvent_LOW(EntityViewRenderEvent.RenderFogEvent event)
    {
        BedrockVoidFog.getInstance().handleFogVoidRender(event);
    }
}
