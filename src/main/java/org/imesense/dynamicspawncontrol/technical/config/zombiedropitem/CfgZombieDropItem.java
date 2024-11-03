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

        DataZombieDropItem.ConfigDataZombieDrop.Instance =
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
        JsonObject jsonObjectZombieDrop = getJsonObject();

        recordObject.add(DataZombieDropItem.ConfigDataZombieDrop.Instance.
                getCategoryObject(), jsonObjectZombieDrop);

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
     * @return
     */
    private static JsonObject getJsonObject()
    {
        JsonObject recordObject = new JsonObject();

        recordObject.addProperty("break_item",
                DataZombieDropItem.ConfigDataZombieDrop.Instance.getBreakItem());

        recordObject.addProperty("hand_item_damage_factor",
                DataZombieDropItem.ConfigDataZombieDrop.Instance.getHandItemDamageFactor());

        recordObject.addProperty("head_damage_factor",
                DataZombieDropItem.ConfigDataZombieDrop.Instance.getHeadDamageFactor());

        recordObject.addProperty("chest_damage_factor",
                DataZombieDropItem.ConfigDataZombieDrop.Instance.getChestDamageFactor());

        recordObject.addProperty("legs_damage_factor",
                DataZombieDropItem.ConfigDataZombieDrop.Instance.getLegsDamageFactor());

        recordObject.addProperty("feet_damage_factor",
                DataZombieDropItem.ConfigDataZombieDrop.Instance.getFeetDamageFactor());

        recordObject.addProperty("damage_spread_factor",
                DataZombieDropItem.ConfigDataZombieDrop.Instance.getDamageSpreadFactor());

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

            if (readableObject.has(DataZombieDropItem.ConfigDataZombieDrop.Instance.getCategoryObject()))
            {
                JsonObject jsonObjectZombieDrop =
                        readableObject.getAsJsonObject(DataZombieDropItem.ConfigDataZombieDrop.Instance.getCategoryObject());

                if (jsonObjectZombieDrop.has("break_item"))
                {
                    DataZombieDropItem.ConfigDataZombieDrop.Instance.
                            setBreakItem(jsonObjectZombieDrop.get("break_item").getAsFloat());
                }

                if (jsonObjectZombieDrop.has("hand_item_damage_factor"))
                {
                    DataZombieDropItem.ConfigDataZombieDrop.Instance.
                            setHandItemDamageFactor(jsonObjectZombieDrop.get("hand_item_damage_factor").getAsFloat());
                }

                if (jsonObjectZombieDrop.has("head_damage_factor"))
                {
                    DataZombieDropItem.ConfigDataZombieDrop.Instance.
                            setHeadDamageFactor(jsonObjectZombieDrop.get("head_damage_factor").getAsFloat());
                }

                if (jsonObjectZombieDrop.has("chest_damage_factor"))
                {
                    DataZombieDropItem.ConfigDataZombieDrop.Instance.
                            setChestDamageFactor(jsonObjectZombieDrop.get("chest_damage_factor").getAsFloat());
                }

                if (jsonObjectZombieDrop.has("legs_damage_factor"))
                {
                    DataZombieDropItem.ConfigDataZombieDrop.Instance.
                            setLegsDamageFactor(jsonObjectZombieDrop.get("legs_damage_factor").getAsFloat());
                }

                if (jsonObjectZombieDrop.has("feet_damage_factor"))
                {
                    DataZombieDropItem.ConfigDataZombieDrop.Instance.
                            setFeetDamageFactor(jsonObjectZombieDrop.get("feet_damage_factor").getAsFloat());
                }

                if (jsonObjectZombieDrop.has("damage_spread_factor"))
                {
                    DataZombieDropItem.ConfigDataZombieDrop.Instance.
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