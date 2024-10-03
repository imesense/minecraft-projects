package org.imesense.dynamicspawncontrol.technical.config.zombiedropitem;

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
@DCSSingleConfig(fileName = "cfg_zombie_drop_item.json")
public final class CfgZombieDropItem extends CfgClassAbstract
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgZombieDropItem(String nameConfigFile)
    {
        super(nameConfigFile);

		CodeGenericUtils.printInitClassToLog(this.getClass());

        DataZombieDropItem.zombieDrop.instance = new DataZombieDropItem.zombieDrop("zombie_drop");

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

        monitorObject.addProperty("break_item", DataZombieDropItem.zombieDrop.instance.getBreakItem());
        monitorObject.addProperty("hand_item_damage_factor", DataZombieDropItem.zombieDrop.instance.getHandItemDamageFactor());
        monitorObject.addProperty("head_damage_factor", DataZombieDropItem.zombieDrop.instance.getHeadDamageFactor());
        monitorObject.addProperty("chest_damage_factor", DataZombieDropItem.zombieDrop.instance.getChestDamageFactor());
        monitorObject.addProperty("legs_damage_factor", DataZombieDropItem.zombieDrop.instance.getLegsDamageFactor());
        monitorObject.addProperty("feet_damage_factor", DataZombieDropItem.zombieDrop.instance.getFeetDamageFactor());
        monitorObject.addProperty("damage_spread_factor", DataZombieDropItem.zombieDrop.instance.getDamageSpreadFactor());

        jsonObject.add(DataZombieDropItem.zombieDrop.instance.getCategoryObject(), monitorObject);

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

            if (jsonObject.has(DataZombieDropItem.zombieDrop.instance.getCategoryObject()))
            {
                JsonObject zombieDropObject = jsonObject.getAsJsonObject(DataZombieDropItem.zombieDrop.instance.getCategoryObject());

                if (zombieDropObject.has("break_item"))
                {
                    DataZombieDropItem.zombieDrop.instance.setBreakItem(zombieDropObject.get("break_item").getAsFloat());
                }

                if (zombieDropObject.has("hand_item_damage_factor"))
                {
                    DataZombieDropItem.zombieDrop.instance.setHandItemDamageFactor(zombieDropObject.get("hand_item_damage_factor").getAsFloat());
                }

                if (zombieDropObject.has("head_damage_factor"))
                {
                    DataZombieDropItem.zombieDrop.instance.setHeadDamageFactor(zombieDropObject.get("head_damage_factor").getAsFloat());
                }

                if (zombieDropObject.has("chest_damage_factor"))
                {
                    DataZombieDropItem.zombieDrop.instance.setChestDamageFactor(zombieDropObject.get("chest_damage_factor").getAsFloat());
                }

                if (zombieDropObject.has("legs_damage_factor"))
                {
                    DataZombieDropItem.zombieDrop.instance.setLegsDamageFactor(zombieDropObject.get("legs_damage_factor").getAsFloat());
                }

                if (zombieDropObject.has("feet_damage_factor"))
                {
                    DataZombieDropItem.zombieDrop.instance.setFeetDamageFactor(zombieDropObject.get("feet_damage_factor").getAsFloat());
                }

                if (zombieDropObject.has("damage_spread_factor"))
                {
                    DataZombieDropItem.zombieDrop.instance.setDamageSpreadFactor(zombieDropObject.get("damage_spread_factor").getAsFloat());
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