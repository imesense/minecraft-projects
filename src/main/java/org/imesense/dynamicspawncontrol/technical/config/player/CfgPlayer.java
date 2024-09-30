package org.imesense.dynamicspawncontrol.technical.config.player;

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
@DCSSingleConfig(fileName = "cfg_player.json")
public final class CfgPlayer extends CfgClassAbstract
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgPlayer(String nameConfigFile)
    {
        super(nameConfigFile);

        DataPlayer.player.instance = new DataPlayer.player("player");

        if (Files.exists(Paths.get(this.nameConfig)))
        {
            loadFromFile();
        }
        else
        {
            Log.writeDataToLogFile(0, "Config file does not exist. Creating a new one.");
            saveToFile();
        }

        CodeGenericUtils.printInitClassToLog(CfgPlayer.class);
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
        JsonObject monitorObject = new JsonObject();

        monitorObject.addProperty("protected_respawn_player_radius", DataPlayer.player.instance.getProtectRespawnPlayerRadius());
        jsonObject.add("player", monitorObject);

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
     */
    @Override
    public void loadFromFile()
    {
        try (FileReader reader = new FileReader(this.nameConfig))
        {
            JsonElement jsonElement = new JsonParser().parse(reader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject.has("player"))
            {
                JsonObject gameWorldTime = jsonObject.getAsJsonObject("player");

                if (gameWorldTime.has("protected_respawn_player_radius"))
                {
                    DataPlayer.player.instance.setProtectRespawnPlayerRadius(gameWorldTime.get("protected_respawn_player_radius").getAsShort());
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
