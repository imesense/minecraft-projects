package org.imesense.dynamicspawncontrol.core.register.pluginconfig;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseregister.BaseConfigRegister;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
public final class PluginConfigRegister extends BaseConfigRegister
{
    private static volatile PluginConfigRegister _INSTANCE;

    public static PluginConfigRegister getInstance()
    {
        return CodeGeneric.getInstance(PluginConfigRegister.class);
    }

    public PluginConfigRegister()
    {

    }

    private static final Class<?>[] PLUGIN_CONFIG_CLASSES =
    {

    };

    @Override
    protected Class<?>[] getConfigClasses()
    {
        return PLUGIN_CONFIG_CLASSES;
    }
}