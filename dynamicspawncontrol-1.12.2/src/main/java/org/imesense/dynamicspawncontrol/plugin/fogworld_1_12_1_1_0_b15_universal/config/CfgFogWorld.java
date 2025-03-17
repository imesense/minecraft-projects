package org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.config;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseConfigLegacy;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ConceptConfig(fileName = "cfg_fog_world_1_12_1_1_0_b15_universal")
public class CfgFogWorld extends BaseConfigLegacy
{
    public CfgFogWorld(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.FALSE);

        CodeGeneric.printInitClassToLog(this.getClass());

        DataFogWorld.ConfigDataFogWorld.Instance =
                new DataFogWorld.ConfigDataFogWorld("fog_world_1_12_1_1_0_b15_universal");

        if (Files.exists(Paths.get(this.nameConfig)))
        {
            this.loadFromFile();
        }
        else
        {
            this.saveToFile();
        }
    }

    @Override
    public void saveToFile()
    {
        Path configPath = Paths.get(this.nameConfig).getParent();

        if (Files.notExists(configPath))
        {
            try
            {
                Files.createDirectories(configPath);
            }
            catch (IOException exception)
            {
                throw new RuntimeException(exception);
            }
        }

        JsonObject recordObject = getJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter file = new FileWriter(this.nameConfig))
        {
            gson.toJson(recordObject, file);
        }
        catch (IOException exception)
        {
            throw new RuntimeException("Error writing to file: " + exception.getMessage(), exception);
        }
    }

    private static JsonObject getJsonObject() {
        JsonObject recordObject = new JsonObject();
        JsonObject jsonObjectWorldTime = new JsonObject();

        jsonObjectWorldTime.addProperty("Fog_Density",
                DataFogWorld.ConfigDataFogWorld.Instance.getFogDensity());

        jsonObjectWorldTime.addProperty("Fog_Color",
                DataFogWorld.ConfigDataFogWorld.Instance.getFogColor());

        recordObject.add(DataFogWorld.ConfigDataFogWorld.Instance.getCategoryObject(), jsonObjectWorldTime);

        return recordObject;
    }

    @Override
    public void loadFromFile() {
        try (FileReader fileReader = new FileReader(this.nameConfig)) {
            JsonElement fileReaderJsonElement = new JsonParser().parse(fileReader);
            JsonObject readableObject = fileReaderJsonElement.getAsJsonObject();

            if (readableObject.has(DataFogWorld.ConfigDataFogWorld.Instance.getCategoryObject())) {
                JsonObject jsonObjectWorldTime =
                        readableObject.getAsJsonObject(DataFogWorld.ConfigDataFogWorld.Instance.getCategoryObject());

                if (jsonObjectWorldTime.has("Fog_Density")) {
                    DataFogWorld.ConfigDataFogWorld.Instance
                            .setFogDensity(jsonObjectWorldTime.get("Fog_Density").getAsFloat());
                }

                if (jsonObjectWorldTime.has("Fog_Color")) {
                    DataFogWorld.ConfigDataFogWorld.Instance
                            .setFogColor(jsonObjectWorldTime.get("Fog_Color").getAsInt());
                }
            } else {
                Log.writeDataToLogFile(2, "Fog world settings are missing in the config file.");
            }
        } catch (FileNotFoundException exception) {
            Log.writeDataToLogFile(2, "File not found: " + exception.getMessage());
        } catch (IOException exception) {
            Log.writeDataToLogFile(2, "IO Exception while loading: " + exception.getMessage());
        }
    }

}
