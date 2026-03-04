package org.imesense.dynamicspawncontrol.core.event.entity;

import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.renderer.fog.BedrockVoidFog;
//import org.imesense.dynamicspawncontrol.core.plugin.mod.fogworld_1_12_1_1_0_b15_universal.handler.FogEventHandler;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventEntityViewRenderEventFogColors extends BaseOnEventInstance
{
    public OnEventEntityViewRenderEventFogColors()
    {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnEntityViewRenderEventFogColors_LOW(EntityViewRenderEvent.FogColors event)
    {
        //FogEventHandler.getInstance().handleGetFogColor(event);

        BedrockVoidFog.getInstance().handleFogVoidColor(event);
    }
}
