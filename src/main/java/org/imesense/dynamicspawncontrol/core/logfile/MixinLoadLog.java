package org.imesense.dynamicspawncontrol.core.logfile;

import org.imesense.dynamicspawncontrol.DynamicSpawnControl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class MixinLoadLog
{
    private static final String[] MIXIN_CONFIGS =
    {
        "mixins/divinerpg/ai/EntityPeacefulUntilAttacked.Fix.json",
        "mixins/divinerpg/entity/EntityKobblin.Rework.json",
        "mixins/divinerpg/entity/EntityPumpkinSpider.Rework.json",
        "mixins/divinerpg/entity/EntityShadahier.Rework.json",
        "mixins/divinerpg/event/EntitySpawnRegistry.Fix.json",
        "mixins/ic2expwirelessindustry/WorldLoadUnloadHandler.Fix.json",
        "mixins/interfaces/IEntityRendererAccessor.json",
        "mixins/interfaces/IGuiIngameAccessor.json",
        "mixins/minecraft/enchantment/Enchantment.Update.json",
        "mixins/minecraft/food/FoodStats.Update.json",
        "mixins/minecraft/gui/GuiIngameForge.Update.json",
        "mixins/minecraft/item/ItemFood.Update.json",
        "mixins/minecraft/renderer/EntityRenderer.Rework.json",
        "mixins/specialmobs/SpecialMobReplacer.Fix.json",
        "mixins/srparasites/handler/SRPEventHandlerBus.Update.json",
        "mixins/srparasites/util/ParasiteEventEntity.Update.json"
    };

    public static void loadMixins()
    {
        ClassLoader classLoader = DynamicSpawnControl.class.getClassLoader();

        Log.write(0, "Searching for mixin config files...");

        for (String config : MIXIN_CONFIGS)
        {
            Log.write(0, "Trying to load: " + config);

            try (InputStream inputStream = classLoader.getResourceAsStream(config))
            {
                if (inputStream == null)
                {
                    Log.write(2, "Mixin not found in resources: " + config);
                    continue;
                }

                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)))
                {
                    String line;
                    StringBuilder stringBuilder = new StringBuilder();
                    boolean isFirstLine = true;

                    while ((line = bufferedReader.readLine()) != null)
                    {
                        if (!isFirstLine)
                        {
                            stringBuilder.append('\n');
                        }
                        else
                        {
                            isFirstLine = false;
                        }

                        stringBuilder.append(line);
                    }

                    Log.write(0, "Successfully loaded mixin: " + config);
                    Log.write(0, stringBuilder.toString());
                }
            }
            catch (Exception exception)
            {
                Log.write(2, "Error loading " + config + ": " + exception.getMessage());
                exception.printStackTrace();
            }
        }
    }
}
