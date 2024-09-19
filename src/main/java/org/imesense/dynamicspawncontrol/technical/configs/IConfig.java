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
     * @param nameClass
     */
    void init(FMLPreInitializationEvent event, final String nameClass);

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
}
