package org.imesense.dynamicspawncontrol.debug.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
@Mod.EventBusSubscriber
public final class OnEventDummy
{
    private Biome currentBiome = null;
    private long lastBiomeChangeTime = 0;
    private String biomeText = "";
    private final Minecraft mc = Minecraft.getMinecraft();

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

    @SubscribeEvent
    public void onPlayerTick(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            Biome biome = player.world.getBiome(player.getPosition());

            if (biome != currentBiome) {
                currentBiome = biome;
                lastBiomeChangeTime = System.currentTimeMillis();

                String biomeName = biome.getBiomeName();
                int difficultyLevel = getBiomeDifficulty(biome);

                biomeText = biomeName + " - Difficulty: " + difficultyLevel;

                // Вывести сообщение в чат
                player.sendMessage(new TextComponentString(biomeText));
            }
        }
    }

    // Функция для получения уровня сложности биома
    private int getBiomeDifficulty(Biome biome) {
        // Пример зависимости сложности от температуры биома
        float temperature = biome.getDefaultTemperature();
        if (temperature < 0.15) return 1; // Ледяные биомы
        else if (temperature > 1.0) return 3; // Пустынные биомы
        return 2; // Нормальные биомы
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        long currentTime = System.currentTimeMillis();

        // Отображаем текст только в течение 5 секунд после входа в биом
        if (currentTime - lastBiomeChangeTime < 5000) {
            event.getLeft().add(biomeText);
        }
    }
}
