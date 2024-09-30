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
@DCSSingleConfig(fileName = "cfg_render_night.json")
public final class CfgRenderNight extends CfgClassAbstract
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgRenderNight(String nameConfigFile)
    {
        super(nameConfigFile);

		CodeGenericUtils.printInitClassToLog(this.getClass());

        DataRenderNight.renderNight.instance = new DataRenderNight.renderNight("render_night");

        if (Files.exists(Paths.get(this.nameConfig)))
        {
            loadFromFile();
        }
        else
        {
            Log.writeDataToLogFile(0, "Config file does not exist. Creating a new one.");
            saveToFile();
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
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        JsonObject jsonObject = new JsonObject();
        JsonObject monitorObject = getObject();

        jsonObject.add("render_night", monitorObject);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter file = new FileWriter(this.nameConfig))
        {
            gson.toJson(jsonObject, file);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error writing to file: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @return
     */
    private static JsonObject getObject()
    {
        JsonObject monitorObject = getJsonObject();

        JsonArray moonPhaseFactorsArray = new JsonArray();

        for (Double factor : DataRenderNight.renderNight.instance.getMoonPhaseFactors())
        {
            moonPhaseFactorsArray.add(factor);
        }

        monitorObject.add("moonPhaseFactors", moonPhaseFactorsArray);

        JsonArray blacklistByIDArray = new JsonArray();

        for (Integer id : DataRenderNight.renderNight.instance.getBlacklistByID())
        {
            blacklistByIDArray.add(id);
        }

        monitorObject.add("blacklistByID", blacklistByIDArray);

        JsonArray blacklistByNameArray = new JsonArray();

        for (String name : DataRenderNight.renderNight.instance.getBlacklistByName())
        {
            blacklistByNameArray.add(name);
        }

        monitorObject.add("blacklistByName", blacklistByNameArray);

        return monitorObject;
    }

    /**
     *
     * @return
     */
    private static JsonObject getJsonObject()
    {
        JsonObject monitorObject = new JsonObject();

        monitorObject.addProperty("darknessOverWorld", DataRenderNight.renderNight.instance.getDarknessOverWorld());
        monitorObject.addProperty("darknessNether", DataRenderNight.renderNight.instance.getDarknessNether());
        monitorObject.addProperty("darknessEnd", DataRenderNight.renderNight.instance.getDarknessEnd());
        monitorObject.addProperty("darknessDefault", DataRenderNight.renderNight.instance.getDarknessDefault());
        monitorObject.addProperty("darknessSkyLess", DataRenderNight.renderNight.instance.getDarknessSkyLess());
        monitorObject.addProperty("darknessNetherFog", DataRenderNight.renderNight.instance.getDarknessNetherFog());
        monitorObject.addProperty("darknessEndFog", DataRenderNight.renderNight.instance.getDarknessEndFog());
        monitorObject.addProperty("ignoreMoonLight", DataRenderNight.renderNight.instance.getIgnoreMoonLight());
        return monitorObject;
    }

    /**
     *
     */
    @Override
    public void loadFromFile()
    {
        try (FileReader reader = new FileReader(this.nameConfig))
        {
            JsonElement jsonElement = new JsonParser().parse(reader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject.has("render_night"))
            {
                JsonObject renderNightObject = jsonObject.getAsJsonObject("render_night");

                if (renderNightObject.has("darknessOverWorld"))
                {
                    DataRenderNight.renderNight.instance.setDarknessOverWorld(renderNightObject.get("darknessOverWorld").getAsBoolean());
                }

                if (renderNightObject.has("darknessNether"))
                {
                    DataRenderNight.renderNight.instance.setDarknessNether(renderNightObject.get("darknessNether").getAsBoolean());
                }

                if (renderNightObject.has("darknessEnd"))
                {
                    DataRenderNight.renderNight.instance.setDarknessEnd(renderNightObject.get("darknessEnd").getAsBoolean());
                }

                if (renderNightObject.has("darknessDefault"))
                {
                    DataRenderNight.renderNight.instance.setDarknessDefault(renderNightObject.get("darknessDefault").getAsBoolean());
                }

                if (renderNightObject.has("darknessSkyLess"))
                {
                    DataRenderNight.renderNight.instance.setDarknessSkyLess(renderNightObject.get("darknessSkyLess").getAsBoolean());
                }

                if (renderNightObject.has("darknessNetherFog"))
                {
                    DataRenderNight.renderNight.instance.setDarknessNetherFog(renderNightObject.get("darknessNetherFog").getAsBoolean());
                }

                if (renderNightObject.has("darknessEndFog"))
                {
                    DataRenderNight.renderNight.instance.setDarknessEndFog(renderNightObject.get("darknessEndFog").getAsBoolean());
                }

                if (renderNightObject.has("ignoreMoonLight"))
                {
                    DataRenderNight.renderNight.instance.setIgnoreMoonLight(renderNightObject.get("ignoreMoonLight").getAsBoolean());
                }

                if (renderNightObject.has("moonPhaseFactors"))
                {
                    JsonArray moonPhaseFactorsArray = renderNightObject.getAsJsonArray("moonPhaseFactors");
                    Double[] moonPhaseFactors = new Double[moonPhaseFactorsArray.size()];

                    for (int i = 0; i < moonPhaseFactorsArray.size(); i++)
                    {
                        moonPhaseFactors[i] = moonPhaseFactorsArray.get(i).getAsDouble();
                    }

                    DataRenderNight.renderNight.instance.setMoonPhaseFactors(moonPhaseFactors);
                }

                if (renderNightObject.has("blacklistByID"))
                {
                    JsonArray blacklistByIDArray = renderNightObject.getAsJsonArray("blacklistByID");
                    Integer[] blacklistByID = new Integer[blacklistByIDArray.size()];

                    for (int i = 0; i < blacklistByIDArray.size(); i++)
                    {
                        blacklistByID[i] = blacklistByIDArray.get(i).getAsInt();
                    }

                    DataRenderNight.renderNight.instance.setBlacklistByID(blacklistByID);
                }

                if (renderNightObject.has("blacklistByName"))
                {
                    JsonArray blacklistByNameArray = renderNightObject.getAsJsonArray("blacklistByName");
                    String[] blacklistByName = new String[blacklistByNameArray.size()];

                    for (int i = 0; i < blacklistByNameArray.size(); i++)
                    {
                        blacklistByName[i] = blacklistByNameArray.get(i).getAsString();
                    }

                    DataRenderNight.renderNight.instance.setBlacklistByName(blacklistByName);
                }
            }
            else
            {
                Log.writeDataToLogFile(2, "render_night is missing in the config file.");
            }
        }
        catch (FileNotFoundException e)
        {
            Log.writeDataToLogFile(2, "File not found: " + e.getMessage());
        }
        catch (IOException e)
        {
            Log.writeDataToLogFile(2, "IO Exception while loading: " + e.getMessage());
        }
    }
}
