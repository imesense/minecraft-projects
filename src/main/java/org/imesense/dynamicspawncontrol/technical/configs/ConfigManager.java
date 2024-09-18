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
        settingList.add(new SettingsGameDebugger("SettingsGameDebugger"));
        settingList.add(new SettingsWorldGenerator("SettingsWorldGenerator"));
        settingList.add(new SettingsRenderNight("SettingsRenderNight"));
        settingList.add(new SettingsWorldTime("SettingsWorldTime"));
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
