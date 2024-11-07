package org.imesense.dynamicspawncontrol;

import net.minecraftforge.fml.common.Mod;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.api.IDebug;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventSandBox implements IDebug
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnEventSandBox()
    {
		CodeGeneric.printInitClassToLog(this.getClass());

        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    //@SubscribeEvent
    //public static void onFogDensity(EntityViewRenderEvent.FogDensity event) {
        // Задание плотности тумана
   //     event.setDensity(0.05f); // Чем больше значение, тем плотнее туман
    //    GlStateManager.setFog(GlStateManager.FogMode.EXP);
    //    event.setCanceled(true); // Обязательно отменяем стандартное поведение, чтобы применился наш туман
   // }

    //@SubscribeEvent
   // public static void onFogColors(EntityViewRenderEvent.FogColors event) {
        // Установка красного цвета для тумана
   //     event.setRed(0.7f);   // Красный оттенок
   //     event.setGreen(0.2f); // Зеленый оттенок
   //     event.setBlue(0.2f);  // Синий оттенок
   // }
}
