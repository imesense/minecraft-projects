package org.imesense.dynamicspawncontrol.technical.configs;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.proxy.ClientProxy;

import java.io.File;

/**
 *
 */
public final class ConfigLogFile implements IConfig
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public static int LogMaxLines = 32767;

    /**
     *
     */
    public ConfigLogFile()
    {
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;

        CodeGenericUtils.printInitClassToLog(ConfigLogFile.class);
    }

    /**
     *
     * @param event
     */
    @Override
    public void init(FMLPreInitializationEvent event)
    {
        ClientProxy.ConfigLogFile = this.createConfiguration("log");

        this.read();
    }

    /**
     *
     * @param configuration
     */
    @Override
    public void readProperties(Configuration configuration)
    {
        LogMaxLines = this.getConfigValueI(
                configuration,
                "Log maximum lines",
                "log_file_settings",
                LogMaxLines,
                5, 32767,
                "The parameter is responsible for the maximum number of lines in the log"
        );
    }

    /**
     *
     */
    @Override
    public void read()
    {
        Configuration configuration = ClientProxy.ConfigLogFile;

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
