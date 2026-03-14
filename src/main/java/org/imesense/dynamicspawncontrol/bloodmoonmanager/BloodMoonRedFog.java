package org.imesense.dynamicspawncontrol.bloodmoonmanager;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.lwjgl.opengl.GL11;

public class BloodMoonRedFog
{
    //TODO: Механизм расчета тумана:
    // У нас есть оригинальный метод event.getDensity();, который всегда 1. вместо него
    // Когда начинается кровавая луна, отключаем оригинальное событи и сразу подменяем этот кофэ.
    // С кровавой луны, после чего он начинает прибывать, не ломая туман и отключая обработку оригинального события
    // С убываением тоже самое, когда коэф. достигает 0.1, мы отключаем его влияение и переводим туман в оригинальное
    // Состояние без учета коэф. луны
    @SubscribeEvent
    public void onFogDensity(EntityViewRenderEvent.FogDensity event)
    {
        float originalDensity = event.getDensity();

        LogManager.debug("originalDensity: " + originalDensity);

        // Изменяем плотность (например, делаем в 2 раза гуще)
        float newDensity = originalDensity * 2.5f;

        // Устанавливаем новую плотность
        event.setDensity(newDensity);

        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onFogColors(EntityViewRenderEvent.FogColors event)
    {
        float factor = ClientBloodmoonHandler.BLOODMOON_FOG_FACTOR;

        // Получаем текущие цвета (стандартные для текущего биома/погоды)
        float currentRed = event.getRed();
        float currentGreen = event.getGreen();
        float currentBlue = event.getBlue();

        // Целевые цвета кровавой луны
        float targetRed = 0.8F;
        float targetGreen = 0.05F;
        float targetBlue = 0.05F;

        // Интерполируем между текущими и целевыми цветами
        // Но оставляем часть оригинального цвета всегда
        float red = currentRed * (1.0f - factor * 0.8f) + targetRed * (factor * 0.8f);
        float green = currentGreen * (1.0f - factor * 0.9f) + targetGreen * (factor * 0.9f);
        float blue = currentBlue * (1.0f - factor * 0.9f) + targetBlue * (factor * 0.9f);

        // Клиппим чтобы цвета не выходили за пределы
        event.setRed(Math.min(1.0f, red));
        event.setGreen(Math.min(1.0f, green));
        event.setBlue(Math.min(1.0f, blue));
    }
}