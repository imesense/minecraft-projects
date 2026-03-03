package org.imesense.dynamicspawncontrol.core.config.blockworldgenerator;

import com.google.gson.JsonObject;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseJsonConfig;
import org.imesense.dynamicspawncontrol.core.logfile.Logger;

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
        JsonObject jsonObject1 = new JsonObject();

        jsonObject1.addProperty("chance_spawn",
                blockWorldGeneratorDataAbstract.getChanceSpawn());

        jsonObject1.addProperty("min_height",
                blockWorldGeneratorDataAbstract.getMinHeight());

        jsonObject1.addProperty("max_height",
                blockWorldGeneratorDataAbstract.getMaxHeight());

        jsonObject.add(blockWorldGeneratorDataAbstract.getCategory(),
                jsonObject1);
    }

    private void loadBlockSettings(JsonObject jsonObject,
                                   BlockWorldGeneratorDataAbstract blockWorldGeneratorDataAbstract)
    {
        if (jsonObject.has(blockWorldGeneratorDataAbstract.getCategory()))
        {
            JsonObject jsonObject1 =
                    jsonObject.getAsJsonObject(blockWorldGeneratorDataAbstract.getCategory());

            blockWorldGeneratorDataAbstract.
                    setChanceSpawn(jsonObject1.get("chance_spawn").getAsInt());

            blockWorldGeneratorDataAbstract.
                    setMinHeight(jsonObject1.get("min_height").getAsInt());

            blockWorldGeneratorDataAbstract.
                    setMaxHeight(jsonObject1.get("max_height").getAsInt());
        }
        else
        {
            Logger.error(blockWorldGeneratorDataAbstract.getCategory() + " is missing in the config file.");
        }
    }
}
