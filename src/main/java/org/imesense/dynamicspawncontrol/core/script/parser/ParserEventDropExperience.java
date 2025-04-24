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
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@InitLog
public final class ParserEventDropExperience extends BaseParser
{
    public ParserEventDropExperience(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;
    }

    public static <T> T getValueFromJson(JsonObject jsonObject, String key,
                                         T defaultValue, BiFunction<JsonElement, T, T> biFunction)
    {
        if (jsonObject.has(key))
        {
            JsonElement jsonElement = jsonObject.get(key);

            if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isBoolean()
                    && defaultValue instanceof Boolean)
            {
                return (T) Boolean.valueOf(jsonElement.getAsBoolean());
            }
            else if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber())
            {
                return biFunction.apply(jsonElement, defaultValue);
            }
            else if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString())
            {
                return (T) jsonElement.getAsString();
            }
        }

        return defaultValue;
    }

    @Override
    public void loadConfig(boolean init)
    {
        Log.writeDataToLogFile(0, "Reading the config for the first time: " + init + " file: " + this.nameFile);

        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);

        if (!file.exists())
        {
            Log.writeDataToLogFile(0, "Config file not found, creating new: " + file);
            this.createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            JsonArray jsonArray = JsonParser.parseReader(fileReader).getAsJsonArray();

            for (JsonElement element : jsonArray)
            {
                try
                {
                    JsonObject jsonObject = element.getAsJsonObject();
                    EntityDropExperience.Data data = new EntityDropExperience.Data();

                    if (!jsonObject.has("entity"))
                    {
                        Log.writeDataToLogFile(0, "Missing required field 'entity' in entry: " + jsonObject);
                        continue;
                    }

                    List<ResourceLocation> entityLocations = new ArrayList<>();

                    if (jsonObject.get("entity").isJsonArray())
                    {
                        JsonArray entityArray = jsonObject.getAsJsonArray("entity");

                        for (JsonElement el : entityArray)
                        {
                            String entityId = el.getAsString();
                            ResourceLocation location = new ResourceLocation(entityId);
                            EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(location);

                            if (entityEntry == null)
                            {
                                Log.writeDataToLogFile(0, "Entity not found: " + entityId);
                                continue;
                            }

                            entityLocations.add(location);
                        }
                    }
                    else
                    {
                        String entityId = jsonObject.get("entity").getAsString();
                        ResourceLocation location = new ResourceLocation(entityId);
                        EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(location);

                        if (entityEntry == null)
                        {
                            Log.writeDataToLogFile(0, "Entity not found: " + entityId);
                            continue;
                        }

                        entityLocations.add(location);
                    }

                    if (entityLocations.isEmpty())
                    {
                        continue;
                    }

                    data.entities = entityLocations;

                    data.use_default_xp = getValueFromJson(jsonObject, "use_default_xp", false,
                            (el, def) -> el.getAsBoolean());

                    if (data.use_default_xp)
                    {
                        if (!jsonObject.has("multi_xp"))
                        {
                            Log.writeDataToLogFile(0, "use_default_xp = true requires multi_xp for entities: " + entityLocations);
                            continue;
                        }

                        data.multi_xp = getValueFromJson(jsonObject,
                                "multi_xp", 1.0f, (el, defaultValue) -> el.getAsFloat());

                        data.xp = null;
                        data.adding_xp = null;
                    }
                    else
                    {
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

                    if (data.worldTimeIntervalMin != null && data.worldTimeIntervalMax != null
                            && data.worldTimeIntervalMin > data.worldTimeIntervalMax)
                    {
                        continue;
                    }

                    if (jsonObject.has("result"))
                    {
                        String resultStr = jsonObject.get("result").getAsString().toLowerCase();

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

                    Log.writeDataToLogFile(0, String.format(
                            "Loaded entities: %s, mode: %s, xp: %s, multi: %.2f, add: %s, time: %s-%s, result: %s",
                            data.entities,
                            data.use_default_xp ? "DEFAULT_XP" : "FULL",
                            data.use_default_xp ? "N/A" : data.xp,
                            data.multi_xp,
                            data.use_default_xp ? "N/A" : data.adding_xp,
                            data.worldTimeIntervalMin != null ? data.worldTimeIntervalMin : "ANY",
                            data.worldTimeIntervalMax != null ? data.worldTimeIntervalMax : "ANY",
                            data.result
                    ));

                    GeneralDropExperience.getInstance().dropExperienceList.add(data);
                }
                catch (Exception exception)
                {
                    Log.writeDataToLogFile(0, "Error processing config entry: " + element);
                }
            }
        }
        catch (IOException exception)
        {
            Log.writeDataToLogFile(0, "Failed to load config file: " + file);
        }
        catch (JsonParseException exception)
        {
            Log.writeDataToLogFile(0, "Malformed JSON in config file: " + file);
        }
    }

    @Override
    public void eraseData()
    {
        GeneralDropExperience.getInstance().dropExperienceList.clear();
    }
}
