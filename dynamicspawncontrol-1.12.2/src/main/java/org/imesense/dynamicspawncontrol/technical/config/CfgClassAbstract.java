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
    public CfgClassAbstract(String nameConfigFile)
    {
        this.nameConfig = this.constructPathToDirectory() + nameConfigFile;
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
    protected String constructPathToDirectory()
    {
        return DynamicSpawnControl.getGlobalPathToConfigs().getPath() + File.separator +
                DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIRECTORY + File.separator +
                DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIR_CONFIGS + File.separator;
    }
}
