package org.imesense.dynamicspawncontrol.technical.config;

import org.imesense.dynamicspawncontrol.DynamicSpawnControl;

import java.io.File;

/**
 *
 */
public abstract class CfgClassAbstract
{
    /**
     *
     */
    protected String nameConfig;

    /**
     *
     * @param nameConfigFile
     */
    public CfgClassAbstract(String nameConfigFile, final Boolean isConfigFolder)
    {
        this.nameConfig = this.constructPathToDirectory(isConfigFolder) + nameConfigFile;
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
     * @return
     */
    protected String constructPathToDirectory(final Boolean isConfigFolder)
    {
        return DynamicSpawnControl.getGlobalPathToConfigs().getPath() + File.separator +
                DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIRECTORY + File.separator +
                (isConfigFolder ? DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIR_CONFIGS :
                        DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIR_PLUGINS) + File.separator;
    }
}
