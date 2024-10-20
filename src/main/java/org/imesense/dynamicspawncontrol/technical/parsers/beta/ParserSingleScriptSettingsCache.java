package org.imesense.dynamicspawncontrol.technical.parsers.beta;

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.EnumSingleScripts;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.parsers.IBetaParsers;
import org.imesense.dynamicspawncontrol.technical.worldcache.CacheStorage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class ParserSingleScriptSettingsCache implements IBetaParsers
{
    /**
     *
     */
    public ParserSingleScriptSettingsCache()
    {
        CodeGenericUtils.printInitClassToLog(this.getClass());
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
     * @param initialization
     */
    public void loadConfig(boolean initialization)
    {
        File file = getConfigFile(initialization, ProjectStructure.STRUCT_FILES_DIRS.NAME_DIR_CACHE,
                EnumSingleScripts.SCRIPT_CACHE_MOBS.getKeyword());

        if (!file.exists())
        {
            try
            {
                File parentDir = file.getParentFile();
                if (!parentDir.exists() && !parentDir.mkdirs())
                {
                    Log.writeDataToLogFile(0, "Failed to create directories for script file: " + parentDir.getAbsolutePath());
                    throw new RuntimeException("Failed to create directories for script file: " + parentDir.getAbsolutePath());
                }

                if (file.createNewFile())
                {
                    Log.writeDataToLogFile(0, "Created new script file: " + file.getAbsolutePath());
                    try (FileWriter writer = new FileWriter(file))
                    {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();

                        JsonArray initialData = getJsonElements();

                        gson.toJson(initialData, writer);
                        Log.writeDataToLogFile(0, "Initialized new script file with default JSON data: " + file.getAbsolutePath());
                    }
                }
                else
                {
                    Log.writeDataToLogFile(0, "Failed to create new script file: " + file.getAbsolutePath());
                    throw new RuntimeException();
                }
            }
            catch (IOException exception)
            {
                Log.writeDataToLogFile(0, "Error creating new script file: " + exception.getMessage());
                throw new RuntimeException(exception);
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

                    Log.writeDataToLogFile(0, "Entity Loaded: " + entityResourceLocation + " Max Count: " + maxCount);

                    entitiesList.add(new CacheStorage.EntityData(entityResourceLocation, maxCount));
                }

                CacheStorage.instance.EntityCacheMobs = entitiesList;
                Log.writeDataToLogFile(0, "Loaded script with data: " + entitiesList);
            }
            else
            {
                Log.writeDataToLogFile(0, "Script does not contain key 'data'.");
                throw new RuntimeException();
            }
        }
        catch (JsonSyntaxException exception)
        {
            Log.writeDataToLogFile(0, "JSON syntax error in configuration file: " + exception.getMessage());
            throw new RuntimeException(exception);
        }
        catch (IOException exception)
        {
            Log.writeDataToLogFile(0, "Error loading script file: " + exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    /**
     *
     * @return
     */
    private static JsonArray getJsonElements()
    {
        JsonArray initialData = new JsonArray();

        JsonObject cow = new JsonObject();
        cow.addProperty("entity", "minecraft:cow");
        cow.addProperty("max_count", 10);
        initialData.add(cow);

        JsonObject pig = new JsonObject();
        pig.addProperty("entity", "minecraft:pig");
        pig.addProperty("max_count", 8);
        initialData.add(pig);

        JsonObject chicken = new JsonObject();
        chicken.addProperty("entity", "minecraft:chicken");
        chicken.addProperty("max_count", 6);
        initialData.add(chicken);

        JsonObject sheep = new JsonObject();
        sheep.addProperty("entity", "minecraft:sheep");
        sheep.addProperty("max_count", 4);
        initialData.add(sheep);

        JsonObject squid = new JsonObject();
        squid.addProperty("entity", "minecraft:squid");
        squid.addProperty("max_count", 5);
        initialData.add(squid);

        return initialData;
    }
}
