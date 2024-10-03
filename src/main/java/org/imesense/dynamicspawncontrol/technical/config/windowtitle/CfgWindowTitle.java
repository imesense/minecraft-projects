package org.imesense.dynamicspawncontrol.technical.config.windowtitle;

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
@DCSSingleConfig(fileName = "cfg_window_title.json")
public final class CfgWindowTitle extends CfgClassAbstract
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgWindowTitle(String nameConfigFile)
    {
        super(nameConfigFile);

		CodeGenericUtils.printInitClassToLog(this.getClass());

        DataWindowTitle.windowTitle.instance = new DataWindowTitle.windowTitle("window_title");

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
        JsonObject monitorObject = new JsonObject();

        monitorObject.addProperty("title", DataWindowTitle.windowTitle.instance.getWindowTitle());

        jsonObject.add(DataWindowTitle.windowTitle.instance.getCategoryObject(), monitorObject);

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
     */
    @Override
    public void loadFromFile()
    {
        try (FileReader reader = new FileReader(this.nameConfig))
        {
            JsonElement jsonElement = new JsonParser().parse(reader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject.has(DataWindowTitle.windowTitle.instance.getCategoryObject()))
            {
                JsonObject gameWorldTime = jsonObject.getAsJsonObject(DataWindowTitle.windowTitle.instance.getCategoryObject());

                if (gameWorldTime.has("title"))
                {
                    DataWindowTitle.windowTitle.instance.setWindowTitle(gameWorldTime.get("title").getAsString());
                }

            }
            else
            {
                Log.writeDataToLogFile(2, "settings_block_nether_rack is missing in the config file.");
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
