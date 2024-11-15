package org.imesense.dynamicspawncontrol.config.file;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.config.data.BlockWorldGeneratorDataAbstract;
import org.imesense.dynamicspawncontrol.config.data.BlockWorldGeneratorData;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.api.AConfig;
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
@ConceptConfig(fileName = "cfg_block_world_generator")
public final class BlockWorldGeneratorAConfig extends AConfig
{
    /**
     *
     * @param nameConfigFile
     */
    public BlockWorldGeneratorAConfig(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.TRUE);

		CodeGeneric.printInitClassToLog(this.getClass());

        BlockWorldGeneratorData.InfoDataBlockNetherRack.Instance =
                new BlockWorldGeneratorData.InfoDataBlockNetherRack("settings_block_nether_rack");

        BlockWorldGeneratorData.InfoDataBlockMossyCobblestone.Instance =
                new BlockWorldGeneratorData.InfoDataBlockMossyCobblestone("settings_block_mossy_cobblestone");

        BlockWorldGeneratorData.InfoDataBlockBlockMonsterEgg.Instance =
                new BlockWorldGeneratorData.InfoDataBlockBlockMonsterEgg("settings_block_monster_egg");

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
    public void saveBlockSettings(JsonObject jsonObject, String blockName, BlockWorldGeneratorDataAbstract block)
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
    public void loadBlockSettings(JsonObject jsonObject, String blockName, BlockWorldGeneratorDataAbstract block)
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

        saveBlockSettings(recordObject, BlockWorldGeneratorData.InfoDataBlockNetherRack.Instance.getCategoryObject(),
                BlockWorldGeneratorData.InfoDataBlockNetherRack.Instance);

        saveBlockSettings(recordObject, BlockWorldGeneratorData.InfoDataBlockMossyCobblestone.Instance.getCategoryObject(),
                BlockWorldGeneratorData.InfoDataBlockMossyCobblestone.Instance);

        saveBlockSettings(recordObject, BlockWorldGeneratorData.InfoDataBlockBlockMonsterEgg.Instance.getCategoryObject(),
                BlockWorldGeneratorData.InfoDataBlockBlockMonsterEgg.Instance);

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

            loadBlockSettings(readableObject, BlockWorldGeneratorData.InfoDataBlockNetherRack.Instance.getCategoryObject(),
                    BlockWorldGeneratorData.InfoDataBlockNetherRack.Instance);

            loadBlockSettings(readableObject, BlockWorldGeneratorData.InfoDataBlockMossyCobblestone.Instance.getCategoryObject(),
                    BlockWorldGeneratorData.InfoDataBlockMossyCobblestone.Instance);

            loadBlockSettings(readableObject, BlockWorldGeneratorData.InfoDataBlockBlockMonsterEgg.Instance.getCategoryObject(),
                    BlockWorldGeneratorData.InfoDataBlockBlockMonsterEgg.Instance);
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