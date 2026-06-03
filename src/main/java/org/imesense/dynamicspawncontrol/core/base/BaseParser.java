package org.imesense.dynamicspawncontrol.core.base;

import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@TODO(value = "Merge 'base' files into 'core/base/...' and fix the class diagram in version 0.2", showOnce = false, priority = TODO.TodoPriority.HIGH)
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
        LogManager.info("Reloading config for: " + this.nameFile);

        EraseData eraseData = this::eraseData;
        eraseData.eraseData();

        ConfigLoader configLoader = this::loadConfig;
        configLoader.load(false);

        LogManager.info("Config reloaded successfully for: " + this.nameFile);
    }

    public abstract void eraseData();

    public abstract void loadConfig(boolean init);

    public void createNewConfigFile(final File FILE)
    {
        try
        {
            File file = FILE.getParentFile();

            if (!file.exists())
            {
                LogManager.info("Directory does not exist, creating: " + file.getAbsolutePath());

                if (!file.mkdirs())
                {
                    throw new IOException("Failed to create directory: " + file.getAbsolutePath());
                }
            }

            try (FileWriter fileWriter = new FileWriter(FILE))
            {
                fileWriter.write("[]");
                fileWriter.flush();
            }
        }
        catch (IOException exception)
        {
            LogManager.info("Error creating new config file: " + exception.getMessage());
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
