package org.imesense.dynamicspawncontrol.technical.worldcache;

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.technical.customlibrary.AuxFunctions;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CacheConfig
{
    /**
     *
     * @param nameClass
     */
    public CacheConfig(final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);
    }

    /**
     *
     */
    public void reloadConfig()
    {
        loadConfig(false);
    }

    /**
     *
     */
    private static CacheConfig classInstance;

    /**
     *
     * @return
     */
    public static CacheConfig getClassInstance()
    {
        return classInstance;
    }

    /**
     *
     * @param initialization
     */
    public void loadConfig(boolean initialization)
    {
        classInstance = this;

        File file = (initialization)
                ? new File(DynamicSpawnControl.getGlobalPathToConfigs().getPath() + File.separator +
                DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIRECTORY +
                File.separator + DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIR_CACHE,
                AuxFunctions.NameSingleScript.SCRIPT_CACHE_MOBS.getKeyword())
                : new File("config/DynamicsSpawnControl/cache/" +
                AuxFunctions.NameSingleScript.SCRIPT_CACHE_MOBS.getKeyword());

        if (!file.exists())
        {
            try
            {
                File parentDir = file.getParentFile();
                if (!parentDir.exists() && !parentDir.mkdirs())
                {
                    Log.writeDataToLogFile(Log.TypeLog[0], "Failed to create directories for script file: " + parentDir.getAbsolutePath());
                    throw new RuntimeException("Failed to create directories for script file: " + parentDir.getAbsolutePath());
                }

                if (file.createNewFile())
                {
                    Log.writeDataToLogFile(Log.TypeLog[0], "Created new script file: " + file.getAbsolutePath());
                    try (FileWriter writer = new FileWriter(file))
                    {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        JsonArray emptyJsonArray = new JsonArray();
                        gson.toJson(emptyJsonArray, writer);
                        Log.writeDataToLogFile(Log.TypeLog[0], "Initialized new script file with empty JSON array: " + file.getAbsolutePath());
                    }
                }
                else
                {
                    Log.writeDataToLogFile(Log.TypeLog[0], "Failed to create new script file: " + file.getAbsolutePath());
                    throw new RuntimeException();
                }
            }
            catch (IOException e)
            {
                Log.writeDataToLogFile(Log.TypeLog[0], "Error creating new script file: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }

        try (FileReader reader = new FileReader(file))
        {
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

            if (jsonArray != null)
            {
                List<CacheStorage.EntityData> entitiesList = new ArrayList<>();

                for (JsonElement element : jsonArray)
                {
                    JsonObject dataObject = element.getAsJsonObject();
                    String entityName = dataObject.get("entity").getAsString();
                    int maxCount = dataObject.get("max_count").getAsInt();

                    String[] parts = entityName.split(":");
                    String domain = parts.length > 1 ? parts[0] : "minecraft";
                    String path = parts.length > 1 ? parts[1] : parts[0];
                    ResourceLocation entityResourceLocation = new ResourceLocation(domain, path);

                    Log.writeDataToLogFile(Log.TypeLog[0], "Entity Loaded: " + entityResourceLocation + " Max Count: " + maxCount);

                    entitiesList.add(new CacheStorage.EntityData(entityResourceLocation, maxCount));
                }

                CacheStorage.getInstance().EntityCacheMobs = entitiesList;
                Log.writeDataToLogFile(Log.TypeLog[0], "Loaded script with data: " + entitiesList);
            }
            else
            {
                Log.writeDataToLogFile(Log.TypeLog[0], "Script does not contain key 'data'.");
                throw new RuntimeException();
            }
        }
        catch (JsonSyntaxException exception)
        {
            Log.writeDataToLogFile(Log.TypeLog[0], "JSON syntax error in configuration file: " + exception.getMessage());
            throw new RuntimeException(exception);
        }
        catch (IOException exception)
        {
            Log.writeDataToLogFile(Log.TypeLog[0], "Error loading script file: " + exception.getMessage());
            throw new RuntimeException(exception);
        }
    }
}
