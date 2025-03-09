package org.imesense.dynamicspawncontrol.plugin.darkness_forge_1_12_x_0_5_0.config;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.api.BaseConfig;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 */
@ConceptConfig(fileName = "cfg_darkness_forge_1_12_x_0_5_0")
public final class CfgDarkness extends BaseConfig
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgDarkness(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.FALSE);

		CodeGeneric.printInitClassToLog(this.getClass());

        DataDarkness.ConfigDataRenderNight.Instance =
                new DataDarkness.ConfigDataRenderNight("darkness_forge_1_12_x_0_5_0");

        if (Files.exists(Paths.get(this.nameConfig)))
        {
            this.loadFromFile();
        }
        else
        {
            this.saveToFile();
        }
    }

    /**
     *
     */
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

        JsonObject recordObject = new JsonObject();
        JsonObject jsonObjectRenderNight = getObject();

        recordObject.add(DataDarkness.ConfigDataRenderNight.Instance.
                getCategoryObject(), jsonObjectRenderNight);

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

    /**
     *
     * @return
     */
    private static JsonObject getObject()
    {
        JsonObject recordObject = getJsonObject();

        JsonArray moonPhaseFactorsArray = new JsonArray();

        for (Double factor : DataDarkness.ConfigDataRenderNight.Instance.getMoonPhaseFactors())
        {
            moonPhaseFactorsArray.add(factor);
        }

        recordObject.add("moonPhaseFactors", moonPhaseFactorsArray);

        JsonArray blacklistByIDArray = new JsonArray();

        for (Integer id : DataDarkness.ConfigDataRenderNight.Instance.getBlacklistByID())
        {
            blacklistByIDArray.add(id);
        }

        recordObject.add("blacklistByID", blacklistByIDArray);

        JsonArray blacklistByNameArray = new JsonArray();

        for (String name : DataDarkness.ConfigDataRenderNight.Instance.getBlacklistByName())
        {
            blacklistByNameArray.add(name);
        }

        recordObject.add("blacklistByName", blacklistByNameArray);

        return recordObject;
    }

    /**
     *
     * @return
     */
    private static JsonObject getJsonObject()
    {
        JsonObject recordObject = new JsonObject();

        recordObject.addProperty("darknessOverWorld",
                DataDarkness.ConfigDataRenderNight.Instance.getDarknessOverWorld());

        recordObject.addProperty("darknessNether",
                DataDarkness.ConfigDataRenderNight.Instance.getDarknessNether());

        recordObject.addProperty("darknessEnd",
                DataDarkness.ConfigDataRenderNight.Instance.getDarknessEnd());

        recordObject.addProperty("darknessDefault",
                DataDarkness.ConfigDataRenderNight.Instance.getDarknessDefault());

        recordObject.addProperty("darknessSkyLess",
                DataDarkness.ConfigDataRenderNight.Instance.getDarknessSkyLess());

        recordObject.addProperty("darknessNetherFog",
                DataDarkness.ConfigDataRenderNight.Instance.getDarknessNetherFog());

        recordObject.addProperty("darknessEndFog",
                DataDarkness.ConfigDataRenderNight.Instance.getDarknessEndFog());

        recordObject.addProperty("ignoreMoonLight",
                DataDarkness.ConfigDataRenderNight.Instance.getIgnoreMoonLight());

        recordObject.addProperty("invertBlacklist",
                DataDarkness.ConfigDataRenderNight.Instance.getInvertBlacklist());

        return recordObject;
    }

    /**
     *
     */
    @Override
    public void loadFromFile()
    {
        try (FileReader fileReader = new FileReader(this.nameConfig))
        {
            JsonElement fileReaderJsonElement = new JsonParser().parse(fileReader);
            JsonObject readableObject = fileReaderJsonElement.getAsJsonObject();

            if (readableObject.has(DataDarkness.ConfigDataRenderNight.Instance.getCategoryObject()))
            {
                JsonObject jsonObjectRenderNight =
                        readableObject.getAsJsonObject(DataDarkness.ConfigDataRenderNight.Instance.getCategoryObject());

                if (jsonObjectRenderNight.has("darknessOverWorld"))
                {
                    DataDarkness.ConfigDataRenderNight.Instance.
                            setDarknessOverWorld(jsonObjectRenderNight.get("darknessOverWorld").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("darknessNether"))
                {
                    DataDarkness.ConfigDataRenderNight.Instance.
                            setDarknessNether(jsonObjectRenderNight.get("darknessNether").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("darknessEnd"))
                {
                    DataDarkness.ConfigDataRenderNight.Instance.
                            setDarknessEnd(jsonObjectRenderNight.get("darknessEnd").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("darknessDefault"))
                {
                    DataDarkness.ConfigDataRenderNight.Instance.
                            setDarknessDefault(jsonObjectRenderNight.get("darknessDefault").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("darknessSkyLess"))
                {
                    DataDarkness.ConfigDataRenderNight.Instance.
                            setDarknessSkyLess(jsonObjectRenderNight.get("darknessSkyLess").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("darknessNetherFog"))
                {
                    DataDarkness.ConfigDataRenderNight.Instance.
                            setDarknessNetherFog(jsonObjectRenderNight.get("darknessNetherFog").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("darknessEndFog"))
                {
                    DataDarkness.ConfigDataRenderNight.Instance.
                            setDarknessEndFog(jsonObjectRenderNight.get("darknessEndFog").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("ignoreMoonLight"))
                {
                    DataDarkness.ConfigDataRenderNight.Instance.
                            setIgnoreMoonLight(jsonObjectRenderNight.get("ignoreMoonLight").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("invertBlacklist"))
                {
                    DataDarkness.ConfigDataRenderNight.Instance.
                            setInvertBlacklist(jsonObjectRenderNight.get("invertBlacklist").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("moonPhaseFactors"))
                {
                    JsonArray moonPhaseFactorsArray =
                            jsonObjectRenderNight.getAsJsonArray("moonPhaseFactors");

                    Double[] moonPhaseFactors = new Double[moonPhaseFactorsArray.size()];

                    for (int i = 0; i < moonPhaseFactorsArray.size(); i++)
                    {
                        moonPhaseFactors[i] = moonPhaseFactorsArray.get(i).getAsDouble();
                    }

                    DataDarkness.ConfigDataRenderNight.Instance.
                            setMoonPhaseFactors(moonPhaseFactors);
                }

                if (jsonObjectRenderNight.has("blacklistByID"))
                {
                    JsonArray blacklistByIDArray =
                            jsonObjectRenderNight.getAsJsonArray("blacklistByID");

                    Integer[] blacklistByID = new Integer[blacklistByIDArray.size()];

                    for (int i = 0; i < blacklistByIDArray.size(); i++)
                    {
                        blacklistByID[i] = blacklistByIDArray.get(i).getAsInt();
                    }

                    DataDarkness.ConfigDataRenderNight.Instance.
                            setBlacklistByID(blacklistByID);
                }

                if (jsonObjectRenderNight.has("blacklistByName"))
                {
                    JsonArray blacklistByNameArray =
                            jsonObjectRenderNight.getAsJsonArray("blacklistByName");

                    String[] blacklistByName = new String[blacklistByNameArray.size()];

                    for (int i = 0; i < blacklistByNameArray.size(); i++)
                    {
                        blacklistByName[i] = blacklistByNameArray.get(i).getAsString();
                    }

                    DataDarkness.ConfigDataRenderNight.Instance.
                            setBlacklistByName(blacklistByName);
                }
            }
            else
            {
                Log.writeDataToLogFile(2, "render_night is missing in the config file.");
            }
        }
        catch (FileNotFoundException exception)
        {
            Log.writeDataToLogFile(2, "File not found: " + exception.getMessage());
        }
        catch (IOException exception)
        {
            Log.writeDataToLogFile(2, "IO Exception while loading: " + exception.getMessage());
        }
    }
}
