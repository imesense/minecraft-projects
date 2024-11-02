package org.imesense.dynamicspawncontrol.plugin.darkness_forge_1_12_x_0_5_0.config;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.config.CfgClassAbstract;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotation.DCSSingleConfig;

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
@DCSSingleConfig(fileName = "cfg_darkness_forge_1_12_x_0_5_0")
public final class CfgDarkness extends CfgClassAbstract
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgDarkness(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.FALSE);

		CodeGenericUtil.printInitClassToLog(this.getClass());

        DataDarkness.ConfigDataRenderNight.instance =
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

        recordObject.add(DataDarkness.ConfigDataRenderNight.instance.
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

        for (Double factor : DataDarkness.ConfigDataRenderNight.instance.getMoonPhaseFactors())
        {
            moonPhaseFactorsArray.add(factor);
        }

        recordObject.add("moonPhaseFactors", moonPhaseFactorsArray);

        JsonArray blacklistByIDArray = new JsonArray();

        for (Integer id : DataDarkness.ConfigDataRenderNight.instance.getBlacklistByID())
        {
            blacklistByIDArray.add(id);
        }

        recordObject.add("blacklistByID", blacklistByIDArray);

        JsonArray blacklistByNameArray = new JsonArray();

        for (String name : DataDarkness.ConfigDataRenderNight.instance.getBlacklistByName())
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
                DataDarkness.ConfigDataRenderNight.instance.getDarknessOverWorld());

        recordObject.addProperty("darknessNether",
                DataDarkness.ConfigDataRenderNight.instance.getDarknessNether());

        recordObject.addProperty("darknessEnd",
                DataDarkness.ConfigDataRenderNight.instance.getDarknessEnd());

        recordObject.addProperty("darknessDefault",
                DataDarkness.ConfigDataRenderNight.instance.getDarknessDefault());

        recordObject.addProperty("darknessSkyLess",
                DataDarkness.ConfigDataRenderNight.instance.getDarknessSkyLess());

        recordObject.addProperty("darknessNetherFog",
                DataDarkness.ConfigDataRenderNight.instance.getDarknessNetherFog());

        recordObject.addProperty("darknessEndFog",
                DataDarkness.ConfigDataRenderNight.instance.getDarknessEndFog());

        recordObject.addProperty("ignoreMoonLight",
                DataDarkness.ConfigDataRenderNight.instance.getIgnoreMoonLight());

        recordObject.addProperty("invertBlacklist",
                DataDarkness.ConfigDataRenderNight.instance.getInvertBlacklist());

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

            if (readableObject.has(DataDarkness.ConfigDataRenderNight.instance.getCategoryObject()))
            {
                JsonObject jsonObjectRenderNight =
                        readableObject.getAsJsonObject(DataDarkness.ConfigDataRenderNight.instance.getCategoryObject());

                if (jsonObjectRenderNight.has("darknessOverWorld"))
                {
                    DataDarkness.ConfigDataRenderNight.instance.
                            setDarknessOverWorld(jsonObjectRenderNight.get("darknessOverWorld").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("darknessNether"))
                {
                    DataDarkness.ConfigDataRenderNight.instance.
                            setDarknessNether(jsonObjectRenderNight.get("darknessNether").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("darknessEnd"))
                {
                    DataDarkness.ConfigDataRenderNight.instance.
                            setDarknessEnd(jsonObjectRenderNight.get("darknessEnd").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("darknessDefault"))
                {
                    DataDarkness.ConfigDataRenderNight.instance.
                            setDarknessDefault(jsonObjectRenderNight.get("darknessDefault").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("darknessSkyLess"))
                {
                    DataDarkness.ConfigDataRenderNight.instance.
                            setDarknessSkyLess(jsonObjectRenderNight.get("darknessSkyLess").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("darknessNetherFog"))
                {
                    DataDarkness.ConfigDataRenderNight.instance.
                            setDarknessNetherFog(jsonObjectRenderNight.get("darknessNetherFog").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("darknessEndFog"))
                {
                    DataDarkness.ConfigDataRenderNight.instance.
                            setDarknessEndFog(jsonObjectRenderNight.get("darknessEndFog").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("ignoreMoonLight"))
                {
                    DataDarkness.ConfigDataRenderNight.instance.
                            setIgnoreMoonLight(jsonObjectRenderNight.get("ignoreMoonLight").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("invertBlacklist"))
                {
                    DataDarkness.ConfigDataRenderNight.instance.
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

                    DataDarkness.ConfigDataRenderNight.instance.
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

                    DataDarkness.ConfigDataRenderNight.instance.
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

                    DataDarkness.ConfigDataRenderNight.instance.
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
