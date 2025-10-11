package org.imesense.dynamicspawncontrol.core.pluginconfig.darkness;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseJsonConfig;

@Getter
@Setter
@InitLog
@ConceptConfig(fileName = "plugin_cfg_darkness_forge_1_12_x_0_5_0")
@TODO(
        value = "Убрать лишние опции, оставить только влияение освещенности луны",
        showOnce = false,
        priority = TODO.TodoPriority.HIGH)
public final class PluginDarknessConfig extends BaseJsonConfig
{
    private int[] blacklistByID = {};
    private double[] moonPhaseFactors = {0.06, 0.04, 0.03, 0.02, 0.0, 0.01, 0.02, 0.04};

    public PluginDarknessConfig(String configPath)
    {
        super(configPath, false);
        loadOrCreateConfig();
    }

    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject jsonObject = new JsonObject();

        JsonArray blacklistByIDArray = new JsonArray();
        for (int id : blacklistByID)
        {
            blacklistByIDArray.add(id);
        }
        jsonObject.add("blacklistByID", blacklistByIDArray);

        JsonArray moonPhaseFactorsArray = new JsonArray();
        for (double factor : moonPhaseFactors)
        {
            moonPhaseFactorsArray.add(factor);
        }
        jsonObject.add("moonPhaseFactors", moonPhaseFactorsArray);

        return jsonObject;
    }

    @Override
    protected void applyConfig(JsonObject jsonObject)
    {
        if (jsonObject.has("blacklistByID"))
        {
            JsonArray blacklistByIDArray = jsonObject.getAsJsonArray("blacklistByID");
            blacklistByID = new int[blacklistByIDArray.size()];
            for (int i = 0; i < blacklistByIDArray.size(); i++)
            {
                blacklistByID[i] = blacklistByIDArray.get(i).getAsInt();
            }
        }

        if (jsonObject.has("moonPhaseFactors"))
        {
            JsonArray moonPhaseFactorsArray = jsonObject.getAsJsonArray("moonPhaseFactors");
            moonPhaseFactors = new double[moonPhaseFactorsArray.size()];
            for (int i = 0; i < moonPhaseFactorsArray.size(); i++)
            {
                moonPhaseFactors[i] = moonPhaseFactorsArray.get(i).getAsDouble();
            }
        }
    }
}
