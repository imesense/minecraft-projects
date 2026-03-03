package org.imesense.dynamicspawncontrol.core.baseconfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.JsonObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.logfile.Logger;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@TODO(value = "Merge 'base' files into 'core/base/...' and fix the class diagram in version 0.2", showOnce = false, priority = TODO.TodoPriority.HIGH)
public abstract class BaseJsonConfig
{
    protected final Gson gson;
    protected final String configPath;

    protected static final Map<Class<? extends BaseJsonConfig>, BaseJsonConfig> INSTANCES = new ConcurrentHashMap<>();

    public BaseJsonConfig(String nameConfigFile, boolean isConfigFolder)
    {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.configPath = constructPathToDirectory(isConfigFolder) + nameConfigFile;

        INSTANCES.put(this.getClass(), this);
    }

    public static <T extends BaseJsonConfig> T getInstance(Class<T> _class)
    {
        return _class.cast(INSTANCES.get(_class));
    }

    public void loadOrCreateConfig()
    {
        Path path = Paths.get(configPath);

        try
        {
            Files.createDirectories(path.getParent());

            if (Files.notExists(path))
            {
                saveConfig(createDefaultConfig());
            }
            else
            {
                loadConfig();
            }
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    protected void loadConfig()
    {
        try (FileReader fileReader = new FileReader(configPath))
        {
            JsonObject jsonObject = gson.fromJson(fileReader, JsonObject.class);
            applyConfig(jsonObject);
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    public void saveConfig(JsonObject jsonObject)
    {
        try (FileWriter fileWriter = new FileWriter(configPath))
        {
            gson.toJson(jsonObject, fileWriter);
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    protected String constructPathToDirectory(boolean isConfigFolder)
    {
        String path = DynamicSpawnControl.getGlobalPathToConfigs().getPath() + File.separator +
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY + File.separator +
                (isConfigFolder ? DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_CONFIGS :
                        DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_PLUGINS) + File.separator;

        Logger.write(0, "constructPathToDirectory: " + path);

        return path;
    }

    protected abstract JsonObject createDefaultConfig();

    protected abstract void applyConfig(JsonObject jsonObject);
}
