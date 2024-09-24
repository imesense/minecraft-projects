package org.imesense.dynamicspawncontrol.technical.configs;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.proxy.ClientProxy;

/**
 *
 */
public final class ConfigDebugSingleEvents implements IConfig
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public ConfigDebugSingleEvents()
    {
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;

        CodeGenericUtils.printInitClassToLog(ConfigDebugSingleEvents.class);
    }

    /**
     *
     * @param event
     */
    @Override
    public void init(FMLPreInitializationEvent event)
    {
        ClientProxy.ConfigDebugSingleEvents = this.createConfiguration("debug_actions_single_events");

        this.read();
    }

    @Override
    public void readProperties(Configuration configuration)
    {

    }

    /**
     *
     */
    @Override
    public void read()
    {
        Configuration configuration = ClientProxy.ConfigDebugSingleEvents;

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
