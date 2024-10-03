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

		CodeGenericUtils.printInitClassToLog(this.getClass());

        DataPlayer.ConfigDataPlayer.instance = new DataPlayer.ConfigDataPlayer("player");

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

        monitorObject.addProperty("protected_respawn_player_radius", DataPlayer.ConfigDataPlayer.instance.getProtectRespawnPlayerRadius());
        jsonObject.add(DataPlayer.ConfigDataPlayer.instance.getCategoryObject(), monitorObject);

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

            if (jsonObject.has(DataPlayer.ConfigDataPlayer.instance.getCategoryObject()))
            {
                JsonObject gameWorldTime = jsonObject.getAsJsonObject(DataPlayer.ConfigDataPlayer.instance.getCategoryObject());

                if (gameWorldTime.has("protected_respawn_player_radius"))
                {
                    DataPlayer.ConfigDataPlayer.instance.setProtectRespawnPlayerRadius(gameWorldTime.get("protected_respawn_player_radius").getAsShort());
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
