package org.imesense.dynamicspawncontrol.core.config.player;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseJsonConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@Getter
@Setter
@ConceptConfig(fileName = "cfg_player")
public final class PlayerConfig extends BaseJsonConfig
{
    private short protectRespawnPlayerRadius = 15;

    public PlayerConfig(String nameConfigFile)
    {
        super(nameConfigFile, true);

        CodeGeneric.printInitClassToLog(this.getClass());

        loadOrCreateConfig();
    }

    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject config = new JsonObject();

        config.addProperty("protected_respawn_player_radius", protectRespawnPlayerRadius);

        return config;
    }

    @Override
    protected void applyConfig(JsonObject jsonObject)
    {
        if (jsonObject.has("protected_respawn_player_radius"))
        {
            protectRespawnPlayerRadius = jsonObject.get("protected_respawn_player_radius").getAsShort();
        }
    }
}
