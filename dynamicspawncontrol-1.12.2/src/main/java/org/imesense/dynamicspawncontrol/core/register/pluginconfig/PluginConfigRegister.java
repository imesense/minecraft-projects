package org.imesense.dynamicspawncontrol.core.register.pluginconfig;

import org.imesense.dynamicspawncontrol.core.baseregister.BaseConfigRegister;
import org.imesense.dynamicspawncontrol.core.pluginconfig.darkness.PluginDarknessConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

public final class PluginConfigRegister extends BaseConfigRegister
{
    private static volatile PluginConfigRegister _INSTANCE;

    public static PluginConfigRegister getInstance()
    {
        return CodeGeneric.getInstance(PluginConfigRegister.class);
    }

    private static final Class<?>[] PLUGIN_CONFIG_CLASSES =
    {
        PluginDarknessConfig.class
    };

    @Override
    protected Class<?>[] getConfigClasses()
    {
        return PLUGIN_CONFIG_CLASSES;
    }
}