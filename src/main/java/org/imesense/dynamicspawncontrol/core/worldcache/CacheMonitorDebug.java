package org.imesense.dynamicspawncontrol.core.worldcache;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.gui.ScaledResolution;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventWorldCache;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.util.Date;

public final class CacheMonitorDebug
{
    private static volatile CacheMonitorDebug _INSTANCE;

    public static CacheMonitorDebug getInstance()
    {
        return CodeGeneric.getInstance(CacheMonitorDebug.class);
    }

    private final CacheGeneralStorage CACHE_GENERAL_STORAGE = CacheGeneralStorage.getInstance();

    public CacheMonitorDebug()
    {
		CodeGeneric.printInitClassToLog(this.getClass());
    }

    public void renderDebugInfo(ScaledResolution resolution)
    {
        final FontRenderer FONT_RENDER = UniqueField.CLIENT.fontRenderer;

        final String ACTUAL_ANIMALS = TextFormatting.GREEN + "Actual Animals: " + this.CACHE_GENERAL_STORAGE.getActualAnimalCount();
        final String ACTUAL_HOSTILE = TextFormatting.RED + "Actual Hostile Entities: " + this.CACHE_GENERAL_STORAGE.getActualHostileEntityCount();
        final String ACTUAL_TOTAL = TextFormatting.YELLOW + "Actual Total Entities: " + this.CACHE_GENERAL_STORAGE.getActualTotalEntityCount();
        final String UPDATE_TICK = TextFormatting.WHITE + "Tick Counter: " + this.CACHE_GENERAL_STORAGE.TickCounter;
        final String VALID_CHUNKS = TextFormatting.YELLOW + "Valid Chunks: " + this.CACHE_GENERAL_STORAGE.getValidChunkCount();

        final String BUFFER_ANIMALS = TextFormatting.GREEN + "Buffer Animals: " + this.CACHE_GENERAL_STORAGE.getBufferAnimalCount();
        final String BUFFER_HOSTILE = TextFormatting.RED + "Buffer Hostile Entities: " + this.CACHE_GENERAL_STORAGE.getBufferHostileEntityCount();
        final String BUFFER_TOTAL = TextFormatting.YELLOW + "Buffer Total Entities: " + this.CACHE_GENERAL_STORAGE.getBufferTotalEntityCount();

        final String ENTITIES_BY_NAME = TextFormatting.AQUA + "Entities by Name: " + this.CACHE_GENERAL_STORAGE.ENTITIES_ACTUAL_BY_NAME.size();
        final String ENTITIES_BY_RESOURCE = TextFormatting.BLUE + "Entities by Resource: " + this.CACHE_GENERAL_STORAGE.ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.size();

        final String LAST_UPDATE = TextFormatting.GRAY + "Last Update: " + (this.CACHE_GENERAL_STORAGE.IsFirstUpdate ? "First Update" : "Subsequent Update");
        final String PRIMARY_PLAYER = TextFormatting.GOLD + "Primary Player Logged: " + this.CACHE_GENERAL_STORAGE.IsPrimaryPlayerLogged;
        final String LAST_UPDATE_TIME = TextFormatting.GRAY + "Last Update Time: " + new Date(this.CACHE_GENERAL_STORAGE.getLastUpdateTime()).toString();

        final String SEPARATOR = TextFormatting.WHITE + "-------------------------------------------------------------------";

        final int X = 10, Y = 10;

        FONT_RENDER.drawString(ACTUAL_ANIMALS, X, Y, 0xFFFFFF);
        FONT_RENDER.drawString(ACTUAL_HOSTILE, X, Y + 10, 0xFFFFFF);
        FONT_RENDER.drawString(ACTUAL_TOTAL, X, Y + 20, 0xFFFFFF);
        FONT_RENDER.drawString(UPDATE_TICK, X, Y + 30, 0xFFFFFF);
        FONT_RENDER.drawString(VALID_CHUNKS, X, Y + 40, 0xFFFFFF);
        FONT_RENDER.drawString(SEPARATOR, X, Y + 50, 0xFFFFFF);

        FONT_RENDER.drawString(BUFFER_ANIMALS, X, Y + 60, 0xFFFFFF);
        FONT_RENDER.drawString(BUFFER_HOSTILE, X, Y + 70, 0xFFFFFF);
        FONT_RENDER.drawString(BUFFER_TOTAL, X, Y + 80, 0xFFFFFF);
        FONT_RENDER.drawString(SEPARATOR, X, Y + 90, 0xFFFFFF);

        FONT_RENDER.drawString(ENTITIES_BY_NAME, X, Y + 100, 0xFFFFFF);
        FONT_RENDER.drawString(ENTITIES_BY_RESOURCE, X, Y + 110, 0xFFFFFF);
        FONT_RENDER.drawString(SEPARATOR, X, Y + 120, 0xFFFFFF);

        FONT_RENDER.drawString(LAST_UPDATE, X, Y + 130, 0xFFFFFF);
        FONT_RENDER.drawString(PRIMARY_PLAYER, X, Y + 140, 0xFFFFFF);

        FONT_RENDER.drawString(LAST_UPDATE_TIME, X, Y + 150, 0xFFFFFF);
    }
}
