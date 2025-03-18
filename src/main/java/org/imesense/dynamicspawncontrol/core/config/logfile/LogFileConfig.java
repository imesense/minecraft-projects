package org.imesense.dynamicspawncontrol.core.config.logfile;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseConfigLegacy;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseJsonConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@Getter
@Setter
@ConceptConfig(fileName = "cfg_log_file")
public final class LogFileConfig extends BaseJsonConfig
{
    private short logMaxLines = Short.MAX_VALUE;

    public LogFileConfig(String nameConfigFile)
    {
        super(nameConfigFile, true);

        CodeGeneric.printInitClassToLog(this.getClass());

        loadOrCreateConfig();
    }


    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject config = new JsonObject();

        config.addProperty("log_max_lines", logMaxLines);

        return config;
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
