package org.imesense.dynamicspawncontrol.technical.config.zombiedropitem;

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
@DCSSingleConfig(fileName = "cfg_zombie_drop_item")
public final class CfgZombieDropItem extends CfgClassAbstract
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgZombieDropItem(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.TRUE);

		CodeGenericUtil.printInitClassToLog(this.getClass());

        DataZombieDropItem.ConfigDataZombieDrop.instance =
                new DataZombieDropItem.ConfigDataZombieDrop("zombie_drop");

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
        JsonObject jsonObjectZombieDrop = getJsonObject();

        recordObject.add(DataZombieDropItem.ConfigDataZombieDrop.instance.
                getCategoryObject(), jsonObjectZombieDrop);

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
     * @return
     */
    private static JsonObject getJsonObject()
    {
        JsonObject recordObject = new JsonObject();

        recordObject.addProperty("break_item",
                DataZombieDropItem.ConfigDataZombieDrop.instance.getBreakItem());

        recordObject.addProperty("hand_item_damage_factor",
                DataZombieDropItem.ConfigDataZombieDrop.instance.getHandItemDamageFactor());

        recordObject.addProperty("head_damage_factor",
                DataZombieDropItem.ConfigDataZombieDrop.instance.getHeadDamageFactor());

        recordObject.addProperty("chest_damage_factor",
                DataZombieDropItem.ConfigDataZombieDrop.instance.getChestDamageFactor());

        recordObject.addProperty("legs_damage_factor",
                DataZombieDropItem.ConfigDataZombieDrop.instance.getLegsDamageFactor());

        recordObject.addProperty("feet_damage_factor",
                DataZombieDropItem.ConfigDataZombieDrop.instance.getFeetDamageFactor());

        recordObject.addProperty("damage_spread_factor",
                DataZombieDropItem.ConfigDataZombieDrop.instance.getDamageSpreadFactor());

        return recordObject;
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

            if (readableObject.has(DataZombieDropItem.ConfigDataZombieDrop.instance.getCategoryObject()))
            {
                JsonObject jsonObjectZombieDrop =
                        readableObject.getAsJsonObject(DataZombieDropItem.ConfigDataZombieDrop.instance.getCategoryObject());

                if (jsonObjectZombieDrop.has("break_item"))
                {
                    DataZombieDropItem.ConfigDataZombieDrop.instance.
                            setBreakItem(jsonObjectZombieDrop.get("break_item").getAsFloat());
                }

                if (jsonObjectZombieDrop.has("hand_item_damage_factor"))
                {
                    DataZombieDropItem.ConfigDataZombieDrop.instance.
                            setHandItemDamageFactor(jsonObjectZombieDrop.get("hand_item_damage_factor").getAsFloat());
                }

                if (jsonObjectZombieDrop.has("head_damage_factor"))
                {
                    DataZombieDropItem.ConfigDataZombieDrop.instance.
                            setHeadDamageFactor(jsonObjectZombieDrop.get("head_damage_factor").getAsFloat());
                }

                if (jsonObjectZombieDrop.has("chest_damage_factor"))
                {
                    DataZombieDropItem.ConfigDataZombieDrop.instance.
                            setChestDamageFactor(jsonObjectZombieDrop.get("chest_damage_factor").getAsFloat());
                }

                if (jsonObjectZombieDrop.has("legs_damage_factor"))
                {
                    DataZombieDropItem.ConfigDataZombieDrop.instance.
                            setLegsDamageFactor(jsonObjectZombieDrop.get("legs_damage_factor").getAsFloat());
                }

                if (jsonObjectZombieDrop.has("feet_damage_factor"))
                {
                    DataZombieDropItem.ConfigDataZombieDrop.instance.
                            setFeetDamageFactor(jsonObjectZombieDrop.get("feet_damage_factor").getAsFloat());
                }

                if (jsonObjectZombieDrop.has("damage_spread_factor"))
                {
                    DataZombieDropItem.ConfigDataZombieDrop.instance.
                            setDamageSpreadFactor(jsonObjectZombieDrop.get("damage_spread_factor").getAsFloat());
                }
            }
            else
            {
                Log.writeDataToLogFile(2, "'zombie_drop' section is missing in the config file.");
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