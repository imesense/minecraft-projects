package org.imesense.dynamicspawncontrol;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.common.Loader;

public enum DynamicSpawnControlInitMixins
{
    UNLIMITED_ENCHANTMENT(false, "mixins\\mixin.unlimited.enchantment.json"),
    DARKNESS_RENDERER(false, "mixins\\mixin.darkness.renderer.json"),
    PLAYER_HUNGER(false, "mixins\\mixin.minecraft.food.json"),

    DIVINE_RPG_FIX(true, "mixins\\mixin.fix.spawn.divinerpg.json", "divinerpg"),
    WIRELESS_INDUSTRY(true, "mixins\\mixin.ic2.exp.wireless.industry.json", "wirelesstools"),
    SRPARASITES_CONFIG(true, "mixins\\mixin.srparasites.config.json", "srparasites"),
    SPECIALMOBS_CONFIG(true, "mixins\\mixin.specialmobs.json", "specialmobs");

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
