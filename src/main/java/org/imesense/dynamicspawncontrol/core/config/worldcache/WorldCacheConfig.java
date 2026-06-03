package org.imesense.dynamicspawncontrol.core.config.worldcache;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.base.BaseJsonConfig;

@Getter
@Setter
@InitLog
@ConceptConfig(fileName = "cfg_cache_world")
public final class WorldCacheConfig extends BaseJsonConfig
{
    private boolean spawnPeacefulCreaturesAtNight = false;

    public WorldCacheConfig(String configPath)
    {
        super(configPath, true);

        loadOrCreateConfig();
    }

    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("spawn_peaceful_creatures_at_night", spawnPeacefulCreaturesAtNight);

        return jsonObject;
    }

    @Override
    protected void applyConfig(JsonObject jsonObject)
    {
        if (jsonObject.has("spawn_peaceful_creatures_at_night"))
        {
            spawnPeacefulCreaturesAtNight =
                    jsonObject.get("spawn_peaceful_creatures_at_night").getAsBoolean();
        }
    }
}
