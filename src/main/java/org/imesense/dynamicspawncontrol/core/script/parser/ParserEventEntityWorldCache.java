package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.script.storage.EntityWorldCache.storage.GeneralEntityWorldCache;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.storage.GeneralMobTaskManager;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public final class ParserEventEntityWorldCache extends BaseParser
{
    public ParserEventEntityWorldCache(final String NAME_FILE)
    {
        CodeGeneric.printInitClassToLog(this.getClass());
        this.nameFile = NAME_FILE;
    }

    @Override
    public void loadConfig(boolean init)
    {
        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_CACHE, this.nameFile);

        if (!file.exists())
        {
            Log.writeDataToLogFile(0, "Config file not found, creating new: " + file);
            createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(fileReader).getAsJsonArray();

            GeneralEntityWorldCache generalEntityWorldCache = GeneralEntityWorldCache.getInstance();

            for (JsonElement topLevelElement : jsonArray)
            {
                JsonObject topLevelObject = topLevelElement.getAsJsonObject();

                for (Map.Entry<String, JsonElement> entry : topLevelObject.entrySet())
                {
                    String entityName = entry.getKey();
                    JsonObject entityData = entry.getValue().getAsJsonObject();

                    GeneralEntityWorldCache.EntityWorldCacheData cacheData = new GeneralEntityWorldCache.EntityWorldCacheData();

                    cacheData.entity = entityName;
                    cacheData.per_player = entityData.get("per_player").getAsBoolean();
                    cacheData.per_chunk = entityData.get("per_chunk").getAsBoolean();
                    cacheData.max_entity_count = entityData.get("max_entity_count").getAsInt();
                    cacheData.min_entity_count = entityData.get("min_entity_count").getAsInt();
                    cacheData.result = String.valueOf(Event.Result.valueOf(entityData.get("result").getAsString().toUpperCase()));

                    generalEntityWorldCache.entityWorldCacheDataList.add(cacheData);

                    Log.writeDataToLogFile(0, "Added entity data to cache: " +
                            "Entity: " + cacheData.entity + ", " +
                            "Per Player: " + cacheData.per_player + ", " +
                            "Per Chunk: " + cacheData.per_chunk + ", " +
                            "Max Entity Count: " + cacheData.max_entity_count + ", " +
                            "Min Entity Count: " + cacheData.min_entity_count + ", " +
                            "Result: " + cacheData.result);
                }
            }

            Log.writeDataToLogFile(0, "Successfully loaded entity world cache from: " + file);
        }
        catch (FileNotFoundException exception)
        {
            Log.writeDataToLogFile(0, "Config file not found: " + file);
            throw new RuntimeException(exception);
        }
        catch (IOException exception)
        {
            Log.writeDataToLogFile(0, "Error reading config file: " + file);
            throw new RuntimeException(exception);
        }
        catch (Exception exception)
        {
            Log.writeDataToLogFile(0, "Error parsing config file: " + file);
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void eraseData()
    {
        GeneralEntityWorldCache.getInstance().entityWorldCacheDataList.clear();
    }
}