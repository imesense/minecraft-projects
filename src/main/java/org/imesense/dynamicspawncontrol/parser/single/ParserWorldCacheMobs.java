package org.imesense.dynamicspawncontrol.parser.single;

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.api.AbstractConceptParser;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheStorage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class ParserWorldCacheMobs extends AbstractConceptParser
{
    /**
     *
     */
    public ParserWorldCacheMobs(final String NAME_FILE)
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        this.nameFile = NAME_FILE;
    }

    /**
     *
     */
    @Override
    public void reloadConfig()
    {
        this.loadConfig(false);
    }

    /**
     *
     * @param initialization
     */
    @Override
    public void loadConfig(boolean initialization)
    {
        File file = getConfigFile(initialization,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_CACHE, this.nameFile);

        if (!file.exists())
        {
            createNewConfigFile(file);
        }

        try (FileReader fileReader = new FileReader(file))
        {
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(fileReader, JsonArray.class);

            if (jsonArray == null)
            {
                CodeGeneric.logAndThrow("Script does not contain key 'data'.");
            }

            List<CacheStorage.EntityData> entitiesList = new ArrayList<>();

            assert jsonArray != null;

            for (JsonElement jsonElement : jsonArray)
            {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String entityName = jsonObject.get("entity").getAsString();

                int maxCount = jsonObject.get("max_count").getAsInt();

                String[] parts = entityName.split(":");
                ResourceLocation resourceLocation =
                        new ResourceLocation(parts.length > 1 ? parts[0] : "minecraft", parts.length > 1 ? parts[1] : parts[0]);

                entitiesList.add(new CacheStorage.EntityData(resourceLocation, maxCount));
                Log.writeDataToLogFile(0, "Entity Loaded: " + resourceLocation + " Max Count: " + maxCount);
            }

            CacheStorage.Instance.EntityCacheMobs = entitiesList;
            Log.writeDataToLogFile(0, "Loaded script with data: " + entitiesList);

        }
        catch (IOException | JsonSyntaxException exception)
        {
            CodeGeneric.logAndThrow("Error loading script file: " + exception.getMessage(), exception);
        }
    }

    /**
     *
     * @param file
     */
    private void createNewConfigFile(File file)
    {
        try
        {
            File parentDir = file.getParentFile();

            if (!parentDir.exists() && !parentDir.mkdirs())
            {
                CodeGeneric.logAndThrow("Failed to create directories for script file: " + parentDir.getAbsolutePath());
            }

            if (file.createNewFile())
            {
                try (FileWriter fileWriter = new FileWriter(file))
                {
                    new GsonBuilder().setPrettyPrinting().create().toJson(getJsonElements(), fileWriter);
                    Log.writeDataToLogFile(0, "Initialized new script file with default JSON data: " + file.getAbsolutePath());
                }
            }
            else
            {
                CodeGeneric.logAndThrow("Failed to create new script file: " + file.getAbsolutePath());
            }

        }
        catch (IOException exception)
        {
            CodeGeneric.logAndThrow("Error creating new script file: " + exception.getMessage(), exception);
        }
    }

    /**
     *
     * @return
     */
    private static JsonArray getJsonElements()
    {
        JsonArray jsonArray = new JsonArray();

        jsonArray.add(createEntityJson("minecraft:cow", 10));
        jsonArray.add(createEntityJson("minecraft:pig", 8));
        jsonArray.add(createEntityJson("minecraft:chicken", 6));
        jsonArray.add(createEntityJson("minecraft:sheep", 4));
        jsonArray.add(createEntityJson("minecraft:squid", 5));

        return jsonArray;
    }

    /**
     *
     * @param entity
     * @param maxCount
     * @return
     */
    private static JsonObject createEntityJson(String entity, int maxCount)
    {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("entity", entity);
        jsonObject.addProperty("max_count", maxCount);

        return jsonObject;
    }
}

