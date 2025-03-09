package org.imesense.dynamicspawncontrol.core.config.file;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.core.config.data.BlockWorldGeneratorDataAbstract;
import org.imesense.dynamicspawncontrol.core.config.data.BlockWorldGeneratorData;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
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
public final class BlockWorldGeneratorConfig extends BaseConfig
{
    /**
     *
     * @param nameConfigFile
     */
    public BlockWorldGeneratorConfig(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.TRUE);
        CodeGeneric.printInitClassToLog(this.getClass());

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
            block.setChanceSpawn(jsonObjectBlockInfo.get("chance_spawn").getAsInt());
            block.setMinHeight(jsonObjectBlockInfo.get("min_height").getAsInt());
            block.setMaxHeight(jsonObjectBlockInfo.get("max_height").getAsInt());
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
        saveBlockSettings(recordObject, BlockWorldGeneratorData.NETHER_RACK.getCategory(), BlockWorldGeneratorData.NETHER_RACK);
        saveBlockSettings(recordObject, BlockWorldGeneratorData.MOSSY_COBBLESTONE.getCategory(), BlockWorldGeneratorData.MOSSY_COBBLESTONE);
        saveBlockSettings(recordObject, BlockWorldGeneratorData.MONSTER_EGG.getCategory(), BlockWorldGeneratorData.MONSTER_EGG);

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

            loadBlockSettings(readableObject, BlockWorldGeneratorData.NETHER_RACK.getCategory(), BlockWorldGeneratorData.NETHER_RACK);
            loadBlockSettings(readableObject, BlockWorldGeneratorData.MOSSY_COBBLESTONE.getCategory(), BlockWorldGeneratorData.MOSSY_COBBLESTONE);
            loadBlockSettings(readableObject, BlockWorldGeneratorData.MONSTER_EGG.getCategory(), BlockWorldGeneratorData.MONSTER_EGG);
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