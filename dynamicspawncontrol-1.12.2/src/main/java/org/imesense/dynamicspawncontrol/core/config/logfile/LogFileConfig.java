package org.imesense.dynamicspawncontrol.core.config.logfile;

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
@ConceptConfig(fileName = "cfg_log_file")
public final class LogFileConfig extends BaseJsonConfig
{
    private short logMaxLines = Short.MAX_VALUE;

    public LogFileConfig(String configPath)
    {
        super(configPath, true);

        loadOrCreateConfig();
    }


    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("log_max_lines", logMaxLines);

        return jsonObject;
    }

    @Override
    protected void applyConfig(JsonObject jsonObject)
    {
        if (jsonObject.has("log_max_lines"))
        {
            logMaxLines = jsonObject.get("log_max_lines").getAsShort();
        }
    }
}
