package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
public final class RenderFPS
{
    private static volatile RenderFPS _INSTANCE;

    public static RenderFPS getInstance()
    {
        return CodeGeneric.getInstance(RenderFPS.class);
    }

    public RenderFPS()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void renderFpsOverlay(RenderGameOverlayEvent.Text event)
    {
        if (UniqueField.CLIENT.currentScreen instanceof net.minecraft.client.gui.GuiChat)
            return;

        ScaledResolution res = event.getResolution();

        String fpsText = "FPS: " + Minecraft.getDebugFPS();

        int textWidth = UniqueField.CLIENT.fontRenderer.getStringWidth(fpsText);
        int x = res.getScaledWidth() - textWidth - 2;
        int y = res.getScaledHeight() - 10;

        UniqueField.CLIENT.fontRenderer.drawStringWithShadow(fpsText, x, y, 0xFFFFFF);
    }
}
