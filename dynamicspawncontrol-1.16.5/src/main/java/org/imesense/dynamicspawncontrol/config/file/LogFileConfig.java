package org.imesense.dynamicspawncontrol.config.file;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.config.data.LogFileData;
import org.imesense.dynamicspawncontrol.core.Config;
import org.imesense.dynamicspawncontrol.core.LogFile;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

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
@ConceptConfig(fileName = "log_file")
public final class LogFileConfig extends Config
{
    /**
     *
     * @param nameConfigFile
     */
    public LogFileConfig(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.TRUE);

        CodeGeneric.printInitClassToLog(this.getClass());

        LogFileData.ConfigDataLogFile.Instance =
                new LogFileData.ConfigDataLogFile("settings");

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
                LogFileData.ConfigDataLogFile.Instance.getLogMaxLines());

        recordObject.add(LogFileData.ConfigDataLogFile.Instance.getCategoryObject(), jsonObjectLogFile);

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

            if (readableObject.has(LogFileData.ConfigDataLogFile.Instance.getCategoryObject()))
            {
                JsonObject jsonObjectLogFile =
                        readableObject.getAsJsonObject(LogFileData.ConfigDataLogFile.Instance.
                                getCategoryObject());

                if (jsonObjectLogFile.has("max_lines"))
                {
                    LogFileData.ConfigDataLogFile.Instance.
                            setLogMaxLines(jsonObjectLogFile.get("max_lines").getAsShort());
                }
            }
            else
            {
                LogFile.writeDataToLogFile(2, "settings is missing in the config file.");
            }
        }
        catch (FileNotFoundException exception)
        {
            LogFile.writeDataToLogFile(2, "File not found: " + exception.getMessage());
        }
        catch (IOException exception)
        {
            LogFile.writeDataToLogFile(2, "IO Exception while loading: " + exception.getMessage());
        }
    }
}
