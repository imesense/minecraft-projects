package org.imesense.dynamicspawncontrol.core.worldcache;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import org.imesense.dynamicspawncontrol.core.LogFile;

public class CacheMonitor
{
    private static boolean instanceExists = false;

    public CacheMonitor()
    {
        if (instanceExists)
        {
            LogFile.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    public void renderDebugInfo(int width, int height)
    {
        MatrixStack matrixStack = new MatrixStack();
        final FontRenderer FONT_RENDER = Minecraft.getInstance().font;

        final String ACTUAL_ANIMALS = TextFormatting.GREEN + "Actual Animals: " + Cache.Instance.getActualAnimalCount();
        final String ACTUAL_HOSTILE = TextFormatting.RED + "Actual Hostile Entities: " + Cache.Instance.getActualHostileEntityCount();
        final String ACTUAL_TOTAL = TextFormatting.YELLOW + "Actual Total Entities: " + Cache.Instance.getActualTotalEntityCount();
        final String UPDATE_TICK = TextFormatting.WHITE + "Tick Counter: " + Cache.Instance.TickCounter;
        final String VALID_CHUNKS = TextFormatting.YELLOW + "Valid Chunks: " + Cache.Instance.getValidChunkCount();

        final String SEPARATOR = TextFormatting.WHITE + "--------------------------------------------";

        final int X = 10, Y = 10;

        FONT_RENDER.drawShadow(matrixStack, ACTUAL_ANIMALS, X, Y, 0xFFFFFF);
        FONT_RENDER.drawShadow(matrixStack, ACTUAL_HOSTILE, X, Y + 10, 0xFFFFFF);
        FONT_RENDER.drawShadow(matrixStack, ACTUAL_TOTAL, X, Y + 20, 0xFFFFFF);
        FONT_RENDER.drawShadow(matrixStack, UPDATE_TICK, X, Y + 30, 0xFFFFFF);
        FONT_RENDER.drawShadow(matrixStack, VALID_CHUNKS, X, Y + 40, 0xFFFFFF);
        FONT_RENDER.drawShadow(matrixStack, SEPARATOR, X, Y + 50, 0xFFFFFF);

        final String BUFFER_ANIMALS = TextFormatting.GREEN + "Buffer Animals: " + Cache.Instance.getBufferAnimalCount();
        final String BUFFER_HOSTILE = TextFormatting.RED + "Buffer Hostile Entities: " + Cache.Instance.getBufferHostileEntityCount();
        final String BUFFER_TOTAL = TextFormatting.YELLOW + "Buffer Total Entities: " + Cache.Instance.getBufferTotalEntityCount();

        FONT_RENDER.drawShadow(matrixStack, BUFFER_ANIMALS, X, Y + 60, 0xFFFFFF);
        FONT_RENDER.drawShadow(matrixStack, BUFFER_HOSTILE, X, Y + 70, 0xFFFFFF);
        FONT_RENDER.drawShadow(matrixStack, BUFFER_TOTAL, X, Y + 80, 0xFFFFFF);
    }
}

