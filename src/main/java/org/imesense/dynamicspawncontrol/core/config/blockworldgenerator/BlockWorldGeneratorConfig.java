package org.imesense.dynamicspawncontrol.core.config.blockworldgenerator;

import com.google.gson.JsonObject;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseJsonConfig;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
@ConceptConfig(fileName = "cfg_block_world_generator")
public final class BlockWorldGeneratorConfig extends BaseJsonConfig
{
    public BlockWorldGeneratorConfig(String configPath)
    {
        super(configPath, true);

        loadOrCreateConfig();
    }

    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject jsonObject = new JsonObject();

        saveBlockSettings(jsonObject, BlockWorldGeneratorData.NETHER_RACK);
        saveBlockSettings(jsonObject, BlockWorldGeneratorData.MOSSY_COBBLESTONE);
        saveBlockSettings(jsonObject, BlockWorldGeneratorData.MONSTER_EGG);
        saveBlockSettings(jsonObject, BlockWorldGeneratorData.EMERALD_ORE);

        return jsonObject;
    }

    @Override
    protected void applyConfig(JsonObject jsonObject)
    {
        loadBlockSettings(jsonObject, BlockWorldGeneratorData.NETHER_RACK);
        loadBlockSettings(jsonObject, BlockWorldGeneratorData.MOSSY_COBBLESTONE);
        loadBlockSettings(jsonObject, BlockWorldGeneratorData.MONSTER_EGG);
        loadBlockSettings(jsonObject, BlockWorldGeneratorData.EMERALD_ORE);
    }

    private void saveBlockSettings(JsonObject jsonObject,
                                   BlockWorldGeneratorDataAbstract blockWorldGeneratorDataAbstract)
    {
        JsonObject blockSettings = new JsonObject();

        blockSettings.addProperty("chance_spawn",
                blockWorldGeneratorDataAbstract.getChanceSpawn());

        blockSettings.addProperty("min_height",
                blockWorldGeneratorDataAbstract.getMinHeight());

        blockSettings.addProperty("max_height",
                blockWorldGeneratorDataAbstract.getMaxHeight());

        jsonObject.add(blockWorldGeneratorDataAbstract.getCategory(),
                blockSettings);
    }

    private void loadBlockSettings(JsonObject jsonObject,
                                   BlockWorldGeneratorDataAbstract blockWorldGeneratorDataAbstract)
    {
        if (jsonObject.has(blockWorldGeneratorDataAbstract.getCategory()))
        {
            JsonObject blockSettings =
                    jsonObject.getAsJsonObject(blockWorldGeneratorDataAbstract.getCategory());

            blockWorldGeneratorDataAbstract.
                    setChanceSpawn(blockSettings.get("chance_spawn").getAsInt());

            blockWorldGeneratorDataAbstract.
                    setMinHeight(blockSettings.get("min_height").getAsInt());

            blockWorldGeneratorDataAbstract.
                    setMaxHeight(blockSettings.get("max_height").getAsInt());
        }
        else
        {
            Log.writeDataToLogFile(2,
                    blockWorldGeneratorDataAbstract.getCategory() + " is missing in the config file.");
        }
    }
}
