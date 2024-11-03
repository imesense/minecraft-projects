package org.imesense.dynamicspawncontrol.technical.parser;

import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.ProjectStructure;

import java.io.File;

/**
 *
 */
public interface IBetaParser
{
    /**
     *
     */
    void reloadConfig();

    /**
     *
     * @param init
     */
    void loadConfig(boolean init);

    /**
     *
     * @param initialization
     * @param DIRECTORY
     * @param FILE_NAME
     * @return
     */
    default File getConfigFile(boolean initialization, final String DIRECTORY, final String FILE_NAME)
    {
        return initialization ? new File(DynamicSpawnControl.getGlobalPathToConfigs().getPath() + File.separator +
                ProjectStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY + File.separator + DIRECTORY, FILE_NAME) :
                    new File("config/DynamicSpawnControl/" + DIRECTORY + "/" + FILE_NAME);
    }
}
