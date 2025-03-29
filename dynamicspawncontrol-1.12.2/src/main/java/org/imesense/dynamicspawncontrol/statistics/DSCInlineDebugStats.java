package org.imesense.dynamicspawncontrol.statistics;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.config.debug.DebugConfig;
import org.imesense.dynamicspawncontrol.core.memory.Configuration;
import org.imesense.dynamicspawncontrol.core.memory.MemoryEvents;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheGeneralStorage;

import java.text.SimpleDateFormat;
import java.util.Date;

@InitLog
public final class DSCInlineDebugStats
{
    private static volatile DSCInlineDebugStats _INSTANCE;

    public static DSCInlineDebugStats getInstance()
    {
        return CodeGeneric.getInstance(DSCInlineDebugStats.class);
    }

    private final CacheGeneralStorage CACHE_GENERAL_STORAGE = CacheGeneralStorage.getInstance();

    public DSCInlineDebugStats()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    private static final Minecraft MC = Minecraft.getMinecraft();

    public void handleRenderDebugInfo(RenderGameOverlayEvent.Post event)
    {
        if (MC.gameSettings.showDebugInfo || !DebugConfig.getInstance(DebugConfig.class).isShowStats())
        {
            return;
        }

        FontRenderer fontRenderer = MC.fontRenderer;
        BlockPos playerPos = MC.player.getPosition();
        Biome biome = MC.world.getBiome(playerPos);
        int skyLight = MC.world.getLightFor(EnumSkyBlock.SKY, playerPos);
        int blockLight = MC.world.getLightFor(EnumSkyBlock.BLOCK, playerPos);
        long worldTime = MC.world.getWorldTime();
        int day = (int) (worldTime / 24000);
        int timeOfDay = (int) (worldTime % 24000);
        int moonPhase = MC.world.getMoonPhase();
        float difficulty = MC.world.getDifficultyForLocation(playerPos).getAdditionalDifficulty();

        String fps =
                TextFormatting.GREEN + "FPS: " + Minecraft.getDebugFPS();

        String coordinates =
                TextFormatting.AQUA + "Pos: X: " + playerPos.getX() + " Y: " + playerPos.getY() + " Z: " + playerPos.getZ();

        String biomeInfo =
                TextFormatting.YELLOW + "Biome: " + biome.getBiomeName();

        String lightLevels =
                TextFormatting.GOLD + "Light - Sky: " + skyLight + " | Block: " + blockLight;

        String difficultyInfo =
                TextFormatting.RED + "Local Difficulty: " + difficulty;

        String gameDay =
                TextFormatting.DARK_PURPLE + "Day: " + day;

        String time =
                TextFormatting.BLUE + "Time: " + timeOfDay;

        String moonPhaseInfo =
                TextFormatting.DARK_AQUA + "Moon Phase: " + moonPhase;

        final String ACTUAL_ANIMALS =
                TextFormatting.GREEN + "Actual Animals: " + this.CACHE_GENERAL_STORAGE.getActualAnimalCount();

        final String ACTUAL_HOSTILE =
                TextFormatting.RED + "Actual Hostile Entities: " + this.CACHE_GENERAL_STORAGE.getActualHostileEntityCount();

        final String ACTUAL_TOTAL =
                TextFormatting.YELLOW + "Actual Total Entities: " + this.CACHE_GENERAL_STORAGE.getActualTotalEntityCount();

        final String UPDATE_TICK =
                TextFormatting.WHITE + "Tick Counter: " + this.CACHE_GENERAL_STORAGE.TickCounter;

        final String VALID_CHUNKS =
                TextFormatting.YELLOW + "Valid Chunks: " + this.CACHE_GENERAL_STORAGE.getValidChunkCount();

        final String BUFFER_ANIMALS =
                TextFormatting.GREEN + "Buffer Animals: " + this.CACHE_GENERAL_STORAGE.getBufferAnimalCount();

        final String BUFFER_HOSTILE =
                TextFormatting.RED + "Buffer Hostile Entities: " + this.CACHE_GENERAL_STORAGE.getBufferHostileEntityCount();

        final String BUFFER_TOTAL =
                TextFormatting.YELLOW + "Buffer Total Entities: " + this.CACHE_GENERAL_STORAGE.getBufferTotalEntityCount();

        final String ENTITIES_BY_NAME =
                TextFormatting.AQUA + "Entities by Name: " + this.CACHE_GENERAL_STORAGE.ENTITIES_ACTUAL_BY_NAME.size();

        final String ENTITIES_BY_RESOURCE =
                TextFormatting.BLUE + "Entities by Resource: " + this.CACHE_GENERAL_STORAGE.ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.size();

        final String LAST_UPDATE =
                TextFormatting.GRAY + "Last Update: " +
                        (this.CACHE_GENERAL_STORAGE.IsFirstUpdate ? "First Update" : "Subsequent Update");

        final String PRIMARY_PLAYER =
                TextFormatting.GOLD + "Primary Player Logged: " + this.CACHE_GENERAL_STORAGE.IsPrimaryPlayerLogged;

        final String LAST_UPDATE_TIME =
                TextFormatting.GRAY + "Last Update Time: " + new Date(this.CACHE_GENERAL_STORAGE.getLastUpdateTime()).toString();

        final String ACTUAL_WATER_MOBS =
                TextFormatting.DARK_BLUE + "Actual Water Mobs: " + this.CACHE_GENERAL_STORAGE.getActualWaterMobCount();

        final String BUFFER_WATER_MOBS =
                TextFormatting.DARK_BLUE + "Buffer Water Mobs: " + this.CACHE_GENERAL_STORAGE.getBufferWaterMobCount();

        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        String memoryUsage = TextFormatting.LIGHT_PURPLE + String.format(
                "Memory: %.1f/%.1fMB (%.1f%%)",
                usedMemory / (1024.0 * 1024.0),
                maxMemory / (1024.0 * 1024.0),
                (usedMemory * 100.0) / maxMemory
        );

        String memoryDetails = TextFormatting.DARK_PURPLE + String.format(
                "Heap: %.1fMB | Used: %.1fMB | Free: %.1fMB",
                totalMemory / (1024.0 * 1024.0),
                usedMemory / (1024.0 * 1024.0),
                freeMemory / (1024.0 * 1024.0)
        );

        long lastCleanMillis = MemoryEvents.getLastCleanTime();
        String lastCleanTime = TextFormatting.GOLD + "Last GC: " +
                (lastCleanMillis == 0 ? "Never" :
                        new SimpleDateFormat("HH:mm:ss").format(new Date(lastCleanMillis)));

        long nextCleanMillis = lastCleanMillis + (Configuration.getAutomaticCleanup().getMaxInterval() * 1000L);
        long timeUntilNextClean = nextCleanMillis - System.currentTimeMillis();
        String nextCleanTime = TextFormatting.YELLOW + "Next GC in: " +
                (timeUntilNextClean <= 0 ? "Now" :
                        String.format("%.1f min", timeUntilNextClean / 60000.0));

        String timeSinceClean = TextFormatting.AQUA + "Since GC: " +
                (lastCleanMillis == 0 ? "N/A" :
                        String.format("%.1f min", (System.currentTimeMillis() - lastCleanMillis) / 60000.0));

        int x = 10;
        int y = 10;

        fontRenderer.drawString(fps, x, y, 0xFFFFFF);
        fontRenderer.drawString(coordinates, x, y + 10, 0xFFFFFF);
        fontRenderer.drawString(biomeInfo, x, y + 20, 0xFFFFFF);
        fontRenderer.drawString(lightLevels, x, y + 30, 0xFFFFFF);
        fontRenderer.drawString(difficultyInfo, x, y + 40, 0xFFFFFF);
        fontRenderer.drawString(gameDay, x, y + 50, 0xFFFFFF);
        fontRenderer.drawString(time, x, y + 60, 0xFFFFFF);
        fontRenderer.drawString(moonPhaseInfo, x, y + 70, 0xFFFFFF);

        String separator = TextFormatting.WHITE + "-------------------------------------";
        fontRenderer.drawString(separator, x, y + 80, 0xFFFFFF);

        int cacheY = y + 90;

        fontRenderer.drawString(ACTUAL_ANIMALS, x, cacheY, 0xFFFFFF);
        fontRenderer.drawString(ACTUAL_HOSTILE, x, cacheY + 10, 0xFFFFFF);
        fontRenderer.drawString(ACTUAL_TOTAL, x, cacheY + 20, 0xFFFFFF);
        fontRenderer.drawString(UPDATE_TICK, x, cacheY + 30, 0xFFFFFF);
        fontRenderer.drawString(VALID_CHUNKS, x, cacheY + 40, 0xFFFFFF);
        fontRenderer.drawString(separator, x, cacheY + 50, 0xFFFFFF);

        fontRenderer.drawString(BUFFER_ANIMALS, x, cacheY + 60, 0xFFFFFF);
        fontRenderer.drawString(BUFFER_HOSTILE, x, cacheY + 70, 0xFFFFFF);
        fontRenderer.drawString(BUFFER_TOTAL, x, cacheY + 80, 0xFFFFFF);
        fontRenderer.drawString(separator, x, cacheY + 90, 0xFFFFFF);

        fontRenderer.drawString(ENTITIES_BY_NAME, x, cacheY + 100, 0xFFFFFF);
        fontRenderer.drawString(ENTITIES_BY_RESOURCE, x, cacheY + 110, 0xFFFFFF);
        fontRenderer.drawString(separator, x, cacheY + 120, 0xFFFFFF);

        fontRenderer.drawString(ACTUAL_WATER_MOBS, x, cacheY + 130, 0xFFFFFF);
        fontRenderer.drawString(BUFFER_WATER_MOBS, x, cacheY + 140, 0xFFFFFF);
        fontRenderer.drawString(separator, x, cacheY + 150, 0xFFFFFF);

        fontRenderer.drawString(LAST_UPDATE, x, cacheY + 160, 0xFFFFFF);
        fontRenderer.drawString(PRIMARY_PLAYER, x, cacheY + 170, 0xFFFFFF);
        fontRenderer.drawString(LAST_UPDATE_TIME, x, cacheY + 180, 0xFFFFFF);

        int memoryY = cacheY + 190;
        fontRenderer.drawString(separator, x, memoryY, 0xFFFFFF);
        fontRenderer.drawString(memoryUsage, x, memoryY + 10, 0xFFFFFF);
        fontRenderer.drawString(memoryDetails, x, memoryY + 20, 0xFFFFFF);
        fontRenderer.drawString(lastCleanTime, x, memoryY + 30, 0xFFFFFF);
        fontRenderer.drawString(timeSinceClean, x, memoryY + 40, 0xFFFFFF);
        fontRenderer.drawString(nextCleanTime, x, memoryY + 50, 0xFFFFFF);
    }
}
