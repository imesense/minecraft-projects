package org.imesense.dynamicspawncontrol.core.logfile;

import org.imesense.dynamicspawncontrol.DynamicSpawnControl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class MixinLoadLog
{
    private static final String[] MIXIN_CONFIGS =
    {
        "assets/mixins/divinerpg/ai/EntityPeacefulUntilAttacked.Fix.json",
        "assets/mixins/divinerpg/entity/EntityKobblin.Rework.json",
        "assets/mixins/divinerpg/entity/EntityPumpkinSpider.Rework.json",
        "assets/mixins/divinerpg/entity/EntityShadahier.Rework.json",
        "assets/mixins/divinerpg/entity/EntityLheiva.Rework.json",
        "assets/mixins/divinerpg/entity/EntityHellPig.Fix.json",
        "assets/mixins/divinerpg/entity/EntityTwins.Rework.json",
        "assets/mixins/divinerpg/event/EntitySpawnRegistry.Fix.json",
        "assets/mixins/ic2expwirelessindustry/WorldLoadUnloadHandler.Fix.json",
        "assets/mixins/interfaces/IEntityRendererAccessor.json",
        "assets/mixins/interfaces/IGuiIngameAccessor.json",
        "assets/mixins/minecraft/enchantment/Enchantment.Update.json",
        "assets/mixins/minecraft/food/FoodStats.Update.json",
        "assets/mixins/minecraft/gui/GuiIngameForge.Update.json",
        "assets/mixins/minecraft/item/ItemFood.Update.json",
        "assets/mixins/minecraft/renderer/EntityRenderer.Rework.json",
        "assets/mixins/minecraft/block/BlockFire.Fix.json",
        "assets/mixins/specialmobs/SpecialMobReplacer.Fix.json",
        "assets/mixins/srparasites/handler/SRPEventHandlerBus.Update.json",
        "assets/mixins/srparasites/util/ParasiteEventEntity.Update.json",
        "assets/mixins/railraft/BetaMessageTickHandler.Update.json",
        "assets/mixins/cavesplus/CavesProcedureGen.Update.json"
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
