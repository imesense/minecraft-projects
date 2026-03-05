package org.imesense.dynamicspawncontrol;

import fermiumbooter.FermiumRegistryAPI;
import lombok.Getter;
import net.minecraftforge.fml.common.Loader;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;

import java.util.Arrays;

public enum DynamicSpawnControlInitMixins
{
    // ===== Divine RPG =====
    DIVINE_RPG_AI_FIX(true, "assets/mixins/divinerpg/ai/EntityPeacefulUntilAttacked.Fix.json", "divinerpg"),
    DIVINE_RPG_KOBLIN_REWORK(true, "assets/mixins/divinerpg/entity/EntityKobblin.Rework.json", "divinerpg"),
    DIVINE_RPG_SPIDER_REWORK(true, "assets/mixins/divinerpg/entity/EntityPumpkinSpider.Rework.json", "divinerpg"),
    DIVINE_RPG_SHADAHIER_REWORK(true, "assets/mixins/divinerpg/entity/EntityShadahier.Rework.json", "divinerpg"),
    DIVINE_RPG_LHEIVA(true, "assets/mixins/divinerpg/entity/EntityLheiva.Rework.json", "divinerpg"),
    DIVINE_RPG_HELL_PIG(true, "assets/mixins/divinerpg/entity/EntityHellPig.Fix.json", "divinerpg"),
    DIVINE_RPG_TWINS(true, "assets/mixins/divinerpg/entity/EntityTwins.Rework.json", "divinerpg"),
    DIVINE_RPG_FROST_ARCHER(true, "assets/mixins/divinerpg/entity/EntityFrostArcher.Fix.json", "divinerpg"),
    DIVINE_RPG_SPAWN_FIX(true, "assets/mixins/divinerpg/event/EntitySpawnRegistry.Fix.json", "divinerpg"),

    // ===== IC2 Wireless Industry =====
    WIRELESS_INDUSTRY_FIX(true, "assets/mixins/ic2expwirelessindustry/WorldLoadUnloadHandler.Fix.json", "wirelesstools"),

    // ===== Interfaces =====
    ENTITY_RENDERER_ACCESSOR(false, "assets/mixins/interfaces/IEntityRendererAccessor.json"),
    GUI_INGAME_ACCESSOR(false, "assets/mixins/interfaces/IGuiIngameAccessor.json"),
    WORLD_ACCESSOR(false, "assets/mixins/interfaces/IWorldAccessor.json"),
    VEC3D_ACCESSOR(false, "assets/mixins/interfaces/IVec3dAccessor.json"),

    // ===== Minecraft =====
    ENCHANTMENT_UPDATE(false, "assets/mixins/minecraft/enchantment/Enchantment.Update.json"),
    FOOD_STATS_UPDATE(false, "assets/mixins/minecraft/food/FoodStats.Update.json"),
    GUI_INGAME_UPDATE(false, "assets/mixins/minecraft/gui/GuiIngameForge.Update.json"),
    ITEM_FOOD_UPDATE(false, "assets/mixins/minecraft/item/ItemFood.Update.json"),
    ENTITY_RENDERER_REWORK(false, "assets/mixins/minecraft/renderer/EntityRenderer.Rework.json"),
    BLOCK_FIRE_FIX(false, "assets/mixins/minecraft/block/BlockFire.Fix.json"),
    WORLD_UPDATE(false, "assets/mixins/minecraft/world/World.Update.json"),
    LAYER_SPIDER_EYES_FIX(false, "assets/mixins/minecraft/entity/layers/LayerSpiderEyes.Fix.json"),
    LAYER_ENDERMAN_EYES_FIX(false, "assets/mixins/minecraft/entity/layers/LayerEndermanEyes.Fix.json"),
    MINECRAFT_CREATE_DISPLAY_UPDATE(false, "assets/mixins/minecraft/minecraft/Minecraft.Update.json"),

    // ===== Special Mobs =====
    SPECIALMOBS_REPLACER_FIX(true, "assets/mixins/specialmobs/SpecialMobReplacer.Fix.json", "specialmobs"),

    // ===== SR Parasites =====
    SRPARASITES_HANDLER_FIX(true, "assets/mixins/srparasites/handler/SRPEventHandlerBus.Update.json", "srparasites"),
    SRPARASITES_EVENT_FIX(true, "assets/mixins/srparasites/util/ParasiteEventEntity.Update.json", "srparasites"),

    // ===== Railcraft =====
    RAILCRAFT_EVENT_BETA_MESSAGE_TICK_HANDLER_UPDATE(true, "assets/mixins/railcraft/BetaMessageTickHandler.Update.json", "railcraft"),

    // ===== CavesPlus =====
    CAVESPLUS_PROCEDURE_GEN_FIX(true, "assets/mixins/cavesplus/CavesProcedureGen.Update.json", "caves");

    @Getter
    private final String configPath;
    private final String requiredModId;
    private final boolean conditional;

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
        if (!conditional)
        {
            logRegistration("ALWAYS");
            FermiumRegistryAPI.enqueueMixin(false, configPath);
            return;
        }

        // Conditional mixin
        logRegistration("CONDITIONAL (" + requiredModId + ")");

        FermiumRegistryAPI.enqueueMixin(true, configPath, () ->
        {
            boolean loaded = Loader.isModLoaded(requiredModId);

            EarlyLogBuffer.log(
                    LogManager.DEBUG,
                    String.format("[MIXIN] %-45s → %s",
                            this.name(),
                            loaded ? "LOADED" : "SKIPPED (mod not present)")
            );

            return loaded;
        });
    }

    private void logRegistration(String type)
    {
        EarlyLogBuffer.log(
                LogManager.DEBUG,
                String.format("[MIXIN] %-45s | %-18s | %s",
                        this.name(),
                        type,
                        configPath)
        );
    }

    public static String[] getAllConfigPaths()
    {
        return Arrays.stream(values())
                .map(DynamicSpawnControlInitMixins::getConfigPath)
                .toArray(String[]::new);
    }

    public static String[] getConditionalConfigPaths()
    {
        return Arrays.stream(values())
                .filter(m -> m.conditional)
                .map(DynamicSpawnControlInitMixins::getConfigPath)
                .toArray(String[]::new);
    }
}