package org.imesense.dynamicspawncontrol;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.common.Loader;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

public enum DynamicSpawnControlInitMixins
{
    // Divine RPG
    DIVINE_RPG_AI_FIX(true, "mixins\\divinerpg\\ai\\EntityPeacefulUntilAttacked.Fix.json", "divinerpg"),
    DIVINE_RPG_KOBLIN_REWORK(true, "mixins\\divinerpg\\entity\\EntityKobblin.Rework.json", "divinerpg"),
    DIVINE_RPG_SPIDER_REWORK(true, "mixins\\divinerpg\\entity\\EntityPumpkinSpider.Rework.json", "divinerpg"),
    DIVINE_RPG_SHADAHIER_REWORK(true, "mixins\\divinerpg\\entity\\EntityShadahier.Rework.json", "divinerpg"),
    DIVINE_RPG_SPAWN_FIX(true, "mixins\\divinerpg\\event\\EntitySpawnRegistry.Fix.json", "divinerpg"),

    // IC2 Wireless Industry
    WIRELESS_INDUSTRY_FIX(true, "mixins\\ic2expwirelessindustry\\WorldLoadUnloadHandler.Fix.json", "wirelesstools"),

    // Interfaces
    ENTITY_RENDERER_ACCESSOR(false, "mixins\\interfaces\\IEntityRendererAccessor.json"),
    GUI_INGAME_ACCESSOR(false, "mixins\\interfaces\\IGuiIngameAccessor.json"),

    // Minecraft
    ENCHANTMENT_UPDATE(false, "mixins\\minecraft\\enchantment\\Enchantment.Update.json"),
    FOOD_STATS_UPDATE(false, "mixins\\minecraft\\food\\FoodStats.Update.json"),
    GUI_INGAME_UPDATE(false, "mixins\\minecraft\\gui\\GuiIngameForge.Update.json"),
    ITEM_FOOD_UPDATE(false, "mixins\\minecraft\\item\\ItemFood.Update.json"),
    ENTITY_RENDERER_REWORK(false, "mixins\\minecraft\\renderer\\EntityRenderer.Rework.json"),

    // Special Mobs
    SPECIALMOBS_REPLACER_FIX(true, "mixins\\specialmobs\\SpecialMobReplacer.Fix.json", "specialmobs"),

    // SR Parasites
    SRPARASITES_HANDLER_FIX(true, "mixins\\srparasites\\handler\\SRPEventHandlerBus.Update.json", "srparasites"),
    SRPARASITES_EVENT_FIX(true, "mixins\\srparasites\\util\\ParasiteEventEntity.Update.json", "srparasites");

    private final boolean conditional;
    private final String configPath;
    private final String requiredModId;

    DynamicSpawnControlInitMixins(boolean conditional, String configPath)
    {
        this(conditional, configPath, null);
    }

    DynamicSpawnControlInitMixins(boolean conditional, String configPath, String requiredModId)
    {
        this.conditional = conditional;
        this.configPath = configPath;
        this.requiredModId = requiredModId;
    }

    public void register()
    {
        EarlyLogBuffer.log(Log.DEBUG, "Registering mixin: " + this.name() + " | path: " + configPath);

        if (conditional && requiredModId != null)
        {
            EarlyLogBuffer.log(Log.DEBUG, "  conditional: true | mod: " + requiredModId + " | checking at runtime");

            FermiumRegistryAPI.enqueueMixin(true, configPath,
            () ->
                {
                    boolean loaded = Loader.isModLoaded(requiredModId);
                    EarlyLogBuffer.log(Log.DEBUG, "  runtime check for " + this.name() + ": mod " + requiredModId + " loaded = " + loaded);

                    return loaded;
                });
        }
        else
        {
            EarlyLogBuffer.log(Log.DEBUG, "  conditional: false");
            FermiumRegistryAPI.enqueueMixin(false, configPath);
        }
    }
}