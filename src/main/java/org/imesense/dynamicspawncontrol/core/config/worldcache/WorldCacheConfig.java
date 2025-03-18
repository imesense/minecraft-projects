package org.imesense.dynamicspawncontrol.core.config.worldcache;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseJsonConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@Getter
@Setter
@ConceptConfig(fileName = "cfg_cache_world")
public final class WorldCacheConfig extends BaseJsonConfig
{
    public WorldCacheConfig(String configPath)
    {
        super(configPath, true);

        CodeGeneric.printInitClassToLog(this.getClass());

        loadOrCreateConfig();
    }

    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject jsonObject = new JsonObject();

        return jsonObject;
    }

    @Override
    protected void applyConfig(JsonObject jsonObject)
    {

    }
}
