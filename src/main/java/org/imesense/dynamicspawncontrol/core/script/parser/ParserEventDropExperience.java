package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.dropexperience.data.EntityDropExperience;
import org.imesense.dynamicspawncontrol.core.script.storage.dropexperience.storage.GeneralDropExperience;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.BiFunction;

@InitLog
public final class ParserEventDropExperience extends BaseParser
{
    public ParserEventDropExperience(final String NAME_FILE)
    {
        super();

        this.nameFile = NAME_FILE;
    }

    public static <T> T getValueFromJson(JsonObject jsonObject, String key, T defaultValue, BiFunction<JsonElement, T, T> biFunction)
    {
        Log.writeDataToLogFile(0, "Read jsonObject: " + jsonObject);

        if (jsonObject.has(key))
        {
            JsonElement jsonElement = jsonObject.get(key);

            if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber())
            {
                return biFunction.apply(jsonElement, defaultValue);
            }
        }

        return defaultValue;
    }

    @Override
    public void loadConfig(boolean init)
    {
        Log.writeDataToLogFile(0, "Reading the config for the first time: " + init + " " + "file: " + this.nameFile);

        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);

        if (!file.exists())
        {
            Log.writeDataToLogFile(0, "Config file not found, creating new: " + file);
            this.createNewConfigFile(file);
            return;
        }

        try (FileReader reader = new FileReader(file))
        {
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(reader).getAsJsonArray();

            for (JsonElement element : jsonArray)
            {
                JsonObject jsonObject = element.getAsJsonObject();
                EntityDropExperience.Data data = new EntityDropExperience.Data();

                String entityId = jsonObject.get("entity").getAsString();
                data.entity = new ResourceLocation(entityId);

                EntityEntry ee = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityId));

                if (ee == null)
                {
                    Log.writeDataToLogFile(0, "Entity not found: " + entityId);
                    continue;
                }

                data.xp = getValueFromJson(jsonObject,
                        "xp", 0, (el, defaultValue) -> el.getAsInt());

                data.multi_xp = getValueFromJson(jsonObject,
                        "multi_xp", 0.f, (el, defaultValue) -> el.getAsFloat());

                data.adding_xp = getValueFromJson(jsonObject,
                        "adding_xp", 0.f, (el, defaultValue) -> el.getAsFloat());

                Log.writeDataToLogFile(0, "Loaded data for entity: " + data.entity + ", xp: " + data.xp + ", multi_xp: " + data.multi_xp + ", adding_xp: " + data.adding_xp);

                GeneralDropExperience.getInstance().dropExperienceList.add(data);
            }
        }
        catch (IOException exception)
        {
            Log.writeDataToLogFile(0, "Failed to load config file: " + file);
            exception.printStackTrace();
        }
    }

    @Override
    public void eraseData()
    {
        GeneralDropExperience.getInstance().dropExperienceList.clear();
    }
}
