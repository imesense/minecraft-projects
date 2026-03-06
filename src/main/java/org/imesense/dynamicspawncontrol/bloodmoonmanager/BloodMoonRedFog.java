package org.imesense.dynamicspawncontrol.bloodmoonmanager;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class BloodMoonRedFog
{
    @SubscribeEvent
    public void onFogColors(EntityViewRenderEvent.FogColors event)
    {
        if (!ClientBloodmoonHandler.INSTANCE.isBloodmoonActive())
            return;

        float factor = ClientBloodmoonHandler.BLOODMOON_FOG_FACTOR;

        float redFactor = 0.6F * factor;

        event.setRed(event.getRed() * (1 - redFactor) + redFactor);
        event.setGreen(event.getGreen() * (1 - redFactor));
        event.setBlue(event.getBlue() * (1 - redFactor));
    }

    @SubscribeEvent
    public void onFogDensity(EntityViewRenderEvent.FogDensity event)
    {

    }

    @SubscribeEvent
    public void onRenderFog(EntityViewRenderEvent.RenderFogEvent event)
    {
        if (!ClientBloodmoonHandler.INSTANCE.isBloodmoonActive())
            return;

        float factor = ClientBloodmoonHandler.BLOODMOON_FOG_FACTOR;

        float start = 30.0F - (25.0F * factor);
        float end = 200.0F - (170.0F * factor);

        GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
        GlStateManager.setFogStart(start);
        GlStateManager.setFogEnd(end);
    }
}
