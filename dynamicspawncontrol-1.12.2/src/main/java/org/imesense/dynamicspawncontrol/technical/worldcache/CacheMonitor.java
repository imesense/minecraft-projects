package org.imesense.dynamicspawncontrol.technical.worldcache;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.gui.ScaledResolution;

import static org.imesense.dynamicspawncontrol.technical.worldcache.Cache.*;

/**
 *
 */
public final class CacheMonitor
{
    /**
     *
     */
    private static final int X = 10, Y = 10;

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

        final String ACTUAL_ANIMALS = TextFormatting.GREEN + "Actual Animals: " + getActualAnimalCount();
        final String ACTUAL_HOSTILE = TextFormatting.RED + "Actual Hostile Entities: " + getActualHostileEntityCount();
        final String ACTUAL_TOTAL = TextFormatting.YELLOW + "Actual Total Entities: " + getActualTotalEntityCount();
        final String UPDATE_TICK = TextFormatting.WHITE + "Tick Counter: " + TickCounter;
        final String VALID_CHUNKS = TextFormatting.YELLOW + "Valid Chunks: " + getValidChunkCount();

        final String SEPARATOR = TextFormatting.WHITE + "--------------------------------------------";

        FONT_RENDER.drawString(ACTUAL_ANIMALS, X, Y, 0xFFFFFF);
        FONT_RENDER.drawString(ACTUAL_HOSTILE, X, Y + 10, 0xFFFFFF);
        FONT_RENDER.drawString(ACTUAL_TOTAL, X, Y + 20, 0xFFFFFF);
        FONT_RENDER.drawString(UPDATE_TICK, X, Y + 30, 0xFFFFFF);
        FONT_RENDER.drawString(VALID_CHUNKS, X, Y + 40, 0xFFFFFF);
        FONT_RENDER.drawString(SEPARATOR, X, Y + 50, 0xFFFFFF);

        final String BUFFER_ANIMALS = TextFormatting.GREEN + "Buffer Animals: " + getBufferAnimalCount();
        final String BUFFER_HOSTILE = TextFormatting.RED + "Buffer Hostile Entities: " + getBufferHostileEntityCount();
        final String BUFFER_TOTAL = TextFormatting.YELLOW + "Buffer Total Entities: " + getBufferTotalEntityCount();

        FONT_RENDER.drawString(BUFFER_ANIMALS, X, Y + 60, 0xFFFFFF);
        FONT_RENDER.drawString(BUFFER_HOSTILE, X, Y + 70, 0xFFFFFF);
        FONT_RENDER.drawString(BUFFER_TOTAL, X, Y + 80, 0xFFFFFF);
    }
}
