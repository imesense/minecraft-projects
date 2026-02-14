package org.imesense.dynamicspawncontrol.core.logfile;

import org.imesense.dynamicspawncontrol.DynamicSpawnControl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class MixinLoadLog
{
    private static final String[] MIXIN_CONFIGS =
    {
        "mixins/divinerpg/ai/EntityPeacefulUntilAttackedFix.json",
        "mixins/divinerpg/entity/EntityKobblinRework.json",
        "mixins/divinerpg/entity/EntityPumpkinSpiderRework.json",
        "mixins/divinerpg/entity/EntityShadahierRework.json",
        "mixins/divinerpg/event/EntitySpawnRegistryFix.json",
        "mixins/ic2expwirelessindustry/WorldLoadUnloadHandlerFix.json",
        "mixins/interfaces/IEntityRendererAccessor.json",
        "mixins/interfaces/IGuiIngameAccessor.json",
        "mixins/minecraft/enchantment/EnchantmentUpdate.json",
        "mixins/minecraft/food/FoodStatsUpdate.json",
        "mixins/minecraft/gui/GuiIngameForgeUpdate.json",
        "mixins/minecraft/item/ItemFoodUpdate.json",
        "mixins/minecraft/renderer/EntityRendererRework.json",
        "mixins/specialmobs/SpecialMobReplacerFix.json",
        "mixins/srparasites/handler/SRPEventHandlerBusUpdate.json",
        "mixins/srparasites/util/ParasiteEventEntityUpdate.json"
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
