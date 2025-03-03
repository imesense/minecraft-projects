package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.api.AbstractConceptParser;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheEntityStorage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ParserEventCacheSettings extends AbstractConceptParser
{
    public ParserEventCacheSettings(final String NAME_FILE)
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        this.nameFile = NAME_FILE;
    }

    @Override
    public void loadConfig(boolean initialization)
    {
        File file = getConfigFile(initialization,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_CACHE, this.nameFile);

        if (!file.exists())
        {
            Log.writeDataToLogFile(0, "Config file not found, creating new: " + file);
            createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(fileReader, JsonArray.class);

            if (jsonArray == null)
            {
                CodeGeneric.logAndThrow("Script does not contain key 'data'.");
            }

            List<CacheEntityStorage.EntityData> entitiesList = new ArrayList<>();

            assert jsonArray != null;

            for (JsonElement jsonElement : jsonArray)
            {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String entityName = jsonObject.get("entity").getAsString();

                int maxCount = jsonObject.get("max_count").getAsInt();

                String[] parts = entityName.split(":");
                ResourceLocation resourceLocation =
                        new ResourceLocation(parts.length > 1 ? parts[0] : "minecraft", parts.length > 1 ? parts[1] : parts[0]);

                entitiesList.add(new CacheEntityStorage.EntityData(resourceLocation, maxCount));
                Log.writeDataToLogFile(0, "Entity Loaded: " + resourceLocation + " Max Count: " + maxCount);
            }

            CacheEntityStorage.Instance.EntityCacheMobs = entitiesList;
            Log.writeDataToLogFile(0, "Loaded script with data: " + entitiesList);

        }
        catch (IOException | JsonSyntaxException exception)
        {
            CodeGeneric.logAndThrow("Error loading script file: " + exception.getMessage(), exception);
        }
    }

    @Override
    public void eraseData()
    {
        CacheEntityStorage.Instance.EntityCacheMobs.clear();
    }
}