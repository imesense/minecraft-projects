package org.imesense.dynamicspawncontrol.core.config.debug;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseJsonConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@Getter
@Setter
@ConceptConfig(fileName = "cfg_debug")
public final class DebugConfig extends BaseJsonConfig
{
    private boolean showStats = false;

    public DebugConfig(String configPath)
    {
        super(configPath, true);

        CodeGeneric.printInitClassToLog(this.getClass());

        loadOrCreateConfig();
    }

    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject config = new JsonObject();

        config.addProperty("show_stats", showStats);

        return config;
    }

    @Override
    protected void applyConfig(JsonObject jsonObject)
    {
        if (jsonObject.has("show_stats"))
        {
            showStats = jsonObject.get("show_stats").getAsBoolean();
        }
    }
}
