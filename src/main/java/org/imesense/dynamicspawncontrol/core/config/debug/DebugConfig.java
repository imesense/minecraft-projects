package org.imesense.dynamicspawncontrol.core.config.debug;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.base.BaseJsonConfig;

@Getter
@Setter
@InitLog
@ConceptConfig(fileName = "cfg_debug")
public final class DebugConfig extends BaseJsonConfig
{
    private boolean showStats = false;
    private boolean showLoggingInEventLootBoxInWorld = false;

    public DebugConfig(String configPath)
    {
        super(configPath, true);

        loadOrCreateConfig();
    }

    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("show_stats", showStats);
        jsonObject.addProperty("show_logging_in_event_loot_box_in_world", showLoggingInEventLootBoxInWorld);

        return jsonObject;
    }

    @Override
    protected void applyConfig(JsonObject jsonObject)
    {
        if (jsonObject.has("show_stats"))
        {
            showStats = jsonObject.get("show_stats").getAsBoolean();
        }

        if (jsonObject.has("show_logging_in_event_loot_box_in_world"))
        {
            showLoggingInEventLootBoxInWorld =
                    jsonObject.get("show_logging_in_event_loot_box_in_world").getAsBoolean();
        }
    }
}
