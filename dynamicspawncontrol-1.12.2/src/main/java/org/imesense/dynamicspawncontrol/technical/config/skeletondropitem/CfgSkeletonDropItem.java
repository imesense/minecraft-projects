package org.imesense.dynamicspawncontrol.technical.config.skeletondropitem;

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
@DCSSingleConfig(fileName = "cfg_skeleton_drop_item.json")
public final class CfgSkeletonDropItem extends CfgClassAbstract
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgSkeletonDropItem(String nameConfigFile)
    {
        super(nameConfigFile);

        CodeGenericUtils.printInitClassToLog(this.getClass());

        DataSkeletonDropItem.ConfigDataSkeletonDrop.instance =
                new DataSkeletonDropItem.ConfigDataSkeletonDrop("skeleton_drop");

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
        JsonObject jsonObjectSkeletonDrop = getJsonObject();

        recordObject.add(DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.
                getCategoryObject(), jsonObjectSkeletonDrop);

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
                DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getBreakItem());

        recordObject.addProperty("hand_item_damage_factor",
                DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getHandItemDamageFactor());

        recordObject.addProperty("head_damage_factor",
                DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getHeadDamageFactor());

        recordObject.addProperty("chest_damage_factor",
                DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getChestDamageFactor());

        recordObject.addProperty("legs_damage_factor",
                DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getLegsDamageFactor());

        recordObject.addProperty("feet_damage_factor",
                DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getFeetDamageFactor());

        recordObject.addProperty("damage_spread_factor",
                DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getDamageSpreadFactor());

        recordObject.addProperty("arrows_to_drops",
                DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getArrowsToDrops());

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

            if (readableObject.has(DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getCategoryObject()))
            {
                JsonObject jsonObjectSkeletonDrop =
                        readableObject.getAsJsonObject(DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getCategoryObject());

                if (jsonObjectSkeletonDrop.has("break_item"))
                {
                    DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.
                            setBreakItem(jsonObjectSkeletonDrop.get("break_item").getAsFloat());
                }

                if (jsonObjectSkeletonDrop.has("hand_item_damage_factor"))
                {
                    DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.
                            setHandItemDamageFactor(jsonObjectSkeletonDrop.get("hand_item_damage_factor").getAsFloat());
                }

                if (jsonObjectSkeletonDrop.has("head_damage_factor"))
                {
                    DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.
                            setHeadDamageFactor(jsonObjectSkeletonDrop.get("head_damage_factor").getAsFloat());
                }

                if (jsonObjectSkeletonDrop.has("chest_damage_factor"))
                {
                    DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.
                            setChestDamageFactor(jsonObjectSkeletonDrop.get("chest_damage_factor").getAsFloat());
                }

                if (jsonObjectSkeletonDrop.has("legs_damage_factor"))
                {
                    DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.
                            setLegsDamageFactor(jsonObjectSkeletonDrop.get("legs_damage_factor").getAsFloat());
                }

                if (jsonObjectSkeletonDrop.has("feet_damage_factor"))
                {
                    DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.
                            setFeetDamageFactor(jsonObjectSkeletonDrop.get("feet_damage_factor").getAsFloat());
                }

                if (jsonObjectSkeletonDrop.has("damage_spread_factor"))
                {
                    DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.
                            setDamageSpreadFactor(jsonObjectSkeletonDrop.get("damage_spread_factor").getAsFloat());
                }

                if (jsonObjectSkeletonDrop.has("arrows_to_drops"))
                {
                    DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.
                            setArrowsToDrops(jsonObjectSkeletonDrop.get("arrows_to_drops").getAsByte());
                }
            }
            else
            {
                Log.writeDataToLogFile(2, "'skeleton_drop' section is missing in the config file.");
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
