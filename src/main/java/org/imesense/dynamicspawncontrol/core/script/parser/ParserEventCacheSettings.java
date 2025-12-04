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
    private static final boolean DEBUG_AND_CHECK_SYNTAX = true;

    public ParserEventCacheSettings(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;

        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "ParserEventCacheSettings constructor called with file: " + NAME_FILE);
        }
    }

    @Override
    public void loadConfig(boolean init)
    {
        Log.write(0, "Reading the config for the first time: " + init + " " + "file: " + this.nameFile);

        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_CACHE, this.nameFile);

        if (!file.exists())
        {
            Log.write(0, "Config file not found, creating new: " + file);
            this.createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "Reading file: " + file.getAbsolutePath());
            }

            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(fileReader, JsonArray.class);

            if (jsonArray == null)
            {
                throw new RuntimeException("Script does not contain key 'data'.");
            }

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "JSON array size: " + jsonArray.size());
            }

            List<CacheEntityStorage.EntityData> entitiesList = new ArrayList<>();

            for (JsonElement jsonElement : jsonArray)
            {
                if (DEBUG_AND_CHECK_SYNTAX)
                {
                    Log.write(0, "Processing new JSON element");
                }

                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonObject dataObject = jsonObject.getAsJsonObject("data");

                if (dataObject == null)
                {
                    throw new RuntimeException("Script does not contain key 'data'.");
                }

                if (dataObject.has("instanceof") && dataObject.has("entity"))
                {
                    throw new RuntimeException("Script cannot contain both 'instanceof' and 'entity' keys. Use only one.");
                }

                String entityName = null;
                String instanceofStr = null;

                if (dataObject.has("instanceof"))
                {
                    instanceofStr = dataObject.get("instanceof").getAsString();

                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        Log.write(0, "Found instanceof: " + instanceofStr);
                    }
                }
                else if (dataObject.has("entity"))
                {
                    entityName = dataObject.get("entity").getAsString();

                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        Log.write(0, "Found entity: " + entityName);
                    }
                }
                else
                {
                    throw new RuntimeException("Script must contain either 'instanceof' or 'entity' key.");
                }

                Boolean isContinue = false;
                if (dataObject.has("continue"))
                {
                    isContinue = dataObject.get("continue").getAsBoolean();

                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        Log.write(0, "Found continue: " + isContinue);
                    }
                }

                Boolean perPlayer = false;
                Boolean perChunk = false;
                Integer maxEntityCount = 0;

                if (!isContinue)
                {
                    if (dataObject.has("per_player"))
                    {
                        perPlayer = dataObject.get("per_player").getAsBoolean();
                    }
                    else
                    {
                        throw new RuntimeException("Script must contain 'per_player' key when continue is false.");
                    }

                    if (dataObject.has("per_chunk"))
                    {
                        perChunk = dataObject.get("per_chunk").getAsBoolean();
                    }
                    else
                    {
                        throw new RuntimeException("Script must contain 'per_chunk' key when continue is false.");
                    }

                    if (dataObject.has("max_entity_count"))
                    {
                        maxEntityCount = dataObject.get("max_entity_count").getAsInt();
                    }
                    else
                    {
                        throw new RuntimeException("Script must contain 'max_entity_count' key when continue is false.");
                    }
                }
                else
                {
                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        Log.write(0, "Continue is true, skipping per_player, per_chunk, and max_entity_count parsing");
                    }
                }

                String resultStr = dataObject.get("result").getAsString();

                if (DEBUG_AND_CHECK_SYNTAX)
                {
                    if (!isContinue)
                    {
                        Log.write(0, "Parsed values - per_player: " + perPlayer +
                                ", per_chunk: " + perChunk +
                                ", max_entity_count: " + maxEntityCount +
                                ", continue: " + isContinue +
                                ", result: " + resultStr);
                    }
                    else
                    {
                        Log.write(0, "Parsed values - continue: " + isContinue +
                                ", result: " + resultStr + " (other parameters ignored)");
                    }
                }

                Event.Result result;

                try
                {
                    result = Event.Result.valueOf(resultStr.toUpperCase());
                }
                catch (IllegalArgumentException exception)
                {
                    throw new RuntimeException("Invalid value for 'result': " + resultStr);
                }

                CacheEntityStorage.EntityData entityData = new CacheEntityStorage.EntityData();

                if (instanceofStr != null)
                {
                    Class<?> checkInstanceof;

                    try
                    {
                        try
                        {
                            checkInstanceof = Class.forName("net.minecraft.entity.monster." + instanceofStr);

                            if (DEBUG_AND_CHECK_SYNTAX)
                            {
                                Log.write(0, "Trying net.minecraft.entity.monster package for: " + instanceofStr);
                            }
                        }
                        catch (ClassNotFoundException exception1)
                        {
                            try
                            {
                                checkInstanceof = Class.forName("net.minecraft.entity." + instanceofStr);

                                if (DEBUG_AND_CHECK_SYNTAX)
                                {
                                    Log.write(0, "Trying net.minecraft.entity package for: " + instanceofStr);
                                }
                            }
                            catch (ClassNotFoundException exception2)
                            {
                                checkInstanceof = Class.forName(instanceofStr);

                                if (DEBUG_AND_CHECK_SYNTAX)
                                {
                                    Log.write(0, "Trying full class name for: " + instanceofStr);
                                }
                            }
                        }
                    }
                    catch (ClassNotFoundException exception)
                    {
                        throw new RuntimeException("Invalid class for 'instanceof': " + instanceofStr +
                                ". Valid examples: 'EntityZombie' or 'EntityPigZombie', 'net.minecraft.entity.monster.EntityZombie', 'net.minecraft.entity.monster.EntityPigZombie", exception);
                    }

                    entityData.check_instanceof = checkInstanceof;
                    Log.write(0, "Entity checkInstanceof: " + entityData.check_instanceof);
                }
                else if (entityName != null)
                {
                    String[] parts = entityName.split(":");

                    ResourceLocation resourceLocation =
                            new ResourceLocation(parts.length > 1 ? parts[0] : "minecraft", parts.length > 1 ? parts[1] : parts[0]);

                    entityData.entity = resourceLocation;

                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        Log.write(0, "Created ResourceLocation: " + resourceLocation);
                    }
                }

                entityData.per_player = perPlayer;
                entityData.per_chunk = perChunk;
                entityData.max_entity_count = maxEntityCount;
                entityData.isContinue = isContinue;
                entityData.result = result;

                entitiesList.add(entityData);

                if (!isContinue)
                {
                    Log.write(0, "Entity Loaded: " +
                            (instanceofStr != null ? "Instanceof: " + instanceofStr : "Entity: " + entityName) +
                            " Per Player: " + perPlayer + " Per Chunk: " +
                            perChunk + " Max Count: " + maxEntityCount +
                            " Continue: " + isContinue + " Result: " + result);
                }
                else
                {
                    Log.write(0, "Entity Loaded (Continue mode): " +
                            (instanceofStr != null ? "Instanceof: " + instanceofStr : "Entity: " + entityName) +
                            " Continue: " + isContinue + " Result: " + result +
                            " (per_player, per_chunk, max_entity_count ignored)");
                }
            }

            CacheEntityStorage.getInstance().entityData = entitiesList;
            Log.write(0, "Loaded script with data: " + entitiesList);
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
        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "Clearing entity data cache");
        }

        CacheEntityStorage.getInstance().entityData.clear();
    }
}