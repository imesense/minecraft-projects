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
    protected static final int X = 10, Y = 10;

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
        final FontRenderer FONT_RENDER = GetMinecraft.fontRenderer;

        final String ANIMALS = TextFormatting.GREEN + "Animals: " + getAnimalCount();
        final String HOSTILE = TextFormatting.RED + "Hostile Entities: " + getHostileEntityCount();
        final String TOTAL = TextFormatting.YELLOW + "Total Entities: " + getTotalEntityCount();

        FONT_RENDER.drawString(ANIMALS, X, Y, 0xFFFFFF);
        FONT_RENDER.drawString(HOSTILE, X, Y + 10, 0xFFFFFF);
        FONT_RENDER.drawString(TOTAL, X, Y + 20, 0xFFFFFF);
    }
}
