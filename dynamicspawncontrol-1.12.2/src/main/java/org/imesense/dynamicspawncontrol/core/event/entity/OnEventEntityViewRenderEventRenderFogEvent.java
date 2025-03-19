package org.imesense.dynamicspawncontrol.core.event.entity;

import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.plugin.mod.fogworld_1_12_1_1_0_b15_universal.handler.FogEventHandler;
import org.imesense.dynamicspawncontrol.core.plugin.mod.void_fog_1_12_1_1_2.FogEvent;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventEntityViewRenderEventRenderFogEvent extends BaseOnEventInstance
{
    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnEntityViewRenderEventRenderFogEvent(EntityViewRenderEvent.RenderFogEvent event)
    {
        FogEventHandler.getInstance().handleRenderFog(event);

        FogEvent.getInstance().handleFogVoidRender(event);
    }
}
