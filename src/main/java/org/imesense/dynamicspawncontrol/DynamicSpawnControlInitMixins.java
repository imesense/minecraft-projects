package org.imesense.dynamicspawncontrol;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.common.Loader;

public enum DynamicSpawnControlInitMixins
{
    // Divine RPG
    DIVINE_RPG_AI_FIX(true, "mixins/divinerpg/ai/EntityPeacefulUntilAttackedFix.json", "divinerpg"),
    DIVINE_RPG_KOBLIN_REWORK(true, "mixins/divinerpg/entity/EntityKobblinRework.json", "divinerpg"),
    DIVINE_RPG_SPIDER_REWORK(true, "mixins/divinerpg/entity/EntityPumpkinSpiderRework.json", "divinerpg"),
    DIVINE_RPG_SHADAHIER_REWORK(true, "mixins/divinerpg/entity/EntityShadahierRework.json", "divinerpg"),
    DIVINE_RPG_SPAWN_FIX(true, "mixins/divinerpg/event/EntitySpawnRegistryFix.json", "divinerpg"),

    // IC2 Wireless Industry
    WIRELESS_INDUSTRY_FIX(true, "mixins/ic2expwirelessindustry/WorldLoadUnloadHandlerFix.json", "wirelesstools"),

    // Interfaces
    ENTITY_RENDERER_ACCESSOR(false, "mixins/interfaces/IEntityRendererAccessor.json"),
    GUI_INGAME_ACCESSOR(false, "mixins/interfaces/IGuiIngameAccessor.json"),

    // Minecraft
    ENCHANTMENT_UPDATE(false, "mixins/minecraft/enchantment/EnchantmentUpdate.json"),
    FOOD_STATS_UPDATE(false, "mixins/minecraft/food/FoodStatsUpdate.json"),
    GUI_INGAME_UPDATE(false, "mixins/minecraft/gui/GuiIngameForgeUpdate.json"),
    ITEM_FOOD_UPDATE(false, "mixins/minecraft/item/ItemFoodUpdate.json"),
    ENTITY_RENDERER_REWORK(false, "mixins/minecraft/renderer/EntityRendererRework.json"),

    // Special Mobs
    SPECIALMOBS_REPLACER_FIX(true, "mixins/specialmobs/SpecialMobReplacerFix.json", "specialmobs"),

    // SR Parasites
    SRPARASITES_HANDLER_FIX(true, "mixins/srparasites/handler/SRPEventHandlerBusUpdate.json", "srparasites"),
    SRPARASITES_EVENT_FIX(true, "mixins/srparasites/util/ParasiteEventEntityUpdate.json", "srparasites");

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
        if (conditional && requiredModId != null)
        {
            FermiumRegistryAPI.enqueueMixin(true, configPath,
                    () -> Loader.isModLoaded(requiredModId));
        }
        else
        {
            FermiumRegistryAPI.enqueueMixin(false, configPath);
        }
    }
}
