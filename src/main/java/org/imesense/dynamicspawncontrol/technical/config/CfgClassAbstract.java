package org.imesense.dynamicspawncontrol.technical.config;

import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.ProjectStructure;

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
                ProjectStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY + File.separator +
                (isConfigFolder ? ProjectStructure.STRUCT_FILES_DIRS.NAME_DIR_CONFIGS :
                        ProjectStructure.STRUCT_FILES_DIRS.NAME_DIR_PLUGINS) + File.separator;
    }
}
