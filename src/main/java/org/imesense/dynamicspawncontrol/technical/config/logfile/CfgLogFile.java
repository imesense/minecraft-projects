package org.imesense.dynamicspawncontrol.technical.config.logfile;

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
@DCSSingleConfig(fileName = "cfg_log_file")
public final class CfgLogFile extends CfgClassAbstract
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgLogFile(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.TRUE);

		CodeGenericUtil.printInitClassToLog(this.getClass());

        DataLogFile.ConfigDataLogFile.Instance =
                new DataLogFile.ConfigDataLogFile("log_file");

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
                DataLogFile.ConfigDataLogFile.Instance.getLogMaxLines());

        recordObject.add(DataLogFile.ConfigDataLogFile.Instance.getCategoryObject(), jsonObjectLogFile);

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

            if (readableObject.has(DataLogFile.ConfigDataLogFile.Instance.getCategoryObject()))
            {
                JsonObject jsonObjectLogFile =
                        readableObject.getAsJsonObject(DataLogFile.ConfigDataLogFile.Instance.
                                getCategoryObject());

                if (jsonObjectLogFile.has("max_lines"))
                {
                    DataLogFile.ConfigDataLogFile.Instance.
                            setLogMaxLines(jsonObjectLogFile.get("max_lines").getAsShort());
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
