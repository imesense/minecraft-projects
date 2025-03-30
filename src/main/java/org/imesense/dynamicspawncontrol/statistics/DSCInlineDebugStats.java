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
import org.imesense.dynamicspawncontrol.core.threads.ThreadMonitor;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheGeneralStorage;

import java.text.SimpleDateFormat;
import java.util.Date;

@InitLog
public final class DSCInlineDebugStats
{
    private static volatile DSCInlineDebugStats _INSTANCE;
    private static final Minecraft MC = Minecraft.getMinecraft();
    private final CacheGeneralStorage CACHE_GENERAL_STORAGE = CacheGeneralStorage.getInstance();
    private final ThreadMonitor threadMonitor = ThreadMonitor.getInstance();

    public static DSCInlineDebugStats getInstance()
    {
        return CodeGeneric.getInstance(DSCInlineDebugStats.class);
    }

    public DSCInlineDebugStats()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

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

        final String ACTUAL_ANIMALS = TextFormatting.GREEN + "Actual Animals: " + CACHE_GENERAL_STORAGE.getActualAnimalCount();
        final String ACTUAL_HOSTILE = TextFormatting.RED + "Actual Hostile: " + CACHE_GENERAL_STORAGE.getActualHostileEntityCount();
        final String ACTUAL_TOTAL = TextFormatting.YELLOW + "Actual Total: " + CACHE_GENERAL_STORAGE.getActualTotalEntityCount();
        final String UPDATE_TICK = TextFormatting.WHITE + "Tick Counter: " + CACHE_GENERAL_STORAGE.TickCounter;
        final String VALID_CHUNKS = TextFormatting.YELLOW + "Valid Chunks: " + CACHE_GENERAL_STORAGE.getValidChunkCount();
        final String BUFFER_ANIMALS = TextFormatting.GREEN + "Buffer Animals: " + CACHE_GENERAL_STORAGE.getBufferAnimalCount();
        final String BUFFER_HOSTILE = TextFormatting.RED + "Buffer Hostile: " + CACHE_GENERAL_STORAGE.getBufferHostileEntityCount();
        final String BUFFER_TOTAL = TextFormatting.YELLOW + "Buffer Total: " + CACHE_GENERAL_STORAGE.getBufferTotalEntityCount();
        final String ENTITIES_BY_NAME = TextFormatting.AQUA + "Entities by Name: " + CACHE_GENERAL_STORAGE.ENTITIES_ACTUAL_BY_NAME.size();
        final String ENTITIES_BY_RESOURCE = TextFormatting.BLUE + "Entities by Resource: " + CACHE_GENERAL_STORAGE.ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.size();
        final String LAST_UPDATE = TextFormatting.GRAY + "Last Update: " + (CACHE_GENERAL_STORAGE.IsFirstUpdate ? "First" : "Subsequent");
        final String PRIMARY_PLAYER = TextFormatting.GOLD + "Primary Player: " + CACHE_GENERAL_STORAGE.IsPrimaryPlayerLogged;
        final String LAST_UPDATE_TIME = TextFormatting.GRAY + "Last Update Time: " + new Date(CACHE_GENERAL_STORAGE.getLastUpdateTime());
        final String ACTUAL_WATER_MOBS = TextFormatting.DARK_BLUE + "Actual Water Mobs: " + CACHE_GENERAL_STORAGE.getActualWaterMobCount();
        final String BUFFER_WATER_MOBS = TextFormatting.DARK_BLUE + "Buffer Water Mobs: " + CACHE_GENERAL_STORAGE.getBufferWaterMobCount();

        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        String memoryUsage = TextFormatting.LIGHT_PURPLE + String.format("Memory: %.1f/%.1fMB (%.1f%%)",
                usedMemory / (1024.0 * 1024.0),
                runtime.maxMemory() / (1024.0 * 1024.0),
                (usedMemory * 100.0) / runtime.maxMemory());

        String memoryDetails = TextFormatting.DARK_PURPLE + String.format("Heap: %.1fMB | Used: %.1fMB | Free: %.1fMB",
                runtime.totalMemory() / (1024.0 * 1024.0),
                usedMemory / (1024.0 * 1024.0),
                runtime.freeMemory() / (1024.0 * 1024.0));

        long lastCleanMillis = MemoryEvents.getLastCleanTime();
        String lastCleanTime = TextFormatting.GOLD + "Last GC: " +
                (lastCleanMillis == 0 ? "Never" : new SimpleDateFormat("HH:mm:ss").format(new Date(lastCleanMillis)));

        long nextCleanMillis = lastCleanMillis + (Configuration.getAutomaticCleanup().getMaxInterval() * 1000L);
        long timeUntilNextClean = nextCleanMillis - System.currentTimeMillis();
        String nextCleanTime = TextFormatting.YELLOW + "Next GC in: " +
                (timeUntilNextClean <= 0 ? "Now" : String.format("%.1f min", timeUntilNextClean / 60000.0));

        String timeSinceClean = TextFormatting.AQUA + "Since GC: " +
                (lastCleanMillis == 0 ? "N/A" : String.format("%.1f min", (System.currentTimeMillis() - lastCleanMillis) / 60000.0));

        int x = 10;
        int y = 10;
        int lineHeight = 10;
        int currentY = y;

        currentY = drawDebugBlock(fontRenderer, x, currentY, lineHeight,
                TextFormatting.GREEN + "FPS: " + Minecraft.getDebugFPS(),
                TextFormatting.AQUA + "Pos: X: " + playerPos.getX() + " Y: " + playerPos.getY() + " Z: " + playerPos.getZ(),
                TextFormatting.YELLOW + "Biome: " + biome.getBiomeName(),
                TextFormatting.GOLD + "Light - Sky: " + skyLight + " | Block: " + blockLight,
                TextFormatting.RED + "Local Difficulty: " + difficulty,
                TextFormatting.DARK_PURPLE + "Day: " + day,
                TextFormatting.BLUE + "Time: " + timeOfDay,
                TextFormatting.DARK_AQUA + "Moon Phase: " + moonPhase);

        currentY = drawSeparator(fontRenderer, x, currentY, lineHeight);

        currentY = drawDebugBlock(fontRenderer, x, currentY, lineHeight,
                ACTUAL_ANIMALS,
                ACTUAL_HOSTILE,
                ACTUAL_TOTAL,
                UPDATE_TICK,
                VALID_CHUNKS);

        currentY = drawSeparator(fontRenderer, x, currentY, lineHeight);

        currentY = drawDebugBlock(fontRenderer, x, currentY, lineHeight,
                BUFFER_ANIMALS,
                BUFFER_HOSTILE,
                BUFFER_TOTAL);

        currentY = drawSeparator(fontRenderer, x, currentY, lineHeight);

        currentY = drawDebugBlock(fontRenderer, x, currentY, lineHeight,
                ENTITIES_BY_NAME,
                ENTITIES_BY_RESOURCE);

        currentY = drawSeparator(fontRenderer, x, currentY, lineHeight);

        currentY = drawDebugBlock(fontRenderer, x, currentY, lineHeight,
                ACTUAL_WATER_MOBS,
                BUFFER_WATER_MOBS);

        currentY = drawSeparator(fontRenderer, x, currentY, lineHeight);

        currentY = drawDebugBlock(fontRenderer, x, currentY, lineHeight,
                LAST_UPDATE,
                PRIMARY_PLAYER,
                LAST_UPDATE_TIME);

        currentY = drawSeparator(fontRenderer, x, currentY, lineHeight);

        currentY = drawDebugBlock(fontRenderer, x, currentY, lineHeight,
                memoryUsage,
                memoryDetails,
                lastCleanTime,
                timeSinceClean,
                nextCleanTime);

        renderThreadStats(fontRenderer, x, currentY);
    }

    private int drawDebugBlock(FontRenderer fontRenderer, int x, int y, int lineHeight, String... lines) {
        for (String line : lines)
        {
            fontRenderer.drawString(line, x, y, 0xFFFFFF);
            y += lineHeight;
        }
        return y;
    }

    private int drawSeparator(FontRenderer fontRenderer, int x, int y, int lineHeight)
    {
        String separator = TextFormatting.WHITE + "-------------------------------------";
        fontRenderer.drawString(separator, x, y, 0xFFFFFF);
        return y + lineHeight;
    }

    private void renderThreadStats(FontRenderer fontRenderer, int x, int startY)
    {
        int lineHeight = 10;
        ThreadMonitor.ThreadStats mainStats = threadMonitor.getThreadStats("main");
        ThreadMonitor.ThreadStats loggingStats = threadMonitor.getThreadStats("logging");

        drawDebugBlock(fontRenderer, x, startY, lineHeight,
                TextFormatting.WHITE + "-------- [ Thread Statistics ] --------",
                TextFormatting.RED + String.format("Main Thread: %.1fms (Max: %.1fms, Avg: %.1fms)",
                        mainStats.currentDelay,
                        mainStats.maxDelay,
                        mainStats.averageDelay),
                TextFormatting.YELLOW + String.format("Tick Count: %d", mainStats.tickCount),
                TextFormatting.GREEN + String.format("Estimated TPS: %.1f", 1000.0 / (50 + mainStats.currentDelay)),
                TextFormatting.LIGHT_PURPLE + "-------- [ Logging Thread ] --------",
                TextFormatting.AQUA + String.format("Logging Delay: %.1fms (Max: %.1fms)",
                        loggingStats != null ? loggingStats.currentDelay : 0,
                        loggingStats != null ? loggingStats.maxDelay : 0),
                TextFormatting.BLUE + String.format("Log Operations: %d",
                        loggingStats != null ? loggingStats.tickCount : 0));
    }

    public void updateMainThreadStats()
    {
        threadMonitor.updateMainThreadStats();
    }
}
