package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.dropexperience.data.EntityDropExperience;
import org.imesense.dynamicspawncontrol.core.script.storage.dropexperience.storage.GeneralDropExperience;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.BiFunction;

@InitLog
public final class ParserEventDropExperience extends BaseParser
{
    private static final boolean DEBUG_AND_CHECK_SYNTAX = true;

    public ParserEventDropExperience(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;

        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "ParserEventDropExperience constructor called with file: " + NAME_FILE);
        }
    }

    public static <T> T getValueFromJson(JsonObject jsonObject, String key,
                                         T defaultValue, BiFunction<JsonElement, T, T> biFunction)
    {
        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "Getting value from JSON for key: " + key + " with default: " + defaultValue);
        }

        if (jsonObject.has(key))
        {
            JsonElement jsonElement = jsonObject.get(key);

            if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isBoolean()
                    && defaultValue instanceof Boolean)
            {
                if (DEBUG_AND_CHECK_SYNTAX)
                {
                    Log.write(0, "Found boolean value: " + jsonElement.getAsBoolean());
                }

                return (T) Boolean.valueOf(jsonElement.getAsBoolean());
            }
            else if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber())
            {
                T value = biFunction.apply(jsonElement, defaultValue);

                if (DEBUG_AND_CHECK_SYNTAX)
                {
                    Log.write(0, "Found number value: " + value);
                }

                return value;
            }
            else if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString())
            {
                if (DEBUG_AND_CHECK_SYNTAX)
                {
                    Log.write(0, "Found string value: " + jsonElement.getAsString());
                }

                return (T) jsonElement.getAsString();
            }
        }

        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "Using default value for key: " + key);
        }

        return defaultValue;
    }

    @Override
    public void loadConfig(boolean init)
    {
        Log.write(0, "Reading the config for the first time: " + init + " file: " + this.nameFile);

        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);

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

            JsonArray jsonArray = new JsonParser().parse(fileReader).getAsJsonArray();

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "Found " + jsonArray.size() + " entries in JSON array");
            }

            for (JsonElement element : jsonArray)
            {
                try
                {
                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        Log.write(0, "Processing new JSON element");
                    }

                    JsonObject jsonObject = element.getAsJsonObject();
                    EntityDropExperience.Data data = new EntityDropExperience.Data();

                    if (!jsonObject.has("entity"))
                    {
                        Log.write(0, "Missing required field 'entity' in entry: " + jsonObject);
                        continue;
                    }

                    String entityId = jsonObject.get("entity").getAsString();

                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        Log.write(0, "Processing entity: " + entityId);
                    }

                    data.entity = new ResourceLocation(entityId);

                    EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(data.entity);

                    if (entityEntry == null)
                    {
                        Log.write(0, "Entity not found: " + entityId);
                        continue;
                    }

                    data.idDimension = getValueFromJson(jsonObject, "id_dimension", null,
                            (el, def) -> el.getAsInt());

                    if (DEBUG_AND_CHECK_SYNTAX && data.idDimension != null)
                    {
                        Log.write(0, "Found id_dimension: " + data.idDimension + " for entity: " + entityId);
                    }

                    data.use_default_xp = getValueFromJson(jsonObject, "use_default_xp", false,
                            (el, def) -> el.getAsBoolean());

                    if (data.use_default_xp)
                    {
                        if (DEBUG_AND_CHECK_SYNTAX)
                        {
                            Log.write(0, "Using default XP for entity: " + entityId);
                        }

                        if (!jsonObject.has("multi_xp"))
                        {
                            Log.write(0, "use_default_xp = true requires multi_xp for entity: " + entityId);
                            continue;
                        }

                        data.multi_xp = getValueFromJson(jsonObject,
                                "multi_xp", 1.0f, (el, defaultValue) -> el.getAsFloat());

                        data.xp = null;
                        data.adding_xp = null;
                    }
                    else
                    {
                        if (DEBUG_AND_CHECK_SYNTAX)
                        {
                            Log.write(0, "Using custom XP values for entity: " + entityId);
                        }

                        data.xp = getValueFromJson(jsonObject,
                                "xp", 0, (el, defaultValue) -> el.getAsInt());

                        data.multi_xp = getValueFromJson(jsonObject,
                                "multi_xp", 1.0f, (el, defaultValue) -> el.getAsFloat());

                        data.adding_xp = getValueFromJson(jsonObject,
                                "adding_xp", 0.f, (el, defaultValue) -> el.getAsFloat());
                    }

                    data.worldTimeIntervalMin = getValueFromJson(jsonObject,
                            "time_min", null, (el, defaultValue) -> el.getAsLong());

                    data.worldTimeIntervalMax = getValueFromJson(jsonObject,
                            "time_max", null, (el, defaultValue) -> el.getAsLong());

                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        Log.write(0, "Time interval for " + entityId + ": " +
                                data.worldTimeIntervalMin + " - " + data.worldTimeIntervalMax);
                    }

                    if (data.worldTimeIntervalMin != null && data.worldTimeIntervalMax != null
                            && data.worldTimeIntervalMin > data.worldTimeIntervalMax)
                    {
                        Log.write(0, "Invalid time interval for entity " + entityId
                                + ": min > max (" + data.worldTimeIntervalMin + " > " + data.worldTimeIntervalMax + ")");

                        continue;
                    }

                    if (jsonObject.has("result"))
                    {
                        String resultStr = jsonObject.get("result").getAsString().toLowerCase();

                        if (DEBUG_AND_CHECK_SYNTAX)
                        {
                            Log.write(0, "Processing result: " + resultStr);
                        }

                        switch (resultStr)
                        {
                            case "allow":
                                data.result = Event.Result.ALLOW;
                                break;
                            case "deny":
                                data.result = Event.Result.DENY;
                                break;
                            default:
                                data.result = Event.Result.DEFAULT;
                                break;
                        }
                    }

                    Log.write(0, String.format(
                            "Loaded entity: %s, dimension: %s, mode: %s, xp: %s, multi: %.2f, add: %s, time: %s-%s, result: %s",
                            data.entity,
                            data.idDimension != null ? data.idDimension.toString() : "any",
                            data.use_default_xp ? "DEFAULT_XP" : "FULL",
                            data.use_default_xp ? "N/A" : data.xp,
                            data.multi_xp,
                            data.use_default_xp ? "N/A" : data.adding_xp,
                            data.worldTimeIntervalMin != null ? data.worldTimeIntervalMin : "ANY",
                            data.worldTimeIntervalMax != null ? data.worldTimeIntervalMax : "ANY",
                            data.result
                    ));

                    GeneralDropExperience.getInstance().dropExperienceList.add(data);

                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        Log.write(0, "Successfully added drop experience data for entity: " + entityId);
                    }
                }
                catch (Exception exception)
                {
                    Log.write(0, "Error processing config entry: " + element);

                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        Log.write(0, "Exception details: " + exception.getMessage());
                    }
                }
            }
        }
        catch (IOException exception)
        {
            Log.write(0, "Failed to load config file: " + file);

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "IOException details: " + exception.getMessage());
            }
        }
        catch (JsonParseException exception)
        {
            Log.write(0, "Malformed JSON in config file: " + file);

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "JsonParseException details: " + exception.getMessage());
            }
        }
    }

    @Override
    public void eraseData()
    {
        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "Clearing drop experience list");
        }

        GeneralDropExperience.getInstance().dropExperienceList.clear();
    }
}