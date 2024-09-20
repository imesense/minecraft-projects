package org.imesense.dynamicspawncontrol.technical.configs;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;

import java.io.File;

/**
 *
 */
public interface IConfig
{
    /**
     *
     * @param event
     */
    void init(FMLPreInitializationEvent event);

    /**
     *
     * @param configuration
     */
    void readProperties(Configuration configuration);

    /**
     *
     */
    void read();

    /**
     *
     * @param fileName
     * @return
     */
    default Configuration createConfiguration(final String fileName)
    {
        return new Configuration(new File(DynamicSpawnControl.getGlobalPathToConfigs().getPath() +
                File.separator + DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIRECTORY + File.separator +
                DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIR_CONFIGS, fileName +
                DynamicSpawnControl.STRUCT_FILES_EXTENSION.CONFIG_FILE_EXTENSION));
    }

    /**
     *
     * @param config
     * @param name
     * @param category
     * @param defaultValue
     * @param minValue
     * @param maxValue
     * @param comment
     * @return
     */
    default float getConfigValueF(Configuration config, String name, String category, Float defaultValue, Float minValue, Float maxValue, String comment)
    {
        return config.getFloat(name, category, defaultValue, minValue, maxValue, comment);
    }

    /**
     *
     * @param config
     * @param name
     * @param category
     * @param defaultValue
     * @param comment
     * @return
     */
    default boolean getConfigValueB(Configuration config, String name, String category, Boolean defaultValue, String comment)
    {
        return config.getBoolean(name, category, defaultValue, comment);
    }

    /**
     *
     * @param config
     * @param name
     * @param category
     * @param defaultValue
     * @param comment
     * @return
     */
    default String getConfigValueS(Configuration config, String name, String category, String defaultValue, String comment)
    {
        return config.getString(name, category, defaultValue, comment);
    }

    /**
     *
     * @param config
     * @param name
     * @param category
     * @param defaultValue
     * @param minValue
     * @param maxValue
     * @param comment
     * @return
     */
    default int getConfigValueI(Configuration config, String name, String category, Integer defaultValue, Integer minValue, Integer maxValue, String comment)
    {
        return config.getInt(name, category, defaultValue, minValue, maxValue, comment);
    }

    /**
     *
     * @param config
     * @param name
     * @param category
     * @param defaultValue
     * @param comment
     * @return
     */
    default int[] getIntListConfigValue(Configuration config, String name, String category, int[] defaultValue, String comment)
    {
        return config.get(name, category, defaultValue, comment).getIntList();
    }

    /**
     *
     * @param config
     * @param name
     * @param category
     * @param defaultValue
     * @param comment
     * @return
     */
    default double[] getDoubleListConfigValue(Configuration config, String name, String category, double[] defaultValue, String comment)
    {
        return config.get(name, category, defaultValue, comment).getDoubleList();
    }

    /**
     *
     * @param config
     * @param name
     * @param category
     * @param defaultValue
     * @param comment
     * @return
     */
    default String[] getStringListConfigValue(Configuration config, String name, String category, String[] defaultValue, String comment)
    {
        return config.get(name, category, defaultValue, comment).getStringList();
    }
}
