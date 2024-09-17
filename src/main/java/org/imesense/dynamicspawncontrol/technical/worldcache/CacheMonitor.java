package org.imesense.dynamicspawncontrol.technical.worldcache;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.gui.ScaledResolution;

import static org.imesense.dynamicspawncontrol.technical.worldcache.Cache.*;

/**
 *
 */
public class CacheMonitor
{
    /**
     *
     */
    protected static int x = 10, y = 10;

    /**
     *
     */
    public static final Minecraft GetMinecraft = Minecraft.getMinecraft();

    /**
     *
     * @param resolution
     */
    public static void renderDebugInfo(ScaledResolution resolution)
    {
        FontRenderer fontRenderer = GetMinecraft.fontRenderer;

        String animalMessage = TextFormatting.GREEN + "Animals: " + getAnimalCount();
        String hostileMessage = TextFormatting.RED + "Hostile Entities: " + getHostileEntityCount();
        String totalMessage = TextFormatting.YELLOW + "Total Entities: " + getTotalEntityCount();

        fontRenderer.drawString(animalMessage, x, y, 0xFFFFFF);
        fontRenderer.drawString(hostileMessage, x, y + 10, 0xFFFFFF);
        fontRenderer.drawString(totalMessage, x, y + 20, 0xFFFFFF);
    }
}
