package org.imesense.dynamicspawncontrol.technical.config.blockgenerator;

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
@DCSSingleConfig(fileName = "cfg_block_world_generator")
public final class CfgBlockWorldGenerator extends CfgClassAbstract
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgBlockWorldGenerator(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.TRUE);

		CodeGenericUtil.printInitClassToLog(this.getClass());

        DataBlockWorldGenerator.InfoDataBlockNetherRack.Instance =
                new DataBlockWorldGenerator.InfoDataBlockNetherRack("settings_block_nether_rack");

        DataBlockWorldGenerator.InfoDataBlockMossyCobblestone.Instance =
                new DataBlockWorldGenerator.InfoDataBlockMossyCobblestone("settings_block_mossy_cobblestone");

        DataBlockWorldGenerator.InfoDataBlockBlockMonsterEgg.Instance =
                new DataBlockWorldGenerator.InfoDataBlockBlockMonsterEgg("settings_block_monster_egg");

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
     * @param jsonObject
     * @param blockName
     * @param block
     */
    public void saveBlockSettings(JsonObject jsonObject, String blockName, ConfigDataBlock block)
    {
        JsonObject jsonObjectBlockInfo = new JsonObject();

        jsonObjectBlockInfo.addProperty("chance_spawn", block.getChanceSpawn());
        jsonObjectBlockInfo.addProperty("min_height", block.getMinHeight());
        jsonObjectBlockInfo.addProperty("max_height", block.getMaxHeight());

        jsonObject.add(blockName, jsonObjectBlockInfo);
    }

    /**
     *
     * @param jsonObject
     * @param blockName
     * @param block
     */
    public void loadBlockSettings(JsonObject jsonObject, String blockName, ConfigDataBlock block)
    {
        if (jsonObject.has(blockName))
        {
            JsonObject jsonObjectBlockInfo = jsonObject.getAsJsonObject(blockName);

            if (jsonObjectBlockInfo.has("chance_spawn"))
            {
                block.setChanceSpawn(jsonObjectBlockInfo.get("chance_spawn").getAsInt());
            }

            if (jsonObjectBlockInfo.has("min_height"))
            {
                block.setMinHeight(jsonObjectBlockInfo.get("min_height").getAsInt());
            }

            if (jsonObjectBlockInfo.has("max_height"))
            {
                block.setMaxHeight(jsonObjectBlockInfo.get("max_height").getAsInt());
            }
        }
        else
        {
            Log.writeDataToLogFile(2, blockName + " is missing in the config file.");
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

        saveBlockSettings(recordObject, DataBlockWorldGenerator.InfoDataBlockNetherRack.Instance.getCategoryObject(),
                DataBlockWorldGenerator.InfoDataBlockNetherRack.Instance);

        saveBlockSettings(recordObject, DataBlockWorldGenerator.InfoDataBlockNetherRack.Instance.getCategoryObject(),
                DataBlockWorldGenerator.InfoDataBlockMossyCobblestone.Instance);

        saveBlockSettings(recordObject, DataBlockWorldGenerator.InfoDataBlockNetherRack.Instance.getCategoryObject(),
                DataBlockWorldGenerator.InfoDataBlockBlockMonsterEgg.Instance);

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

            loadBlockSettings(readableObject, DataBlockWorldGenerator.InfoDataBlockNetherRack.Instance.getCategoryObject(),
                    DataBlockWorldGenerator.InfoDataBlockNetherRack.Instance);

            loadBlockSettings(readableObject, DataBlockWorldGenerator.InfoDataBlockNetherRack.Instance.getCategoryObject(),
                    DataBlockWorldGenerator.InfoDataBlockMossyCobblestone.Instance);

            loadBlockSettings(readableObject, DataBlockWorldGenerator.InfoDataBlockNetherRack.Instance.getCategoryObject(),
                    DataBlockWorldGenerator.InfoDataBlockBlockMonsterEgg.Instance);
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