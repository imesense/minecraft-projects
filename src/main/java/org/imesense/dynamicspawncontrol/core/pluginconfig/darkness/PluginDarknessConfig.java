package org.imesense.dynamicspawncontrol.core.pluginconfig.darkness;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseJsonConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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
    private boolean darknessOverWorld = true; // deleted later
    private boolean darknessNether = true; // deleted later
    private boolean darknessEnd = true; // deleted later
    private boolean darknessDefault = true; // deleted later
    private boolean darknessSkyLess = true; // deleted later

    private boolean ignoreMoonLight = false; // deleted later
    private boolean invertBlacklist = false; // deleted later

    private int[] blacklistByID = {}; // deleted later
    private double[] moonPhaseFactors = {0.6, 0.4, 0.3, 0.2, 0.0, 0.1, 0.2, 0.4};
    private String[] blacklistByName = {}; // deleted later

    public PluginDarknessConfig(String configPath)
    {
        super(configPath, false);

        loadOrCreateConfig();
    }

    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("darknessOverWorld", darknessOverWorld);
        jsonObject.addProperty("darknessNether", darknessNether);
        jsonObject.addProperty("darknessEnd", darknessEnd);
        jsonObject.addProperty("darknessDefault", darknessDefault);
        jsonObject.addProperty("darknessSkyLess", darknessSkyLess);
        jsonObject.addProperty("ignoreMoonLight", ignoreMoonLight);
        jsonObject.addProperty("invertBlacklist", invertBlacklist);

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

        JsonArray blacklistByNameArray = new JsonArray();

        for (String name : blacklistByName)
        {
            blacklistByNameArray.add(name);
        }

        jsonObject.add("blacklistByName", blacklistByNameArray);

        return jsonObject;
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
