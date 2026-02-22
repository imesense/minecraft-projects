package org.imesense.dynamicspawncontrol;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.common.Loader;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

public enum DynamicSpawnControlInitMixins
{
    // Divine RPG
    DIVINE_RPG_AI_FIX(true, "assets\\mixins\\divinerpg\\ai\\EntityPeacefulUntilAttacked.Fix.json", "divinerpg"),
    DIVINE_RPG_KOBLIN_REWORK(true, "assets\\mixins\\divinerpg\\entity\\EntityKobblin.Rework.json", "divinerpg"),
    DIVINE_RPG_SPIDER_REWORK(true, "assets\\mixins\\divinerpg\\entity\\EntityPumpkinSpider.Rework.json", "divinerpg"),
    DIVINE_RPG_SHADAHIER_REWORK(true, "assets\\mixins\\divinerpg\\entity\\EntityShadahier.Rework.json", "divinerpg"),
    DIVINE_RPG_LHEIVA(true, "assets\\mixins\\divinerpg\\entity\\EntityLheiva.Rework.json", "divinerpg"),
    DIVINE_RPG_HELL_PIG(true, "assets\\mixins\\divinerpg\\entity\\EntityHellPig.Fix.json", "divinerpg"),
    DIVINE_RPG_TWINS(true, "assets\\mixins\\divinerpg\\entity\\EntityTwins.Rework.json", "divinerpg"),
    DIVINE_RPG_SPAWN_FIX(true, "assets\\mixins\\divinerpg\\event\\EntitySpawnRegistry.Fix.json", "divinerpg"),

    // IC2 Wireless Industry
    WIRELESS_INDUSTRY_FIX(true, "assets\\mixins\\ic2expwirelessindustry\\WorldLoadUnloadHandler.Fix.json", "wirelesstools"),

    // Interfaces
    ENTITY_RENDERER_ACCESSOR(false, "assets\\mixins\\interfaces\\IEntityRendererAccessor.json"),
    GUI_INGAME_ACCESSOR(false, "assets\\mixins\\interfaces\\IGuiIngameAccessor.json"),

    // Minecraft
    ENCHANTMENT_UPDATE(false, "assets\\mixins\\minecraft\\enchantment\\Enchantment.Update.json"),
    FOOD_STATS_UPDATE(false, "assets\\mixins\\minecraft\\food\\FoodStats.Update.json"),
    GUI_INGAME_UPDATE(false, "assets\\mixins\\minecraft\\gui\\GuiIngameForge.Update.json"),
    ITEM_FOOD_UPDATE(false, "assets\\mixins\\minecraft\\item\\ItemFood.Update.json"),
    ENTITY_RENDERER_REWORK(false, "assets\\mixins\\minecraft\\renderer\\EntityRenderer.Rework.json"),

    // Special Mobs
    SPECIALMOBS_REPLACER_FIX(true, "assets\\mixins\\specialmobs\\SpecialMobReplacer.Fix.json", "specialmobs"),

    // SR Parasites
    SRPARASITES_HANDLER_FIX(true, "assets\\mixins\\srparasites\\handler\\SRPEventHandlerBus.Update.json", "srparasites"),
    SRPARASITES_EVENT_FIX(true, "assets\\mixins\\srparasites\\util\\ParasiteEventEntity.Update.json", "srparasites"),

    // Railcraft
    RAILCRAFT_EVENT_BETA_MESSAGE_TICK_HANDLER_UPDATE(true, "assets\\mixins\\railcraft\\BetaMessageTickHandler.Update.json", "railcraft");

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