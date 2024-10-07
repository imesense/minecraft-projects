package org.imesense.dynamicspawncontrol.debug.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture;
import static net.minecraft.client.gui.Gui.drawRect;

/**
 * OldSerpskiStalker
 * TODO: Реализовать 'высоту' сложности, например 5 черепков от 5 до 20 высота в шахте и так далее
 */
@Mod.EventBusSubscriber
public final class OnEventDummy
{
    private Biome currentBiome = null;
    private Biome displayedBiome = null; //-' биом, который будет отображаться (с задержкой)
    private long lastBiomeChangeTime = 0;
    private String biomeText = "";
    private final Minecraft mc = Minecraft.getMinecraft();

    String[][] biomes =
            {
            {"ocean", "0.5F"},
            {"plains", "0.8F"},
            {"desert", "2.0F"},
            {"extreme_hills", "0.2F"},
            {"forest", "0.7F"},
            {"taiga", "0.25F"},
            {"swampland", "0.8F"},
            {"river", "0.5F"},
            {"hell", "2.0F"},
            {"sky", "0.5F"},
            {"frozen_ocean", "0.0F"},
            {"frozen_river", "0.0F"},
            {"ice_flats", "0.0F"},
            {"ice_mountains", "0.0F"},
            {"mushroom_island", "0.9F"},
            {"mushroom_island_shore", "0.9F"},
            {"beaches", "0.8F"},
            {"desert_hills", "2.0F"},
            {"forest_hills", "0.7F"},
            {"taiga_hills", "0.25F"},
            {"smaller_extreme_hills", "0.2F"},
            {"jungle", "0.95F"},
            {"jungle_hills", "0.95F"},
            {"jungle_edge", "0.95F"},
            {"deep_ocean", "0.5F"},
            {"stone_beach", "0.2F"},
            {"cold_beach", "0.05F"},
            {"birch_forest", "0.6F"},
            {"birch_forest_hills", "0.6F"},
            {"roofed_forest", "0.7F"},
            {"taiga_cold", "-0.5F"},
            {"taiga_cold_hills", "-0.5F"},
            {"redwood_taiga", "0.3F"},
            {"redwood_taiga_hills", "0.3F"},
            {"extreme_hills_with_trees", "0.2F"},
            {"savanna", "1.2F"},
            {"savanna_rock", "1.0F"},
            {"mesa", "2.0F"},
            {"mesa_rock", "2.0F"},
            {"mesa_clear_rock", "2.0F"}
    };

    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnEventDummy()
    {
		CodeGenericUtils.printInitClassToLog(this.getClass());

        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    // Время, которое игрок должен провести в новом биоме, прежде чем обновить отображаемый биом (в миллисекундах)
    private final long biomeChangeDelay = 3000; // 3 секунды
    private Biome previousBiome = null;  // Предыдущий биом
    // Список биомов, которые игнорируются
    private final List<String> ignoredBiomes = Arrays.asList(
            "Ocean", "Deep Ocean", "River", "Beach", "DesertHills" // Здесь можно указать любые биомы
    );

    // Список типов биомов, которые игнорируются
    private final List<String> ignoredBiomeTypes = Arrays.asList(
            "Ocean", "Beach", "River", "DesertHills"
    );

    // Время, которое игрок должен провести в новом биоме, прежде чем обновить отображаемый биом (в миллисекундах)
    private final long biomeChangeMinTime = 3000; // 3 секунды
    //private Biome currentBiome = null;  // Текущий биом
    private Biome confirmedBiome = null;  // Подтверждённый биом для отображения (после задержки)
    private long biomeEntryTime = 0;  // Время входа в новый биом
    //private long lastBiomeChangeTime = 0;  // Время последнего подтверждённого изменения биома

    /**
     * метод для проверки игнорирования биома.
     * игнорируем биомы не по точному совпадению имени, а по типу или категории.
     */
    private boolean isBiomeIgnored(Biome biome) {
        String biomeName = biome.getBiomeName();

        // Проверка по типам биома
        for (String ignoredType : ignoredBiomeTypes) {
            if (biomeName.toLowerCase().contains(ignoredType.toLowerCase())) {
                return true;  // Биом должен быть проигнорирован
            }
        }

        // Дополнительная логика игнорирования, если нужно
        return false;
    }

    @SubscribeEvent
    public void onPlayerTick(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            Biome biome = player.world.getBiome(player.getPosition());

            // Проверяем, изменился ли биом
            if (biome != currentBiome) {
                // Если биом изменился, обновляем текущий биом и запоминаем время входа
                currentBiome = biome;
                biomeEntryTime = System.currentTimeMillis();
            }

            // Проверяем, прошло ли достаточно времени, чтобы подтвердить, что игрок остался в новом биоме
            long currentTime = System.currentTimeMillis();
            if (currentBiome != confirmedBiome && currentTime - biomeEntryTime >= biomeChangeMinTime) {
                // Если игрок находится в новом биоме достаточно долго, обновляем отображаемый биом
                confirmedBiome = currentBiome;
                lastBiomeChangeTime = currentTime;
                biomeText = confirmedBiome.getBiomeName();  // Обновляем текст биома для отображения
     //           System.out.println("Biome updated to: " + biomeText);  // Для отладки
            }
        }
    }


    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        long currentTime = System.currentTimeMillis();

        // Проверяем, есть ли биом для отображения и прошло ли меньше 5 секунд с его смены
        if (confirmedBiome != null && currentTime - lastBiomeChangeTime < 5000) {
            int boxWidth = 120;  // Ширина рамки
            int boxHeight = 50;  // Высота рамки

            // Получаем ширину экрана
            int screenWidth = mc.displayWidth / mc.gameSettings.guiScale;

            // Выравниваем по центру экрана
            int xPos = (screenWidth - boxWidth) / 2;
            int yPos = 20;  // Отступ сверху

            int backgroundColor = 0x80000000;  // Полупрозрачный черный

            // Рисуем рамку
            drawRect(xPos, yPos, xPos + boxWidth, yPos + boxHeight, backgroundColor);

            // Центрирование текста
            int textWidth = mc.fontRenderer.getStringWidth(biomeText);
            int textXPos = xPos + (boxWidth - textWidth) / 2;
            int textYPos = yPos + 10;

            // Отрисовка текста
            mc.fontRenderer.drawString(biomeText, textXPos, textYPos, 0xFFFFFF);

            // Получение количества черепков
            int[] skullCounts = {
                    getRedSkullCountForBiome(confirmedBiome),
                    getOrangeSkullCountForBiome(confirmedBiome),
                    getRedSkullCountForBiomePart(confirmedBiome),
                    getOrangeSkullCountForBiomePart(confirmedBiome)
            };

            ResourceLocation[] skullTextures = {
                    new ResourceLocation("dynamicspawncontrol", "textures/gui/red_skull.png"),
                    new ResourceLocation("dynamicspawncontrol", "textures/gui/orange_skull.png"),
                    new ResourceLocation("dynamicspawncontrol", "textures/gui/red_skull_part.png"),
                    new ResourceLocation("dynamicspawncontrol", "textures/gui/orange_skull_part.png")
            };

            // Общая ширина всех черепков с учетом отступов
            int totalSkulls = skullCounts[0] + skullCounts[1] + skullCounts[2] + skullCounts[3];
            int skullWidth = 16;  // Ширина одного черепка
            int skullSpacing = 2; // Отступ между черепками
            int totalSkullWidth = (skullWidth * totalSkulls) + (skullSpacing * (totalSkulls - 1));

            // Начальная позиция для отрисовки черепков (по центру)
            int skullXPos = xPos + (boxWidth - totalSkullWidth) / 2;
            int skullYPos = textYPos + 20;

            // Отрисовка черепков
            for (int i = 0; i < skullCounts.length; i++) {
                for (int j = 0; j < skullCounts[i]; j++) {
                    mc.getTextureManager().bindTexture(skullTextures[i]);
                    drawModalRectWithCustomSizedTexture(skullXPos, skullYPos, 0, 0, skullWidth, skullWidth, skullWidth, skullWidth);
                    skullXPos += skullWidth + skullSpacing;  // Смещаем следующую позицию вправо
                }
            }
        }
    }

    /**
     *
     * @param biome
     * @return
     */
    private int getRedSkullCountForBiome(Biome biome) {
        switch (biome.getBiomeName()) {
            case "Extreme Hills":
                return 5;
            case "Desert":
                return 3;
            case "Swampland":
                return 4;
            case "Ice Flats":
                return 5;
            case "Forest":
                return 2;
            case "Jungle":
                return 4;
            case "Beach":
                return 1;
            case "Taiga":
                return 3;
            case "Mesa":
                return 4;
            default:
                return 0;
        }
    }

    /**
     *
     * @param biome
     * @return
     */
    private int getRedSkullCountForBiomePart(Biome biome) {
        switch (biome.getBiomeName()) {
            default:
                return 0;
        }
    }

    //-' Получение количества оранжевых черепков
    private int getOrangeSkullCountForBiome(Biome biome) {
        switch (biome.getBiomeName()) {
            case "Plains":
                return 1;
            case "Savanna":
                return 1;
            case "Desert":
                return 2;
            case "Swampland":
                return 1;
            case "Ice Flats":
                return 2;
            case "Forest":
                return 3;
            case "Jungle":
                return 1;
            case "Beach":
                return 2;
            case "Taiga":
                return 1;
            case "Mesa":
                return 1;
            default:
                return 0;
        }
    }


    /**
     *
     * @param biome
     * @return
     */
    private int getOrangeSkullCountForBiomePart(Biome biome) {
        switch (biome.getBiomeName()) {
            default:
                return 0;
        }
    }

}
