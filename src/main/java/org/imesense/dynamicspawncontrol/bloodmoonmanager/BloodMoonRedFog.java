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

        float redTarget = 0.75F;
        float greenTarget = 0.05F;
        float blueTarget = 0.05F;

        event.setRed(event.getRed() * (1 - factor) + redTarget * factor);
        event.setGreen(event.getGreen() * (1 - factor) + greenTarget * factor);
        event.setBlue(event.getBlue() * (1 - factor) + blueTarget * factor);
    }

    @SubscribeEvent
    public void onRenderFog(EntityViewRenderEvent.RenderFogEvent event)
    {
        float factor = ClientBloodmoonHandler.BLOODMOON_FOG_FACTOR;

        float farPlane = event.getFarPlaneDistance();

        //TODO: Ближний туман, но ломается днем границы
        /*
        float start = farPlane * (0.25f - 0.22f * factor);
        float end   = farPlane * (0.45f - 0.40f * factor);
         */

        float start = farPlane * (0.75f - 0.70f * factor);
        float end   = farPlane * (1.0f  - 0.75f * factor);

        GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
        GlStateManager.setFogStart(start);
        GlStateManager.setFogEnd(end);
    }
}
