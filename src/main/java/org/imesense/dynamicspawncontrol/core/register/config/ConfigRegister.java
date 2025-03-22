package org.imesense.dynamicspawncontrol.core.register.config;

import org.imesense.dynamicspawncontrol.core.baseregister.BaseConfigRegister;
import org.imesense.dynamicspawncontrol.core.config.blockworldgenerator.BlockWorldGeneratorConfig;
import org.imesense.dynamicspawncontrol.core.config.debug.DebugConfig;
import org.imesense.dynamicspawncontrol.core.config.dropitem.SkeletonDropConfig;
import org.imesense.dynamicspawncontrol.core.config.dropitem.ZombieDropConfig;
import org.imesense.dynamicspawncontrol.core.config.logfile.LogFileConfig;
import org.imesense.dynamicspawncontrol.core.config.mainwindow.MainWindowTitleConfig;
import org.imesense.dynamicspawncontrol.core.config.player.PlayerConfig;
import org.imesense.dynamicspawncontrol.core.config.worldcache.WorldCacheConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

public final class ConfigRegister extends BaseConfigRegister
{
    private static volatile ConfigRegister _INSTANCE;

    public static ConfigRegister getInstance()
    {
        return CodeGeneric.getInstance(ConfigRegister.class);
    }

    private static final Class<?>[] CONFIG_CLASSES =
    {
        WorldCacheConfig.class,
        LogFileConfig.class,
        PlayerConfig.class,
        MainWindowTitleConfig.class,
        BlockWorldGeneratorConfig.class,
        ZombieDropConfig.class,
        SkeletonDropConfig.class,
        DebugConfig.class
    };

    @Override
    protected Class<?>[] getConfigClasses()
    {
        return CONFIG_CLASSES;
    }
}
