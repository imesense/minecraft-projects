package org.imesense.dynamicspawncontrol.config.file;

import com.google.gson.*;
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
@ConceptConfig(fileName = "cfg_log_file")
public final class LogFileConfig extends AbstractConceptConfig
{
    /**
     *
     * @param nameConfigFile
     */
    public LogFileConfig(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.TRUE);

		CodeGeneric.printInitClassToLog(this.getClass());

        org.imesense.dynamicspawncontrol.config.data.LogFileData.ConfigDataLogFile.Instance =
                new org.imesense.dynamicspawncontrol.config.data.LogFileData.ConfigDataLogFile("log_file");

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
        JsonObject jsonObjectLogFile = new JsonObject();

        jsonObjectLogFile.addProperty("max_lines",
                org.imesense.dynamicspawncontrol.config.data.LogFileData.ConfigDataLogFile.Instance.getLogMaxLines());

        recordObject.add(org.imesense.dynamicspawncontrol.config.data.LogFileData.ConfigDataLogFile.Instance.getCategoryObject(), jsonObjectLogFile);

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

            if (readableObject.has(org.imesense.dynamicspawncontrol.config.data.LogFileData.ConfigDataLogFile.Instance.getCategoryObject()))
            {
                JsonObject jsonObjectLogFile =
                        readableObject.getAsJsonObject(org.imesense.dynamicspawncontrol.config.data.LogFileData.ConfigDataLogFile.Instance.
                                getCategoryObject());

                if (jsonObjectLogFile.has("max_lines"))
                {
                    org.imesense.dynamicspawncontrol.config.data.LogFileData.ConfigDataLogFile.Instance.
                            setLogMaxLines(jsonObjectLogFile.get("max_lines").getAsShort());
                }
            }
            else
            {
                Log.writeDataToLogFile(2, "'log_file' is missing in the config file.");
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
