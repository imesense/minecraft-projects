package org.imesense.dynamicspawncontrol.core.parser;

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.LogFile;
import org.imesense.dynamicspawncontrol.core.api.Parser;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class ParserWorldCache extends Parser
{
    /**
     *
     */
    public ParserWorldCache()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    /**
     *
     * @return
     */
    private static JsonArray getJsonElements()
    {
        JsonArray jsonArray = new JsonArray();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("entity", "minecraft:cow");
        jsonObject.addProperty("max_count", 10);
        jsonArray.add(jsonObject);

        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("entity", "minecraft:pig");
        jsonObject1.addProperty("max_count", 8);
        jsonArray.add(jsonObject1);

        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("entity", "minecraft:chicken");
        jsonObject2.addProperty("max_count", 6);
        jsonArray.add(jsonObject2);

        JsonObject jsonObject3 = new JsonObject();
        jsonObject3.addProperty("entity", "minecraft:sheep");
        jsonObject3.addProperty("max_count", 4);
        jsonArray.add(jsonObject3);

        JsonObject jsonObject4 = new JsonObject();
        jsonObject4.addProperty("entity", "minecraft:squid");
        jsonObject4.addProperty("max_count", 5);
        jsonArray.add(jsonObject4);

        return jsonArray;
    }

    @Override
    public void reloadConfig()
    {
        loadConfig(false);
    }

    @Override
    public void loadConfig(boolean init)
    {
        File file = this.constructPathToDirectory(init, DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_CACHE,
                "cache_mobs" + DynamicSpawnControlStructure.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION);

        if (!file.exists())
        {
            try
            {
                File file1 = file.getParentFile();
                if (!file1.exists() && !file1.mkdirs())
                {
                    LogFile.writeDataToLogFile(0, "Failed to create directories for script file: " + file1.getAbsolutePath());
                    throw new RuntimeException("Failed to create directories for script file: " + file1.getAbsolutePath());
                }

                if (file.createNewFile())
                {
                    LogFile.writeDataToLogFile(0, "Created new script file: " + file.getAbsolutePath());
                    try (FileWriter fileWriter = new FileWriter(file))
                    {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();

                        JsonArray jsonArray = getJsonElements();

                        gson.toJson(jsonArray, fileWriter);
                        LogFile.writeDataToLogFile(0, "Initialized new script file with default JSON data: " + file.getAbsolutePath());
                    }
                }
                else
                {
                    LogFile.writeDataToLogFile(0, "Failed to create new script file: " + file.getAbsolutePath());
                    throw new RuntimeException();
                }
            }
            catch (IOException exception)
            {
                LogFile.writeDataToLogFile(0, "Error creating new script file: " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }

        try (FileReader fileReader = new FileReader(file))
        {
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(fileReader, JsonArray.class);

            if (jsonArray != null)
            {
                List<CacheStorage.EntityData> entitiesList = new ArrayList<>();

                for (JsonElement jsonElement : jsonArray)
                {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String entityName = jsonObject.get("entity").getAsString();
                    int maxCount = jsonObject.get("max_count").getAsInt();

                    String[] parts = entityName.split(":");
                    String domain = parts.length > 1 ? parts[0] : "minecraft";
                    String path = parts.length > 1 ? parts[1] : parts[0];
                    ResourceLocation resourceLocation = new ResourceLocation(domain, path);

                    LogFile.writeDataToLogFile(0, "Entity Loaded: " + resourceLocation + " Max Count: " + maxCount);

                    entitiesList.add(new CacheStorage.EntityData(resourceLocation, maxCount));
                }

                CacheStorage.Instance.EntityCacheMobs = entitiesList;
                LogFile.writeDataToLogFile(0, "Loaded script with data: " + entitiesList);
            }
            else
            {
                LogFile.writeDataToLogFile(0, "Script does not contain key 'data'.");
                throw new RuntimeException();
            }
        }
        catch (JsonSyntaxException exception)
        {
            LogFile.writeDataToLogFile(0, "JSON syntax error in configuration file: " + exception.getMessage());
            throw new RuntimeException(exception);
        }
        catch (IOException exception)
        {
            LogFile.writeDataToLogFile(0, "Error loading script file: " + exception.getMessage());
            throw new RuntimeException(exception);
        }
    }
}
