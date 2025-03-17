package org.imesense.dynamicspawncontrol.core.config.fileLegacy;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.core.config.dataLegacy.PlayerData;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseConfig;
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

@ConceptConfig(fileName = "cfg_player")
public final class PlayerConceptConfig extends BaseConfig
{
    public PlayerConceptConfig(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.TRUE);

		CodeGeneric.printInitClassToLog(this.getClass());

        PlayerData.ConfigDataPlayer.Instance =
                new PlayerData.ConfigDataPlayer("player");

        if (Files.exists(Paths.get(this.nameConfig)))
        {
            this.loadFromFile();
        }
        else
        {
            this.saveToFile();
        }
    }

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
        JsonObject jsonObjectPlayer = new JsonObject();

        jsonObjectPlayer.addProperty("protected_respawn_player_radius",
                PlayerData.ConfigDataPlayer.Instance.getProtectRespawnPlayerRadius());

        recordObject.add(PlayerData.ConfigDataPlayer.Instance.getCategoryObject(), jsonObjectPlayer);

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

    @Override
    public void loadFromFile()
    {
        try (FileReader fileReader = new FileReader(this.nameConfig))
        {
            JsonElement fileReaderJsonElement = new JsonParser().parse(fileReader);
            JsonObject readableObject = fileReaderJsonElement.getAsJsonObject();

            if (readableObject.has(PlayerData.ConfigDataPlayer.Instance.getCategoryObject()))
            {
                JsonObject jsonObjectPlayer =
                        readableObject.getAsJsonObject(PlayerData.ConfigDataPlayer.Instance.getCategoryObject());

                if (jsonObjectPlayer.has("protected_respawn_player_radius"))
                {
                    PlayerData.ConfigDataPlayer.Instance.
                            setProtectRespawnPlayerRadius(jsonObjectPlayer.get("protected_respawn_player_radius").getAsShort());
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
