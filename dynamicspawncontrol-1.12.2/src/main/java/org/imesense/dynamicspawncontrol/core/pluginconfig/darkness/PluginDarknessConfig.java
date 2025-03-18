package org.imesense.dynamicspawncontrol.core.pluginconfig.darkness;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseJsonConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Getter
@Setter
@ConceptConfig(fileName = "plugin_cfg_darkness_forge_1_12_x_0_5_0")
public final class PluginDarknessConfig extends BaseJsonConfig
{
    private boolean darknessOverWorld = true;
    private boolean darknessNether = true;
    private boolean darknessEnd = true;
    private boolean darknessDefault = true;
    private boolean darknessSkyLess = true;
    private boolean darknessNetherFog = true;
    private boolean darknessEndFog = true;
    private boolean ignoreMoonLight = false;
    private boolean invertBlacklist = false;

    private int[] blacklistByID = {};
    private double[] moonPhaseFactors = {0.6, 0.4, 0.3, 0.2, 0.0, 0.1, 0.2, 0.4};
    private String[] blacklistByName = {};

    public PluginDarknessConfig(String nameConfigFile)
    {
        super(nameConfigFile, false);

        CodeGeneric.printInitClassToLog(this.getClass());

        loadOrCreateConfig();
    }

    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject config = new JsonObject();

        config.addProperty("darknessOverWorld", darknessOverWorld);
        config.addProperty("darknessNether", darknessNether);
        config.addProperty("darknessEnd", darknessEnd);
        config.addProperty("darknessDefault", darknessDefault);
        config.addProperty("darknessSkyLess", darknessSkyLess);
        config.addProperty("darknessNetherFog", darknessNetherFog);
        config.addProperty("darknessEndFog", darknessEndFog);
        config.addProperty("ignoreMoonLight", ignoreMoonLight);
        config.addProperty("invertBlacklist", invertBlacklist);

        JsonArray blacklistByIDArray = new JsonArray();

        for (int id : blacklistByID)
        {
            blacklistByIDArray.add(id);
        }

        config.add("blacklistByID", blacklistByIDArray);

        JsonArray moonPhaseFactorsArray = new JsonArray();

        for (double factor : moonPhaseFactors)
        {
            moonPhaseFactorsArray.add(factor);
        }

        config.add("moonPhaseFactors", moonPhaseFactorsArray);

        JsonArray blacklistByNameArray = new JsonArray();

        for (String name : blacklistByName)
        {
            blacklistByNameArray.add(name);
        }

        config.add("blacklistByName", blacklistByNameArray);

        return config;
    }

    @Override
    protected void applyConfig(JsonObject jsonObject)
    {
        Map<String, Consumer<Boolean>> booleanSetters = new HashMap<>();

        booleanSetters.put("darknessOverWorld", this::setDarknessOverWorld);
        booleanSetters.put("darknessNether", this::setDarknessNether);
        booleanSetters.put("darknessEnd", this::setDarknessEnd);
        booleanSetters.put("darknessDefault", this::setDarknessDefault);
        booleanSetters.put("darknessSkyLess", this::setDarknessSkyLess);
        booleanSetters.put("darknessNetherFog", this::setDarknessNetherFog);
        booleanSetters.put("darknessEndFog", this::setDarknessEndFog);
        booleanSetters.put("ignoreMoonLight", this::setIgnoreMoonLight);
        booleanSetters.put("invertBlacklist", this::setInvertBlacklist);

        booleanSetters.forEach((key, setter) ->
        {
            if (jsonObject.has(key))
            {
                setter.accept(jsonObject.get(key).getAsBoolean());
            }
        });

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

        if (jsonObject.has("blacklistByName"))
        {
            JsonArray blacklistByNameArray = jsonObject.getAsJsonArray("blacklistByName");
            blacklistByName = new String[blacklistByNameArray.size()];

            for (int i = 0; i < blacklistByNameArray.size(); i++)
            {
                blacklistByName[i] = blacklistByNameArray.get(i).getAsString();
            }
        }
    }
}
