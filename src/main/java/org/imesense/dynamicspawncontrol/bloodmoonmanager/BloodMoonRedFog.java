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
        float factor = ClientBloodmoonHandler.BLOODMOON_FOG_FACTOR;

        float redFactor = 0.6F * factor;

        event.setRed(event.getRed() * (1 - redFactor) + redFactor);
        event.setGreen(event.getGreen() * (1 - redFactor));
        event.setBlue(event.getBlue() * (1 - redFactor));
    }

    @SubscribeEvent
    public void onRenderFog(EntityViewRenderEvent.RenderFogEvent event)
    {
        float factor = ClientBloodmoonHandler.BLOODMOON_FOG_FACTOR;

        float pulse = (float)Math.sin(net.minecraft.client.Minecraft.getMinecraft().world.getTotalWorldTime() * 0.01f) * 0.02f;

        factor += pulse;
        factor = Math.max(0.0f, Math.min(1.0f, factor));

        float farPlane = event.getFarPlaneDistance();

        float start = farPlane * (0.25f - 0.22f * factor);
        float end   = farPlane * (0.45f - 0.40f * factor);

        GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
        GlStateManager.setFogStart(start);
        GlStateManager.setFogEnd(end);
    }
}
