package org.imesense.dynamicspawncontrol.core.config.blockworldgenerator;

import com.google.gson.JsonObject;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseJsonConfig;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@ConceptConfig(fileName = "cfg_block_world_generator")
public final class BlockWorldGeneratorConfig extends BaseJsonConfig
{
    public BlockWorldGeneratorConfig(String nameConfigFile)
    {
        super(nameConfigFile, true);

        CodeGeneric.printInitClassToLog(this.getClass());

        loadOrCreateConfig();
    }

    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject config = new JsonObject();

        saveBlockSettings(config, BlockWorldGeneratorData.NETHER_RACK);
        saveBlockSettings(config, BlockWorldGeneratorData.MOSSY_COBBLESTONE);
        saveBlockSettings(config, BlockWorldGeneratorData.MONSTER_EGG);

        return config;
    }

    @Override
    protected void applyConfig(JsonObject jsonObject)
    {
        loadBlockSettings(jsonObject, BlockWorldGeneratorData.NETHER_RACK);
        loadBlockSettings(jsonObject, BlockWorldGeneratorData.MOSSY_COBBLESTONE);
        loadBlockSettings(jsonObject, BlockWorldGeneratorData.MONSTER_EGG);
    }

    private void saveBlockSettings(JsonObject jsonObject, BlockWorldGeneratorDataAbstract block)
    {
        JsonObject blockSettings = new JsonObject();

        blockSettings.addProperty("chance_spawn", block.getChanceSpawn());
        blockSettings.addProperty("min_height", block.getMinHeight());
        blockSettings.addProperty("max_height", block.getMaxHeight());

        jsonObject.add(block.getCategory(), blockSettings);
    }

    private void loadBlockSettings(JsonObject jsonObject, BlockWorldGeneratorDataAbstract block)
    {
        if (jsonObject.has(block.getCategory()))
        {
            JsonObject blockSettings = jsonObject.getAsJsonObject(block.getCategory());

            block.setChanceSpawn(blockSettings.get("chance_spawn").getAsInt());
            block.setMinHeight(blockSettings.get("min_height").getAsInt());
            block.setMaxHeight(blockSettings.get("max_height").getAsInt());
        }
        else
        {
            Log.writeDataToLogFile(2, block.getCategory() + " is missing in the config file.");
        }
    }
}
