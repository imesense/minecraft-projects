package org.imesense.dynamicspawncontrol.technical.config.windowtitle;

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
@DCSSingleConfig(fileName = "cfg_window_title")
public final class CfgWindowTitle extends CfgClassAbstract
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgWindowTitle(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.TRUE);

		CodeGenericUtil.printInitClassToLog(this.getClass());

        DataWindowTitle.ConfigDataWindowTitle.instance =
                new DataWindowTitle.ConfigDataWindowTitle("window_title");

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
        JsonObject jsonObjectWindowTitle = new JsonObject();

        jsonObjectWindowTitle.addProperty("title",
                DataWindowTitle.ConfigDataWindowTitle.instance.getWindowTitle());

        recordObject.add(DataWindowTitle.ConfigDataWindowTitle.instance.
                getCategoryObject(), jsonObjectWindowTitle);

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
     */
    @Override
    public void loadFromFile()
    {
        try (FileReader fileReader = new FileReader(this.nameConfig))
        {
            JsonElement fileReaderJsonElement = new JsonParser().parse(fileReader);
            JsonObject readableObject = fileReaderJsonElement.getAsJsonObject();

            if (readableObject.has(DataWindowTitle.ConfigDataWindowTitle.instance.getCategoryObject()))
            {
                JsonObject jsonObjectWindowTitle =
                        readableObject.getAsJsonObject(DataWindowTitle.ConfigDataWindowTitle.
                                instance.getCategoryObject());

                if (jsonObjectWindowTitle.has("title"))
                {
                    DataWindowTitle.ConfigDataWindowTitle.instance.
                            setWindowTitle(jsonObjectWindowTitle.get("title").getAsString());
                }
            }
            else
            {
                Log.writeDataToLogFile(2, "settings_block_nether_rack is missing in the config file.");
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
