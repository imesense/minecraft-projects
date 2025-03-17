package org.imesense.dynamicspawncontrol.core.register.config;

import org.imesense.dynamicspawncontrol.core.baseregister.BaseConfigRegister;
import org.imesense.dynamicspawncontrol.core.config.fileLegacy.*;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.plugin.darkness_forge_1_12_x_0_5_0.config.CfgDarkness;
import org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.config.CfgFogWorld;
import org.imesense.dynamicspawncontrol.plugin.staminaplus_1_12_2_1_1_1.config.CfgStaminaPlayer;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.config.CfgTimeControl;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.config.CfgWebSlinger;

public final class ConfigRegister extends BaseConfigRegister
{
    private static volatile ConfigRegister _INSTANCE;

    public static ConfigRegister getInstance()
    {
        return CodeGeneric.getInstance(ConfigRegister.class);
    }

    private static final Class<?>[] CONFIG_CLASSES =
    {
        CacheWorldGameConfig.class,
        LogFileConfig.class,
        PlayerConceptConfig.class,
        CfgDarkness.class,
        WindowTitleConfig.class,
        BlockWorldGeneratorConfig.class,
        CfgTimeControl.class,
        ZombieDropItemConfig.class,
        SkeletonDropItemConfig.class,
        CfgWebSlinger.class,
        CfgStaminaPlayer.class,
        CfgFogWorld.class
    };

    @Override
    protected Class<?>[] getConfigClasses()
    {
        return CONFIG_CLASSES;
    }
}
