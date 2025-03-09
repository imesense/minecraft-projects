package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

public final class RenderFPS
{
    private static volatile RenderFPS _INSTANCE;

    public static RenderFPS getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (RenderFPS.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new RenderFPS();
                }
            }
        }

        return _INSTANCE;
    }

    public RenderFPS()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    public void renderFpsOverlay()
    {
        if (!(UniqueField.CLIENT.currentScreen instanceof net.minecraft.client.gui.GuiChat))
        {
            ScaledResolution scaled = new ScaledResolution(UniqueField.CLIENT);

            String fpsText = "FPS: " + Minecraft.getDebugFPS();

            int textWidth = UniqueField.CLIENT.fontRenderer.getStringWidth(fpsText);

            int x = scaled.getScaledWidth() - textWidth - 2;

            UniqueField.CLIENT.fontRenderer.drawString(fpsText, x, scaled.getScaledHeight() - 10, 0x80FFFFFF);
        }
    }
}
