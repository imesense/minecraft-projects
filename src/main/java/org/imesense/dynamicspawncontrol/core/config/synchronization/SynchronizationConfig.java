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
        JsonObject jsonObject1 = new JsonObject();
        JsonObject jsonObject2 = new JsonObject();

        jsonObject1.addProperty("ticks_between_checks", ticksBetweenChecks);
        jsonObject1.addProperty("checks_per_player", checksPerPlayer);
        jsonObject1.addProperty("growth_chance", growthChance);
        jsonObject1.addProperty("player_radius", playerRadius);

        jsonObject2.add("ThreadOvergrowingGrass", jsonObject1);

        jsonObject.add("thread_configs", jsonObject2);

        return jsonObject;
    }

    @Override
    protected void applyConfig(JsonObject jsonObject)
    {
        if (jsonObject.has("thread_configs"))
        {
            JsonObject jsonObject1 = jsonObject.getAsJsonObject("thread_configs");

            if (jsonObject1.has("ThreadOvergrowingGrass"))
            {
                JsonObject jsonObject2 = jsonObject1.getAsJsonObject("ThreadOvergrowingGrass");

                if (jsonObject2.has("ticks_between_checks"))
                {
                    ticksBetweenChecks = jsonObject2.get("ticks_between_checks").getAsInt();
                }

                if (jsonObject2.has("checks_per_player"))
                {
                    checksPerPlayer = jsonObject2.get("checks_per_player").getAsInt();
                }

                if (jsonObject2.has("growth_chance"))
                {
                    growthChance = jsonObject2.get("growth_chance").getAsDouble();
                }

                if (jsonObject2.has("player_radius"))
                {
                    playerRadius = jsonObject2.get("player_radius").getAsInt();
                }
            }
        }
    }
}
