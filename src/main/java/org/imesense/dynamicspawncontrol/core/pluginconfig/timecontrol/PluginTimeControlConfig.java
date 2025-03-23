package org.imesense.dynamicspawncontrol.core.pluginconfig.timecontrol;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseJsonConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@Getter
@Setter
@InitLog
@ConceptConfig(fileName = "plugin_cfg_time_control_mod_forge_1_12_2")
public final class PluginTimeControlConfig extends BaseJsonConfig
{
    private int dayLengthMinutes = 10;
    private int nightLengthMinutes = 10;
    private int syncToSystemTimeRate = 20;
    private boolean timeControlDebug = false;
    private boolean syncToSystemTime = false;

    public PluginTimeControlConfig(String configPath)
    {
        super(configPath, false);

        loadOrCreateConfig();
    }

    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("day_length_minutes", dayLengthMinutes);
        jsonObject.addProperty("night_length_minutes", nightLengthMinutes);
        jsonObject.addProperty("sync_to_system_time_rate", syncToSystemTimeRate);
        jsonObject.addProperty("time_control_debug", timeControlDebug);
        jsonObject.addProperty("sync_to_system_time", syncToSystemTime);

        return jsonObject;
    }

    @Override
    protected void applyConfig(JsonObject jsonObject)
    {
        if (jsonObject.has("day_length_minutes"))
        {
            dayLengthMinutes = jsonObject.get("day_length_minutes").getAsInt();
        }

        if (jsonObject.has("night_length_minutes"))
        {
            nightLengthMinutes = jsonObject.get("night_length_minutes").getAsInt();
        }

        if (jsonObject.has("sync_to_system_time_rate"))
        {
            syncToSystemTimeRate = jsonObject.get("sync_to_system_time_rate").getAsInt();
        }

        if (jsonObject.has("time_control_debug"))
        {
            timeControlDebug = jsonObject.get("time_control_debug").getAsBoolean();
        }

        if (jsonObject.has("sync_to_system_time"))
        {
            syncToSystemTime = jsonObject.get("sync_to_system_time").getAsBoolean();
        }
    }
}
