package org.imesense.dynamicspawncontrol.technical.config.rendernight;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.config.CfgClassAbstract;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotations.DCSSingleConfig;

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
@DCSSingleConfig(fileName = "cfg_plugin_render_night")
public final class CfgRenderNight extends CfgClassAbstract
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgRenderNight(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.TRUE);

		CodeGenericUtils.printInitClassToLog(this.getClass());

        DataRenderNight.ConfigDataRenderNight.instance = new DataRenderNight.ConfigDataRenderNight("render_night");

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

        recordObject.add(DataRenderNight.ConfigDataRenderNight.instance.
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

        for (Double factor : DataRenderNight.ConfigDataRenderNight.instance.getMoonPhaseFactors())
        {
            moonPhaseFactorsArray.add(factor);
        }

        recordObject.add("moonPhaseFactors", moonPhaseFactorsArray);

        JsonArray blacklistByIDArray = new JsonArray();

        for (Integer id : DataRenderNight.ConfigDataRenderNight.instance.getBlacklistByID())
        {
            blacklistByIDArray.add(id);
        }

        recordObject.add("blacklistByID", blacklistByIDArray);

        JsonArray blacklistByNameArray = new JsonArray();

        for (String name : DataRenderNight.ConfigDataRenderNight.instance.getBlacklistByName())
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
                DataRenderNight.ConfigDataRenderNight.instance.getDarknessOverWorld());

        recordObject.addProperty("darknessNether",
                DataRenderNight.ConfigDataRenderNight.instance.getDarknessNether());

        recordObject.addProperty("darknessEnd",
                DataRenderNight.ConfigDataRenderNight.instance.getDarknessEnd());

        recordObject.addProperty("darknessDefault",
                DataRenderNight.ConfigDataRenderNight.instance.getDarknessDefault());

        recordObject.addProperty("darknessSkyLess",
                DataRenderNight.ConfigDataRenderNight.instance.getDarknessSkyLess());

        recordObject.addProperty("darknessNetherFog",
                DataRenderNight.ConfigDataRenderNight.instance.getDarknessNetherFog());

        recordObject.addProperty("darknessEndFog",
                DataRenderNight.ConfigDataRenderNight.instance.getDarknessEndFog());

        recordObject.addProperty("ignoreMoonLight",
                DataRenderNight.ConfigDataRenderNight.instance.getIgnoreMoonLight());

        recordObject.addProperty("invertBlacklist",
                DataRenderNight.ConfigDataRenderNight.instance.getInvertBlacklist());

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

            if (readableObject.has(DataRenderNight.ConfigDataRenderNight.instance.getCategoryObject()))
            {
                JsonObject jsonObjectRenderNight =
                        readableObject.getAsJsonObject(DataRenderNight.ConfigDataRenderNight.instance.getCategoryObject());

                if (jsonObjectRenderNight.has("darknessOverWorld"))
                {
                    DataRenderNight.ConfigDataRenderNight.instance.
                            setDarknessOverWorld(jsonObjectRenderNight.get("darknessOverWorld").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("darknessNether"))
                {
                    DataRenderNight.ConfigDataRenderNight.instance.
                            setDarknessNether(jsonObjectRenderNight.get("darknessNether").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("darknessEnd"))
                {
                    DataRenderNight.ConfigDataRenderNight.instance.
                            setDarknessEnd(jsonObjectRenderNight.get("darknessEnd").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("darknessDefault"))
                {
                    DataRenderNight.ConfigDataRenderNight.instance.
                            setDarknessDefault(jsonObjectRenderNight.get("darknessDefault").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("darknessSkyLess"))
                {
                    DataRenderNight.ConfigDataRenderNight.instance.
                            setDarknessSkyLess(jsonObjectRenderNight.get("darknessSkyLess").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("darknessNetherFog"))
                {
                    DataRenderNight.ConfigDataRenderNight.instance.
                            setDarknessNetherFog(jsonObjectRenderNight.get("darknessNetherFog").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("darknessEndFog"))
                {
                    DataRenderNight.ConfigDataRenderNight.instance.
                            setDarknessEndFog(jsonObjectRenderNight.get("darknessEndFog").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("ignoreMoonLight"))
                {
                    DataRenderNight.ConfigDataRenderNight.instance.
                            setIgnoreMoonLight(jsonObjectRenderNight.get("ignoreMoonLight").getAsBoolean());
                }

                if (jsonObjectRenderNight.has("invertBlacklist"))
                {
                    DataRenderNight.ConfigDataRenderNight.instance.
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

                    DataRenderNight.ConfigDataRenderNight.instance.
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

                    DataRenderNight.ConfigDataRenderNight.instance.
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

                    DataRenderNight.ConfigDataRenderNight.instance.
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
