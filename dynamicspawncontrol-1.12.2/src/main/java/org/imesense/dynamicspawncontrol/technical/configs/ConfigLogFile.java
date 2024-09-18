package org.imesense.dynamicspawncontrol.technical.configs;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
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
    public static int LogMaxLines = 32767;

    /**
     *
     */
    public ConfigLogFile(final String nameClass)
    {

    }

    /**
     *
     * @param event
     * @param nameClass
     */
    @Override
    public void init(FMLPreInitializationEvent event, final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);

        ClientProxy.ConfigLogFile = new Configuration(new File(DynamicSpawnControl.getGlobalPathToConfigs().getPath() +
                File.separator + DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIRECTORY + File.separator +
                DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIR_CONFIGS,
                "log" + DynamicSpawnControl.STRUCT_FILES_EXTENSION.CONFIG_FILE_EXTENSION));

        this.read();
    }

    /**
     *
     * @param configuration
     */
    @Override
    public void readProperties(Configuration configuration)
    {
        LogMaxLines =
                configuration.getInt
                        ("Log maximum lines", "log_file_settings", LogMaxLines, 5, 32767,
                                "The parameter is responsible for the maximum number of lines in the log");
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