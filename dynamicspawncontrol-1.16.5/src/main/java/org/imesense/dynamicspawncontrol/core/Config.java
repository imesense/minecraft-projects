package org.imesense.dynamicspawncontrol.core;

import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;

import java.io.File;

/**
 *
 */
public abstract class Config
{
    /**
     *
     */
    protected String nameConfig;

    /**
     *
     * @param nameConfigFile
     * @param IS_CONFIG_FOLDER
     */
    public Config(String nameConfigFile, final Boolean IS_CONFIG_FOLDER)
    {
        this.nameConfig = this.constructPathToDirectory(IS_CONFIG_FOLDER) + nameConfigFile;
    }

    /**
     *
     */
    public abstract void saveToFile();

    /**
     *
     */
    public abstract void loadFromFile();

    /**
     *
     * @param IS_CONFIG_FOLDER
     * @return
     */
    protected String constructPathToDirectory(final Boolean IS_CONFIG_FOLDER)
    {
        return DynamicSpawnControl.getGlobalPathToConfigs().getPath() + File.separator +
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY + File.separator +
                (IS_CONFIG_FOLDER ? DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_CONFIGS :
                        DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_PLUGINS) + File.separator;
    }
}
