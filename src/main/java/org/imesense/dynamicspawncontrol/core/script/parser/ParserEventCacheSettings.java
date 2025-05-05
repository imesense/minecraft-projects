package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheEntityStorage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@InitLog
public final class ParserEventCacheSettings extends BaseParser
{
    public ParserEventCacheSettings(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;
    }

    @Override
    public void loadConfig(boolean init)
    {
        Log.writeDataToLogFile(0, "Reading the config for the first time: " + init + " " + "file: " + this.nameFile);

        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_CACHE, this.nameFile);

        if (!file.exists())
        {
            Log.writeDataToLogFile(0, "Config file not found, creating new: " + file);
            this.createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(fileReader, JsonArray.class);

            if (jsonArray == null)
            {
                throw new RuntimeException("Script does not contain key 'data'.");
            }

            List<CacheEntityStorage.EntityData> entitiesList = new ArrayList<>();

            for (JsonElement jsonElement : jsonArray)
            {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonObject dataObject = jsonObject.getAsJsonObject("data");

                if (dataObject == null)
                {
                    throw new RuntimeException("Script does not contain key 'data'.");
                }

                String entityName = dataObject.get("entity").getAsString();
                String instanceofStr = dataObject.get("instanceof").getAsString();
                Boolean perPlayer = dataObject.get("per_player").getAsBoolean();
                Boolean perChunk = dataObject.get("per_chunk").getAsBoolean();
                Integer maxEntityCount = dataObject.get("max_entity_count").getAsInt();
                String resultStr = dataObject.get("result").getAsString();

                Event.Result result;

                try
                {
                    result = Event.Result.valueOf(resultStr.toUpperCase());
                }
                catch (IllegalArgumentException exception)
                {
                    throw new RuntimeException("Invalid value for 'result': " + resultStr);
                }

                String[] parts = entityName.split(":");

                ResourceLocation resourceLocation =
                        new ResourceLocation(parts.length > 1 ? parts[0] : "minecraft", parts.length > 1 ? parts[1] : parts[0]);

                CacheEntityStorage.EntityData entityData = new CacheEntityStorage.EntityData();
                entityData.entity = resourceLocation;

                Class<?> checkInstanceof;

                try
                {
                    try
                    {
                        checkInstanceof = Class.forName("net.minecraft.entity.monster." + instanceofStr);
                    }
                    catch (ClassNotFoundException exception1)
                    {
                        try
                        {
                            checkInstanceof = Class.forName("net.minecraft.entity." + instanceofStr);
                        }
                        catch (ClassNotFoundException exception2)
                        {
                            checkInstanceof = Class.forName(instanceofStr);
                        }
                    }
                }
                catch (ClassNotFoundException exception)
                {
                    throw new RuntimeException("Invalid class for 'instanceof': " + instanceofStr +
                            ". Valid examples: 'EntityZombie', 'net.minecraft.entity.monster.EntityZombie'", exception);
                }

                entityData.check_instanceof = checkInstanceof;

                Log.writeDataToLogFile(0, "Entity " + entityName + " checkInstanceof: " + entityData.check_instanceof);

                entityData.per_player = perPlayer;
                entityData.per_chunk = perChunk;
                entityData.max_entity_count = maxEntityCount;
                entityData.result = result;

                entitiesList.add(entityData);

                Log.writeDataToLogFile(0, "Entity Loaded: " +
                        resourceLocation + " Per Player: " + perPlayer + " Per Chunk: " +
                        perChunk + " Max Count: " + maxEntityCount + " Result: " + result);
            }

            CacheEntityStorage.getInstance().entityData = entitiesList;
            Log.writeDataToLogFile(0, "Loaded script with data: " + entitiesList);
        }
        catch (IOException | JsonSyntaxException exception)
        {
            throw new RuntimeException("Error loading script file: " + exception.getMessage(), exception);
        }
        catch (NullPointerException exception)
        {
            throw new RuntimeException("Missing required parameter in script file: " + exception.getMessage(), exception);
        }
    }

    @Override
    public void eraseData()
    {
        CacheEntityStorage.getInstance().entityData.clear();
    }
}