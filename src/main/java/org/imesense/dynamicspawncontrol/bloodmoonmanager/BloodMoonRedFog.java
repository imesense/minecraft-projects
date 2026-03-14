package org.imesense.dynamicspawncontrol.bloodmoonmanager;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class BloodMoonRedFog
{
    @SubscribeEvent
    public void onFogDensity(EntityViewRenderEvent.FogDensity event)
    {
        event.setCanceled(true);

        GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP2);

        float pulse = (float)Math.sin(System.currentTimeMillis() * 0.001) * 0.005F;
        GL11.glFogf(GL11.GL_FOG_DENSITY, 0.025F + pulse);
    }

    @SubscribeEvent
    public void onFogColors(EntityViewRenderEvent.FogColors event)
    {
        event.setRed(0.8F);
        event.setGreen(0.05F);
        event.setBlue(0.05F);
    }
}