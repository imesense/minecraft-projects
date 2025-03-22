package org.imesense.dynamicspawncontrol.core.event.rendergame;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventWorldCache;
import org.imesense.dynamicspawncontrol.statistics.DSCInlineDebugStats;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventRenderGameOverlayEventPost extends BaseOnEventInstance
{
    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnRenderGameOverlayEventPost_LOW(RenderGameOverlayEvent.Post event)
    {
        DSCInlineDebugStats.getInstance().handleRenderDebugInfo(event);
    }
}
