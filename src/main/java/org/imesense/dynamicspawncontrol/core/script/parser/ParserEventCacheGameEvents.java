package org.imesense.dynamicspawncontrol.core.script.parser;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.io.File;

//[ реализовать ноду для совместимости с кешем
/*  {
    "event": {
      "day": 7,
      "repeat": true
    },
    "execute": {
      "id_dimension": 0,
      "entity": "minecraft:zombie",
      "max_entity_count": 150,
      "result": "deny"
    },
    "else": {
    // Сюда указываем ноду, которую отправляем в основной кеш для ограничения сущности
      //"id_dimension": 0,
      //"entity": "minecraft:zombie",
     // "max_entity_count": 18,
     // "result": "deny"
     "node_id": 000, -> отправляем в парсер ParserEventCacheSettings. Чтобы активировать его опцию без override события в этом парсере
    }
  }*/
 //]

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheGameEventStorage;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@InitLog
public class ParserEventCacheGameEvents extends BaseParser
{
    private static final boolean DEBUG_AND_CHECK_SYNTAX = true;

    public ParserEventCacheGameEvents(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;

        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "ParserEventCacheGameEvents constructor called with file: " + NAME_FILE);
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
                throw new RuntimeException("Script does not contain valid JSON array.");
            }

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "JSON array size: " + jsonArray.size());
            }

            List<CacheGameEventStorage.GameEventData> eventList = new ArrayList<>();

            for (JsonElement jsonElement : jsonArray)
            {
                if (DEBUG_AND_CHECK_SYNTAX)
                {
                    Log.write(0, "Processing new JSON element");
                }

                JsonObject jsonObject = jsonElement.getAsJsonObject();

                if (!jsonObject.has("event"))
                {
                    throw new RuntimeException("Script must contain 'event' key.");
                }

                JsonObject eventObject = jsonObject.getAsJsonObject("event");

                if (!eventObject.has("day"))
                {
                    throw new RuntimeException("Event must contain 'day' key.");
                }

                Integer day = eventObject.get("day").getAsInt();

                if (day < 0)
                {
                    throw new RuntimeException("Day value cannot be negative.");
                }

                Boolean repeat = false;
                if (eventObject.has("repeat"))
                {
                    repeat = eventObject.get("repeat").getAsBoolean();
                }

                if (DEBUG_AND_CHECK_SYNTAX)
                {
                    Log.write(0, "Parsed event - day: " + day + ", repeat: " + repeat);
                }

                if (!jsonObject.has("entity"))
                {
                    throw new RuntimeException("Script must contain 'entity' key.");
                }

                if (!jsonObject.has("max_entity_count"))
                {
                    throw new RuntimeException("Script must contain 'max_entity_count' key.");
                }

                if (!jsonObject.has("result"))
                {
                    throw new RuntimeException("Script must contain 'result' key.");
                }

                String entityStr = jsonObject.get("entity").getAsString();
                Integer maxEntityCount = jsonObject.get("max_entity_count").getAsInt();
                String resultStr = jsonObject.get("result").getAsString();

                Integer idDimension = null;
                if (jsonObject.has("id_dimension"))
                {
                    idDimension = jsonObject.get("id_dimension").getAsInt();
                }

                Event.Result result;
                try
                {
                    result = Event.Result.valueOf(resultStr.toUpperCase());
                }
                catch (IllegalArgumentException exception)
                {
                    throw new RuntimeException("Invalid value for 'result': " + resultStr +
                            ". Valid values: ALLOW, DENY, DEFAULT");
                }

                String[] parts = entityStr.split(":");
                ResourceLocation entityResource = new ResourceLocation(
                        parts.length > 1 ? parts[0] : "minecraft",
                        parts.length > 1 ? parts[1] : parts[0]
                );

                if (DEBUG_AND_CHECK_SYNTAX)
                {
                    Log.write(0, "Created ResourceLocation: " + entityResource);
                }

                CacheGameEventStorage.GameEventData eventData = new CacheGameEventStorage.GameEventData();

                eventData.day = day;
                eventData.repeat = repeat;

                eventData.entity = entityResource;
                eventData.max_entity_count = maxEntityCount;
                eventData.result = result;
                eventData.idDimension = idDimension;

                eventList.add(eventData);

                Log.write(0, String.format("Game Event Loaded - Day: %d, Repeat: %s, Entity: %s, " +
                                "Max Count: %d, Dimension: %s, Result: %s",
                        day, repeat, entityStr,
                        maxEntityCount,
                        idDimension != null ? idDimension.toString() : "any",
                        result));
            }

            CacheGameEventStorage.getInstance().eventData = eventList;
            Log.write(0, "Loaded script with " + eventList.size() + " game event entries");

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
            Log.write(0, "Clearing game event data cache");
        }

        CacheGameEventStorage.getInstance().eventData.clear();
    }
}
