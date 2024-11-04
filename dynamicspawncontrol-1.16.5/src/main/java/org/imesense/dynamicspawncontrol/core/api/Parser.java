package org.imesense.dynamicspawncontrol.core.api;

import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;

import java.io.File;

public abstract class Parser
{
    public abstract void reloadConfig();

    public abstract void loadConfig(boolean init);

    protected File constructPathToDirectory(boolean initialization, final String DIRECTORY, final String FILE_NAME)
    {
        return initialization ? new File(DynamicSpawnControl.getGlobalPathToConfigs().getPath() + File.separator +
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY + File.separator +
                DIRECTORY, FILE_NAME) :
                new File("config/DynamicSpawnControl/" + DIRECTORY + "/" + FILE_NAME);
    }
}
