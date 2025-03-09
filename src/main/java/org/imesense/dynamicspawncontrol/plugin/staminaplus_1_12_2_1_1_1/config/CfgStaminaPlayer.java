package org.imesense.dynamicspawncontrol.plugin.staminaplus_1_12_2_1_1_1.config;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseConfig;
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
@ConceptConfig(fileName = "cfg_staminaplus_1_12_2_1_1_1")
public final class CfgStaminaPlayer extends BaseConfig
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgStaminaPlayer(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.FALSE);

        CodeGeneric.printInitClassToLog(this.getClass());

        DataStaminaPlayer.ConfigDataStaminaPlayer.Instance =
                new DataStaminaPlayer.ConfigDataStaminaPlayer("staminaplus_1_12_2_1_1_1");

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
        JsonObject jsonObjectStaminaPlayer = new JsonObject();

        jsonObjectStaminaPlayer.addProperty("show_bar",
                DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.getShowBar());

        jsonObjectStaminaPlayer.addProperty("x_offset",
                DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.getXOffset());

        jsonObjectStaminaPlayer.addProperty("y_offset",
                DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.getYOffset());

        jsonObjectStaminaPlayer.addProperty("increase_multiplier",
                DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.getIncreaseMultiplier());

        jsonObjectStaminaPlayer.addProperty("max_stamina",
                DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.getMaxStamina());

        jsonObjectStaminaPlayer.addProperty("walking",
                DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.getWalking());

        jsonObjectStaminaPlayer.addProperty("standing",
                DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.getStanding());

        jsonObjectStaminaPlayer.addProperty("sprinting",
                DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.getSprinting());

        jsonObjectStaminaPlayer.addProperty("sneaking",
                DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.getSneaking());

        jsonObjectStaminaPlayer.addProperty("jumping",
                DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.getJumping());

        recordObject.add(DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.
                getCategoryObject(), jsonObjectStaminaPlayer);

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

            if (readableObject.has(DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.getCategoryObject()))
            {
                JsonObject jsonObjectWeb = readableObject.getAsJsonObject(
                        DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.getCategoryObject());

                if (jsonObjectWeb.has("show_bar"))
                {
                    DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.setShowBar(
                            jsonObjectWeb.get("show_bar").getAsBoolean());
                }

                if (jsonObjectWeb.has("x_offset"))
                {
                    DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.setXOffset(
                            jsonObjectWeb.get("x_offset").getAsInt());
                }

                if (jsonObjectWeb.has("y_offset"))
                {
                    DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.setYOffset(
                            jsonObjectWeb.get("y_offset").getAsInt());
                }

                if (jsonObjectWeb.has("increase_multiplier"))
                {
                    DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.setIncreaseMultiplier(
                            jsonObjectWeb.get("increase_multiplier").getAsDouble());
                }

                if (jsonObjectWeb.has("max_stamina"))
                {
                    DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.setMaxStamina(
                            jsonObjectWeb.get("max_stamina").getAsFloat());
                }

                if (jsonObjectWeb.has("walking"))
                {
                    DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.setWalking(
                            jsonObjectWeb.get("walking").getAsFloat());
                }

                if (jsonObjectWeb.has("standing"))
                {
                    DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.setStanding(
                            jsonObjectWeb.get("standing").getAsFloat());
                }

                if (jsonObjectWeb.has("sprinting"))
                {
                    DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.setSprinting(
                            jsonObjectWeb.get("sprinting").getAsFloat());
                }

                if (jsonObjectWeb.has("sneaking"))
                {
                    DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.setSneaking(
                            jsonObjectWeb.get("sneaking").getAsFloat());
                }

                if (jsonObjectWeb.has("jumping"))
                {
                    DataStaminaPlayer.ConfigDataStaminaPlayer.Instance.setJumping(
                            jsonObjectWeb.get("jumping").getAsFloat());
                }
            }
            else
            {
                Log.writeDataToLogFile(2, "'stamina_player' section is missing in the config file.");
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
