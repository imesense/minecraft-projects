package org.imesense.dynamicspawncontrol.technical.configs;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.proxy.ClientProxy;

import java.io.File;

public final class SettingsLogFile implements IConfig
{
    public static int LogMaxLines = 32767;

    @Override
    public void init(FMLPreInitializationEvent event, String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);

        ClientProxy.ConfigLogFile = new Configuration(new File(DynamicSpawnControl.getGlobalPathToConfigs().getPath() +
                File.separator + DynamicSpawnControl.NAME_DIRECTORY + File.separator + "configs", "log" + DynamicSpawnControl.CONFIG_FILE_EXTENSION));

        read();
    }

    @Override
    public void readProperties(Configuration configuration)
    {
        LogMaxLines =
                configuration.getInt
                        ("Log maximum lines", "log_file_settings", LogMaxLines, 5, 32767,
                                "The parameter is responsible for the maximum number of lines in the log");
    }

    @Override
    public void read()
    {
        Configuration configuration = ClientProxy.ConfigLogFile;

        try
        {
            configuration.load();

            readProperties(configuration);
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
