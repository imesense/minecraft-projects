package org.imesense.dynamicspawncontrol.technical.configs;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.proxy.ClientProxy;

/**
 *
 */
public final class ConfigWindowTitle implements IConfig
{
    /**
     *
     */
    public static String WindowTitle =
            String.format("Minecraft: %s + %s", DynamicSpawnControl.STRUCT_INFO_MOD.VERSION, DynamicSpawnControl.STRUCT_INFO_MOD.NAME);

    public ConfigWindowTitle(final String nameClass)
    {

    }

    /**
     *
     * @param event
     */
    @Override
    public void init(FMLPreInitializationEvent event)
    {
        ClientProxy.ConfigWindowTitle = this.createConfiguration("name_window");

        this.read();
    }

    /**
     *
     * @param configuration
     */
    @Override
    public void readProperties(Configuration configuration)
    {
        WindowTitle = this.getConfigValueS(
                configuration,
                "Window title",
                "window", WindowTitle,
                "The title of the Minecraft window");
    }

    /**
     *
     */
    @Override
    public void read()
    {
        Configuration configuration = ClientProxy.ConfigWindowTitle;

        try
        {
            configuration.load();

            this.readProperties(configuration);
        }
        finally
        {
            if (configuration.hasChanged())
            {
                configuration.save();
            }
        }
    }
}
