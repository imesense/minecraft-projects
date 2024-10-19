package org.imesense.dynamicspawncontrol.technical.worldcache;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.gui.ScaledResolution;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import static org.imesense.dynamicspawncontrol.technical.worldcache.Cache.*;

/**
 *
 */
public final class CacheMonitor
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public final Minecraft GetMinecraft = Minecraft.getMinecraft();

    /**
     *
     */
    public CacheMonitor()
    {
		CodeGenericUtils.printInitClassToLog(this.getClass());
		
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    /**
     *
     * @param resolution
     */
    public void renderDebugInfo(ScaledResolution resolution)
    {
        final FontRenderer FONT_RENDER = GetMinecraft.fontRenderer;

        final String ACTUAL_ANIMALS = TextFormatting.GREEN + "Actual Animals: " + Cache.instance.getActualAnimalCount();
        final String ACTUAL_HOSTILE = TextFormatting.RED + "Actual Hostile Entities: " + Cache.instance.getActualHostileEntityCount();
        final String ACTUAL_TOTAL = TextFormatting.YELLOW + "Actual Total Entities: " + Cache.instance.getActualTotalEntityCount();
        final String UPDATE_TICK = TextFormatting.WHITE + "Tick Counter: " + Cache.instance.TickCounter;
        final String VALID_CHUNKS = TextFormatting.YELLOW + "Valid Chunks: " + Cache.instance.getValidChunkCount();

        final String SEPARATOR = TextFormatting.WHITE + "--------------------------------------------";

        final int X = 10, Y = 10;

        FONT_RENDER.drawString(ACTUAL_ANIMALS, X, Y, 0xFFFFFF);
        FONT_RENDER.drawString(ACTUAL_HOSTILE, X, Y + 10, 0xFFFFFF);
        FONT_RENDER.drawString(ACTUAL_TOTAL, X, Y + 20, 0xFFFFFF);
        FONT_RENDER.drawString(UPDATE_TICK, X, Y + 30, 0xFFFFFF);
        FONT_RENDER.drawString(VALID_CHUNKS, X, Y + 40, 0xFFFFFF);
        FONT_RENDER.drawString(SEPARATOR, X, Y + 50, 0xFFFFFF);

        final String BUFFER_ANIMALS = TextFormatting.GREEN + "Buffer Animals: " + Cache.instance.getBufferAnimalCount();
        final String BUFFER_HOSTILE = TextFormatting.RED + "Buffer Hostile Entities: " + Cache.instance.getBufferHostileEntityCount();
        final String BUFFER_TOTAL = TextFormatting.YELLOW + "Buffer Total Entities: " + Cache.instance.getBufferTotalEntityCount();

        FONT_RENDER.drawString(BUFFER_ANIMALS, X, Y + 60, 0xFFFFFF);
        FONT_RENDER.drawString(BUFFER_HOSTILE, X, Y + 70, 0xFFFFFF);
        FONT_RENDER.drawString(BUFFER_TOTAL, X, Y + 80, 0xFFFFFF);
    }
}
