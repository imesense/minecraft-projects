package org.imesense.dynamicspawncontrol.core.script.parser;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.base.BaseParser;

import java.io.File;

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheGameEventStorage;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheNodeLinkManager;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@InitLog
public class ParserEventCacheGameEvents extends BaseParser
{
    public ParserEventCacheGameEvents(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;
    }

    @Override
    public void loadConfig(boolean init)
    {
        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_CACHE, this.nameFile);

        if (!file.exists())
        {
            this.createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(fileReader, JsonArray.class);

            if (jsonArray == null)
            {
                throw new RuntimeException("Script does not contain valid JSON array.");
            }

            List<CacheGameEventStorage.GameEventData> eventList = new ArrayList<>();
            CacheNodeLinkManager nodeManager = CacheNodeLinkManager.getInstance();

            for (JsonElement jsonElement : jsonArray)
            {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                if (!jsonObject.has("event"))
                {
                    throw new RuntimeException("Script must contain 'event' key.");
                }

                JsonObject eventObject = jsonObject.getAsJsonObject("event");

                Long idNode = null;
                if (eventObject.has("id_node"))
                {
                    idNode = eventObject.get("id_node").getAsLong();
                }

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

                nodeManager.checkForDuplicates(entityStr, idNode);

                CacheGameEventStorage.GameEventData eventData = new CacheGameEventStorage.GameEventData();

                eventData.day = day;
                eventData.repeat = repeat;

                eventData.entity = entityResource;
                eventData.max_entity_count = maxEntityCount;
                eventData.result = result;
                eventData.idDimension = idDimension;
                eventData.idNode = idNode;

                eventList.add(eventData);

                nodeManager.registerEventNode(idNode, eventData);
            }

            CacheGameEventStorage.getInstance().eventData = eventList;

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
        CacheGameEventStorage.getInstance().eventData.clear();
        CacheNodeLinkManager.getInstance().clearAll();
    }
}
