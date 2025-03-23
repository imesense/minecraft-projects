package org.imesense.dynamicspawncontrol.core.event.rendergame;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.eventdescriptions.ComplexityBiomes;
import org.imesense.dynamicspawncontrol.eventdescriptions.RenderFPS;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventRenderGameOverlayEventText extends BaseOnEventInstance
{
    public OnEventRenderGameOverlayEventText()
    {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnRenderGameOverlayEventText_LOW(RenderGameOverlayEvent.Text event)
    {
        ComplexityBiomes.getInstance().renderBiomesOverlay();

        if (!UniqueField.CLIENT.gameSettings.showDebugInfo)
        {
            RenderFPS.getInstance().renderFpsOverlay();
        }
    }
}
