package org.imesense.dynamicspawncontrol.technical.configs;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ConfigManager
{
    /**
     *
     */
    private static final List<IConfig> settingList = new ArrayList<>();

    /**
     *
     */
    static
    {
        settingList.add(new SettingsLogFile("SettingsLogFile"));
    }

    /**
     *
     */
    public ConfigManager(final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);
    }

    /**
     *
     * @param event
     */
    public static void init(FMLPreInitializationEvent event)
    {
        for (IConfig config : settingList)
        {
            config.init(event, config.getClass().getSimpleName());
        }
    }
}
