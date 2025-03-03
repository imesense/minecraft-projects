package org.imesense.dynamicspawncontrol.core.api;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 */
public abstract class AbstractConceptParser
{
    /**
     *
     */
    protected String nameFile = null;

    /**
     *
     */
    public abstract void reloadConfig();

    /**
     *
     * @param init
     */
    public abstract void loadConfig(boolean init);

    /**
     *
     * @param FILE
     */
    public abstract void createNewConfigFile(final File FILE);

    /**
     *
     * @param initialization
     * @param DIRECTORY
     * @param FILE_NAME
     * @return
     */
    protected File getConfigFile(boolean initialization, final String DIRECTORY, final String FILE_NAME)
    {
        return initialization ? new File(DynamicSpawnControl.getGlobalPathToConfigs().getPath() + File.separator +
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY + File.separator + DIRECTORY, FILE_NAME) :
                    new File("config/DynamicSpawnControl/" + DIRECTORY + "/" + FILE_NAME);
    }
}
