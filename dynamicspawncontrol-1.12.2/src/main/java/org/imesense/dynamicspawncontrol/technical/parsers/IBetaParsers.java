package org.imesense.dynamicspawncontrol.technical.parsers;

import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.ProjectStructure;

import java.io.File;

/**
 *
 */
public interface IBetaParsers
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
     * @param directory
     * @param filename
     * @return
     */
    default File getConfigFile(boolean initialization, final String directory, final String filename)
    {
        return initialization ? new File(DynamicSpawnControl.getGlobalPathToConfigs().getPath() + File.separator +
                ProjectStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY + File.separator + directory, filename) :
                    new File("config/DynamicSpawnControl/" + directory + "/" + filename);
    }
}
