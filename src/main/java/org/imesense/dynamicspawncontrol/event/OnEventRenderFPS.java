package org.imesense.dynamicspawncontrol.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventRenderFPS
{
    public OnEventRenderFPS()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    @SubscribeEvent
    public static void onRenderGameOverlay_0(RenderGameOverlayEvent.Text event)
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
