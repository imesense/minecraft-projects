package org.imesense.dynamicspawncontrol.core.mixinconfig;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

public class MixinConfigManager
{
    private static volatile boolean parasitesEnabled = true;

    @Getter
    private static volatile List<String> parasitesBlacklist = Collections.emptyList();

    public static void loadConfig()
    {
        parasitesBlacklist = MixinConfig.getParasitesBlacklist();
        parasitesEnabled = MixinConfig.isParasitesMixinEnabled();
    }

    public static boolean isParasitesMixinEnabled()
    {
        return parasitesEnabled;
    }

    public static void refreshConfig()
    {
        loadConfig();
    }
}