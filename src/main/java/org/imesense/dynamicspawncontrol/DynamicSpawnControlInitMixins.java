package org.imesense.dynamicspawncontrol;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.common.Loader;

public enum DynamicSpawnControlInitMixins
{
    UNLIMITED_ENCHANTMENT(false, "mixin.unlimited.enchantment.json"),
    DARKNESS_RENDERER(false, "mixin.darkness.renderer.json"),
    PLAYER_HUNGER(false, "mixin.minecraft.satiety.json"),

    DIVINE_RPG_FIX(true, "mixin.fix.spawn.divinerpg.json", "divinerpg"),
    WIRELESS_INDUSTRY(true, "mixin.ic2.exp.wireless.industry.json", "wirelesstools"),
    SRPARASITES_CONFIG(true, "mixin.srparasites.config.json", "srparasites");

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
