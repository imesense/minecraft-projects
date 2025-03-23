package org.imesense.dynamicspawncontrol.core.baseparser;

import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class BaseParser
{
    protected String nameFile = null;

    public BaseParser()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    @FunctionalInterface
    public interface ConfigLoader
    {
        void load(boolean init);
    }

    @FunctionalInterface
    public interface EraseData
    {
        void eraseData();
    }

    public void reloadConfig()
    {
        Log.writeDataToLogFile(0, "Reloading config for: " + this.nameFile);

        EraseData data = this::eraseData;
        data.eraseData();

        ConfigLoader loader = this::loadConfig;
        loader.load(false);

        Log.writeDataToLogFile(0, "Config reloaded successfully for: " + this.nameFile);
    }

    public abstract void eraseData();

    public abstract void loadConfig(boolean init);

    public void createNewConfigFile(final File FILE)
    {
        try
        {
            File parentDir = FILE.getParentFile();

            if (!parentDir.exists())
            {
                Log.writeDataToLogFile(0, "Directory does not exist, creating: " + parentDir.getAbsolutePath());

                if (!parentDir.mkdirs())
                {
                    throw new IOException("Failed to create directory: " + parentDir.getAbsolutePath());
                }
            }

            try (FileWriter writer = new FileWriter(FILE))
            {
                writer.write("[]");
                writer.flush();
            }
        }
        catch (IOException exception)
        {
            Log.writeDataToLogFile(0, "Error creating new config file: " + exception.getMessage());
            throw new RuntimeException("Failed to create new config file", exception);
        }
    }

    protected File getConfigFile(boolean initialization, final String DIRECTORY, final String FILE_NAME)
    {
        return initialization ? new File(DynamicSpawnControl.getGlobalPathToConfigs().getPath() + File.separator +
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY + File.separator + DIRECTORY, FILE_NAME) :
                    new File("config/dynamicspawncontrol/" + DIRECTORY + "/" + FILE_NAME);
    }
}
