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

        JsonObject jsonObject = new JsonObject();
        JsonObject monitorObject = getObject();

        jsonObject.add(DataRenderNight.ConfigDataRenderNight.instance.getCategoryObject(), monitorObject);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter file = new FileWriter(this.nameConfig))
        {
            gson.toJson(jsonObject, file);
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
        JsonObject monitorObject = getJsonObject();

        JsonArray moonPhaseFactorsArray = new JsonArray();

        for (Double factor : DataRenderNight.ConfigDataRenderNight.instance.getMoonPhaseFactors())
        {
            moonPhaseFactorsArray.add(factor);
        }

        monitorObject.add("moonPhaseFactors", moonPhaseFactorsArray);

        JsonArray blacklistByIDArray = new JsonArray();

        for (Integer id : DataRenderNight.ConfigDataRenderNight.instance.getBlacklistByID())
        {
            blacklistByIDArray.add(id);
        }

        monitorObject.add("blacklistByID", blacklistByIDArray);

        JsonArray blacklistByNameArray = new JsonArray();

        for (String name : DataRenderNight.ConfigDataRenderNight.instance.getBlacklistByName())
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

        monitorObject.addProperty("darknessOverWorld", DataRenderNight.ConfigDataRenderNight.instance.getDarknessOverWorld());
        monitorObject.addProperty("darknessNether", DataRenderNight.ConfigDataRenderNight.instance.getDarknessNether());
        monitorObject.addProperty("darknessEnd", DataRenderNight.ConfigDataRenderNight.instance.getDarknessEnd());
        monitorObject.addProperty("darknessDefault", DataRenderNight.ConfigDataRenderNight.instance.getDarknessDefault());
        monitorObject.addProperty("darknessSkyLess", DataRenderNight.ConfigDataRenderNight.instance.getDarknessSkyLess());
        monitorObject.addProperty("darknessNetherFog", DataRenderNight.ConfigDataRenderNight.instance.getDarknessNetherFog());
        monitorObject.addProperty("darknessEndFog", DataRenderNight.ConfigDataRenderNight.instance.getDarknessEndFog());
        monitorObject.addProperty("ignoreMoonLight", DataRenderNight.ConfigDataRenderNight.instance.getIgnoreMoonLight());
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

            if (jsonObject.has(DataRenderNight.ConfigDataRenderNight.instance.getCategoryObject()))
            {
                JsonObject renderNightObject = jsonObject.getAsJsonObject(DataRenderNight.ConfigDataRenderNight.instance.getCategoryObject());

                if (renderNightObject.has("darknessOverWorld"))
                {
                    DataRenderNight.ConfigDataRenderNight.instance.setDarknessOverWorld(renderNightObject.get("darknessOverWorld").getAsBoolean());
                }

                if (renderNightObject.has("darknessNether"))
                {
                    DataRenderNight.ConfigDataRenderNight.instance.setDarknessNether(renderNightObject.get("darknessNether").getAsBoolean());
                }

                if (renderNightObject.has("darknessEnd"))
                {
                    DataRenderNight.ConfigDataRenderNight.instance.setDarknessEnd(renderNightObject.get("darknessEnd").getAsBoolean());
                }

                if (renderNightObject.has("darknessDefault"))
                {
                    DataRenderNight.ConfigDataRenderNight.instance.setDarknessDefault(renderNightObject.get("darknessDefault").getAsBoolean());
                }

                if (renderNightObject.has("darknessSkyLess"))
                {
                    DataRenderNight.ConfigDataRenderNight.instance.setDarknessSkyLess(renderNightObject.get("darknessSkyLess").getAsBoolean());
                }

                if (renderNightObject.has("darknessNetherFog"))
                {
                    DataRenderNight.ConfigDataRenderNight.instance.setDarknessNetherFog(renderNightObject.get("darknessNetherFog").getAsBoolean());
                }

                if (renderNightObject.has("darknessEndFog"))
                {
                    DataRenderNight.ConfigDataRenderNight.instance.setDarknessEndFog(renderNightObject.get("darknessEndFog").getAsBoolean());
                }

                if (renderNightObject.has("ignoreMoonLight"))
                {
                    DataRenderNight.ConfigDataRenderNight.instance.setIgnoreMoonLight(renderNightObject.get("ignoreMoonLight").getAsBoolean());
                }

                if (renderNightObject.has("moonPhaseFactors"))
                {
                    JsonArray moonPhaseFactorsArray = renderNightObject.getAsJsonArray("moonPhaseFactors");
                    Double[] moonPhaseFactors = new Double[moonPhaseFactorsArray.size()];

                    for (int i = 0; i < moonPhaseFactorsArray.size(); i++)
                    {
                        moonPhaseFactors[i] = moonPhaseFactorsArray.get(i).getAsDouble();
                    }

                    DataRenderNight.ConfigDataRenderNight.instance.setMoonPhaseFactors(moonPhaseFactors);
                }

                if (renderNightObject.has("blacklistByID"))
                {
                    JsonArray blacklistByIDArray = renderNightObject.getAsJsonArray("blacklistByID");
                    Integer[] blacklistByID = new Integer[blacklistByIDArray.size()];

                    for (int i = 0; i < blacklistByIDArray.size(); i++)
                    {
                        blacklistByID[i] = blacklistByIDArray.get(i).getAsInt();
                    }

                    DataRenderNight.ConfigDataRenderNight.instance.setBlacklistByID(blacklistByID);
                }

                if (renderNightObject.has("blacklistByName"))
                {
                    JsonArray blacklistByNameArray = renderNightObject.getAsJsonArray("blacklistByName");
                    String[] blacklistByName = new String[blacklistByNameArray.size()];

                    for (int i = 0; i < blacklistByNameArray.size(); i++)
                    {
                        blacklistByName[i] = blacklistByNameArray.get(i).getAsString();
                    }

                    DataRenderNight.ConfigDataRenderNight.instance.setBlacklistByName(blacklistByName);
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
