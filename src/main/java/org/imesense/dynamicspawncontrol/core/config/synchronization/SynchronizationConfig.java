package org.imesense.dynamicspawncontrol.core.config.synchronization;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseJsonConfig;

@Getter
@Setter
@InitLog
@ConceptConfig(fileName = "cfg_synchronization")
public final class SynchronizationConfig extends BaseJsonConfig
{
    @Getter
    private int ticksBetweenChecks = 45;

    @Getter
    private int checksPerPlayer = 5;

    @Getter
    private double growthChance = 0.15;

    @Getter
    private int playerRadius = 36;

    public SynchronizationConfig(String configPath)
    {
        super(configPath, true);
        loadOrCreateConfig();
    }

    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject jsonObject = new JsonObject();

        JsonObject grassConfig = new JsonObject();
        grassConfig.addProperty("ticks_between_checks", ticksBetweenChecks);
        grassConfig.addProperty("checks_per_player", checksPerPlayer);
        grassConfig.addProperty("growth_chance", growthChance);
        grassConfig.addProperty("player_radius", playerRadius);

        JsonObject threadConfigs = new JsonObject();
        threadConfigs.add("ThreadOvergrowingGrass", grassConfig);

        jsonObject.add("thread_configs", threadConfigs);

        return jsonObject;
    }

    @Override
    protected void applyConfig(JsonObject jsonObject)
    {
        if (jsonObject.has("thread_configs"))
        {
            JsonObject threadConfigs = jsonObject.getAsJsonObject("thread_configs");

            if (threadConfigs.has("ThreadOvergrowingGrass"))
            {
                JsonObject grassConfig = threadConfigs.getAsJsonObject("ThreadOvergrowingGrass");

                if (grassConfig.has("ticks_between_checks"))
                {
                    ticksBetweenChecks = grassConfig.get("ticks_between_checks").getAsInt();
                }

                if (grassConfig.has("checks_per_player"))
                {
                    checksPerPlayer = grassConfig.get("checks_per_player").getAsInt();
                }

                if (grassConfig.has("growth_chance"))
                {
                    growthChance = grassConfig.get("growth_chance").getAsDouble();
                }

                if (grassConfig.has("player_radius"))
                {
                    playerRadius = grassConfig.get("player_radius").getAsInt();
                }
            }
        }
    }
}
