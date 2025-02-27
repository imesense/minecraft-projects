package org.imesense.dynamicspawncontrol.core.config.file;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.core.config.data.WindowTitleData;
import org.imesense.dynamicspawncontrol.core.api.AbstractConceptConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
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
@ConceptConfig(fileName = "cfg_window_title")
public final class WindowTitleConfig extends AbstractConceptConfig
{
    /**
     *
     * @param nameConfigFile
     */
    public WindowTitleConfig(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.TRUE);

		CodeGeneric.printInitClassToLog(this.getClass());

        WindowTitleData.ConfigDataWindowTitle.Instance =
                new WindowTitleData.ConfigDataWindowTitle("window_title");

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
        Path path = Paths.get(this.nameConfig).getParent();

        if (Files.notExists(path))
        {
            try
            {
                Files.createDirectories(path);
            }
            catch (IOException exception)
            {
                throw new RuntimeException(exception);
            }
        }

        JsonObject recordObject = new JsonObject();
        JsonObject jsonObjectWindowTitle = new JsonObject();

        jsonObjectWindowTitle.addProperty("title",
                WindowTitleData.ConfigDataWindowTitle.Instance.getWindowTitle());

        recordObject.add(WindowTitleData.ConfigDataWindowTitle.Instance.
                getCategoryObject(), jsonObjectWindowTitle);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter fileWriter = new FileWriter(this.nameConfig))
        {
            gson.toJson(recordObject, fileWriter);
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

            if (readableObject.has(WindowTitleData.ConfigDataWindowTitle.Instance.getCategoryObject()))
            {
                JsonObject jsonObjectWindowTitle =
                        readableObject.getAsJsonObject(WindowTitleData.ConfigDataWindowTitle.
                                Instance.getCategoryObject());

                if (jsonObjectWindowTitle.has("title"))
                {
                    WindowTitleData.ConfigDataWindowTitle.Instance.
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
