package org.imesense.dynamicspawncontrol.technical.config.blockgenerator;

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
@DCSSingleConfig(fileName = "cfg_block_world_generator.json")
public final class CfgBlockWorldGenerator extends CfgClassAbstract
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgBlockWorldGenerator(String nameConfigFile)
    {
        super(nameConfigFile);

        DataBlockWorldGenerator.InfoDataBlockNetherRack.instance =
                new DataBlockWorldGenerator.InfoDataBlockNetherRack("settings_block_nether_rack");

        DataBlockWorldGenerator.InfoDataBlockMossyCobblestone.instance =
                new DataBlockWorldGenerator.InfoDataBlockMossyCobblestone("settings_block_mossy_cobblestone");

        DataBlockWorldGenerator.InfoDataBlockBlockMonsterEgg.instance =
                new DataBlockWorldGenerator.InfoDataBlockBlockMonsterEgg("settings_block_monster_egg");

        if (Files.exists(Paths.get(this.nameConfig)))
        {
            loadFromFile();
        }
        else
        {
            Log.writeDataToLogFile(0, "Config file does not exist. Creating a new one.");
            saveToFile();
        }

        CodeGenericUtils.printInitClassToLog(CfgBlockWorldGenerator.class);
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

        JsonObject settingsNetherRack = new JsonObject();

        settingsNetherRack.addProperty("chance_spawn", DataBlockWorldGenerator.InfoDataBlockNetherRack.instance.getChanceSpawn());
        settingsNetherRack.addProperty("min_height", DataBlockWorldGenerator.InfoDataBlockNetherRack.instance.getMinHeight());
        settingsNetherRack.addProperty("max_height", DataBlockWorldGenerator.InfoDataBlockNetherRack.instance.getMaxHeight());

        jsonObject.add("settings_block_nether_rack", settingsNetherRack);

        JsonObject settingsMossyCobblestone = new JsonObject();

        settingsMossyCobblestone.addProperty("chance_spawn", DataBlockWorldGenerator.InfoDataBlockMossyCobblestone.instance.getChanceSpawn());
        settingsMossyCobblestone.addProperty("min_height", DataBlockWorldGenerator.InfoDataBlockMossyCobblestone.instance.getMinHeight());
        settingsMossyCobblestone.addProperty("max_height", DataBlockWorldGenerator.InfoDataBlockMossyCobblestone.instance.getMaxHeight());

        jsonObject.add("settings_block_mossy_cobblestone", settingsMossyCobblestone);

        JsonObject settingsMonsterEgg = new JsonObject();

        settingsMonsterEgg.addProperty("chance_spawn", DataBlockWorldGenerator.InfoDataBlockBlockMonsterEgg.instance.getChanceSpawn());
        settingsMonsterEgg.addProperty("min_height", DataBlockWorldGenerator.InfoDataBlockBlockMonsterEgg.instance.getMinHeight());
        settingsMonsterEgg.addProperty("max_height", DataBlockWorldGenerator.InfoDataBlockBlockMonsterEgg.instance.getMaxHeight());

        jsonObject.add("settings_block_monster_egg", settingsMonsterEgg);

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

            if (jsonObject.has("settings_block_nether_rack"))
            {
                JsonObject settingsNetherRack = jsonObject.getAsJsonObject("settings_block_nether_rack");

                if (settingsNetherRack.has("chance_spawn"))
                {
                    DataBlockWorldGenerator.InfoDataBlockNetherRack.instance.setChanceSpawn(settingsNetherRack.get("chance_spawn").getAsInt());
                }

                if (settingsNetherRack.has("min_height"))
                {
                    DataBlockWorldGenerator.InfoDataBlockNetherRack.instance.setMinHeight(settingsNetherRack.get("min_height").getAsInt());
                }

                if (settingsNetherRack.has("max_height"))
                {
                    DataBlockWorldGenerator.InfoDataBlockNetherRack.instance.setMaxHeight(settingsNetherRack.get("max_height").getAsInt());
                }
            }
            else
            {
                Log.writeDataToLogFile(2, "settings_block_nether_rack is missing in the config file.");
            }

            if (jsonObject.has("settings_block_mossy_cobblestone"))
            {
                JsonObject settingsMossyCobblestone = jsonObject.getAsJsonObject("settings_block_mossy_cobblestone");
                if (settingsMossyCobblestone.has("chance_spawn"))
                {
                    DataBlockWorldGenerator.InfoDataBlockMossyCobblestone.instance.setChanceSpawn(settingsMossyCobblestone.get("chance_spawn").getAsInt());
                }

                if (settingsMossyCobblestone.has("min_height"))
                {
                    DataBlockWorldGenerator.InfoDataBlockMossyCobblestone.instance.setMinHeight(settingsMossyCobblestone.get("min_height").getAsInt());
                }

                if (settingsMossyCobblestone.has("max_height"))
                {
                    DataBlockWorldGenerator.InfoDataBlockMossyCobblestone.instance.setMaxHeight(settingsMossyCobblestone.get("max_height").getAsInt());
                }
            }
            else
            {
                Log.writeDataToLogFile(2, "settings_block_mossy_cobblestone is missing in the config file.");
            }

            if (jsonObject.has("settings_block_monster_egg"))
            {
                JsonObject settingsMonsterEgg = jsonObject.getAsJsonObject("settings_block_monster_egg");

                if (settingsMonsterEgg.has("chance_spawn"))
                {
                    DataBlockWorldGenerator.InfoDataBlockBlockMonsterEgg.instance.setChanceSpawn(settingsMonsterEgg.get("chance_spawn").getAsInt());
                }

                if (settingsMonsterEgg.has("min_height"))
                {
                    DataBlockWorldGenerator.InfoDataBlockBlockMonsterEgg.instance.setMinHeight(settingsMonsterEgg.get("min_height").getAsInt());
                }

                if (settingsMonsterEgg.has("max_height"))
                {
                    DataBlockWorldGenerator.InfoDataBlockBlockMonsterEgg.instance.setMaxHeight(settingsMonsterEgg.get("max_height").getAsInt());
                }
            }
            else
            {
                Log.writeDataToLogFile(2, "settings_block_monster_egg is missing in the config file.");
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