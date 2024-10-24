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
    public CfgClassAbstract(String nameConfigFile, final Boolean IS_CONFIG_FOLDER)
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
     * @return
     */
    protected String constructPathToDirectory(final Boolean IS_CONFIG_FOLDER)
    {
        return DynamicSpawnControl.getGlobalPathToConfigs().getPath() + File.separator +
                ProjectStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY + File.separator +
                (IS_CONFIG_FOLDER ? ProjectStructure.STRUCT_FILES_DIRS.NAME_DIR_CONFIGS :
                        ProjectStructure.STRUCT_FILES_DIRS.NAME_DIR_PLUGINS) + File.separator;
    }
}
