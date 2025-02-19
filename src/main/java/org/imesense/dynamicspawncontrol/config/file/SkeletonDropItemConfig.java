package org.imesense.dynamicspawncontrol.config.file;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.config.data.SkeletonDropItemData;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.api.AbstractConceptConfig;
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
@ConceptConfig(fileName = "cfg_skeleton_drop_item")
public final class SkeletonDropItemConfig extends AbstractConceptConfig
{
    /**
     *
     * @param nameConfigFile
     */
    public SkeletonDropItemConfig(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.TRUE);

        CodeGeneric.printInitClassToLog(this.getClass());

        SkeletonDropItemData.ConfigDataSkeletonDrop.Instance =
                new SkeletonDropItemData.ConfigDataSkeletonDrop("skeleton_drop");

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
        JsonObject jsonObjectSkeletonDrop = getJsonObject();

        recordObject.add(SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.
                getCategoryObject(), jsonObjectSkeletonDrop);

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
                SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getBreakItem());

        recordObject.addProperty("hand_item_damage_factor",
                SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getHandItemDamageFactor());

        recordObject.addProperty("head_damage_factor",
                SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getHeadDamageFactor());

        recordObject.addProperty("chest_damage_factor",
                SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getChestDamageFactor());

        recordObject.addProperty("legs_damage_factor",
                SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getLegsDamageFactor());

        recordObject.addProperty("feet_damage_factor",
                SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getFeetDamageFactor());

        recordObject.addProperty("damage_spread_factor",
                SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getDamageSpreadFactor());

        recordObject.addProperty("arrows_to_drops",
                SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getArrowsToDrops());

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

            if (readableObject.has(SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getCategoryObject()))
            {
                JsonObject jsonObjectSkeletonDrop =
                        readableObject.getAsJsonObject(SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getCategoryObject());

                if (jsonObjectSkeletonDrop.has("break_item"))
                {
                    SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.
                            setBreakItem(jsonObjectSkeletonDrop.get("break_item").getAsFloat());
                }

                if (jsonObjectSkeletonDrop.has("hand_item_damage_factor"))
                {
                    SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.
                            setHandItemDamageFactor(jsonObjectSkeletonDrop.get("hand_item_damage_factor").getAsFloat());
                }

                if (jsonObjectSkeletonDrop.has("head_damage_factor"))
                {
                    SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.
                            setHeadDamageFactor(jsonObjectSkeletonDrop.get("head_damage_factor").getAsFloat());
                }

                if (jsonObjectSkeletonDrop.has("chest_damage_factor"))
                {
                    SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.
                            setChestDamageFactor(jsonObjectSkeletonDrop.get("chest_damage_factor").getAsFloat());
                }

                if (jsonObjectSkeletonDrop.has("legs_damage_factor"))
                {
                    SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.
                            setLegsDamageFactor(jsonObjectSkeletonDrop.get("legs_damage_factor").getAsFloat());
                }

                if (jsonObjectSkeletonDrop.has("feet_damage_factor"))
                {
                    SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.
                            setFeetDamageFactor(jsonObjectSkeletonDrop.get("feet_damage_factor").getAsFloat());
                }

                if (jsonObjectSkeletonDrop.has("damage_spread_factor"))
                {
                    SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.
                            setDamageSpreadFactor(jsonObjectSkeletonDrop.get("damage_spread_factor").getAsFloat());
                }

                if (jsonObjectSkeletonDrop.has("arrows_to_drops"))
                {
                    SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.
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
