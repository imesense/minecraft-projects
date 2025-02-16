package org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.config;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.api.AConfig;
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
public class CfgFogWorld extends AConfig
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

        jsonObjectWorldTime.addProperty("Poison_Fog",
                DataFogWorld.ConfigDataFogWorld.Instance.isPoisonousFog());

        jsonObjectWorldTime.addProperty("Poison_Fog_Delay",
                DataFogWorld.ConfigDataFogWorld.Instance.getPosionTicks());

        jsonObjectWorldTime.addProperty("Poison_Fog_Damage",
                DataFogWorld.ConfigDataFogWorld.Instance.getPoisonDamage());

        // Создаем JSON-массив для биомов
        JsonArray disabledBiomes = new JsonArray();
        for (String biome : DataFogWorld.ConfigDataFogWorld.Instance.getFogBiomeBlacklist()) {
            disabledBiomes.add(biome);
        }
        jsonObjectWorldTime.add("Disabled_Biomes", disabledBiomes);

        // Создаем JSON-массив для измерений
        JsonArray disabledDimensions = new JsonArray();
        for (String dimension : DataFogWorld.ConfigDataFogWorld.Instance.getFogDimensionBlacklist()) {
            disabledDimensions.add(dimension);
        }
        jsonObjectWorldTime.add("Disabled_Dimensions", disabledDimensions);

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

                if (jsonObjectWorldTime.has("Poison_Fog")) {
                    DataFogWorld.ConfigDataFogWorld.Instance
                            .setPoisonousFog(jsonObjectWorldTime.get("Poison_Fog").getAsBoolean());
                }

                if (jsonObjectWorldTime.has("Poison_Fog_Delay")) {
                    DataFogWorld.ConfigDataFogWorld.Instance
                            .setPosionTicks(jsonObjectWorldTime.get("Poison_Fog_Delay").getAsInt());
                }

                if (jsonObjectWorldTime.has("Poison_Fog_Damage")) {
                    DataFogWorld.ConfigDataFogWorld.Instance
                            .setPoisonDamage(jsonObjectWorldTime.get("Poison_Fog_Damage").getAsInt());
                }

                if (jsonObjectWorldTime.has("Disabled_Biomes")) {
                    JsonArray disabledBiomesArray = jsonObjectWorldTime.getAsJsonArray("Disabled_Biomes");
                    String[] disabledBiomes = new String[disabledBiomesArray.size()];

                    for (int i = 0; i < disabledBiomesArray.size(); i++) {
                        disabledBiomes[i] = disabledBiomesArray.get(i).getAsString();
                    }

                    DataFogWorld.ConfigDataFogWorld.Instance.setFogBiomeBlacklist(disabledBiomes);
                }

                if (jsonObjectWorldTime.has("Disabled_Dimensions")) {
                    JsonArray disabledDimensionsArray = jsonObjectWorldTime.getAsJsonArray("Disabled_Dimensions");
                    String[] disabledDimensions = new String[disabledDimensionsArray.size()];

                    for (int i = 0; i < disabledDimensionsArray.size(); i++) {
                        disabledDimensions[i] = disabledDimensionsArray.get(i).getAsString();
                    }

                    DataFogWorld.ConfigDataFogWorld.Instance.setFogDimensionBlacklist(disabledDimensions);
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
