package org.imesense.dynamicspawncontrol.core.pluginconfig.webslinger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseJsonConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ConceptConfig(fileName = "plugin_cfg_webslinger_1_12_2_2_2_4")
public class PluginWebslingerConfig extends BaseJsonConfig
{
    private boolean blockWebReplacement = true;
    private float webMeleeChance = 0.15f;
    private double slingCoolDown = 45.00;
    private float slingInaccuracy = 6.f;
    private float slingVariance = 2.f;
    private boolean slingWebbing = true;
    private boolean slingWebbingOnWeb = false;
    private int AIPrioritySlingWebs = 3;
    private String[] entityIds = { "minecraft:spider" };
    private Map<String, Integer> entityIdPriorityMap = new HashMap() {{ put("minecraft:spider", 3); }};

    public Integer searchEntityPriority(String entityId)
    {
        return entityIdPriorityMap.getOrDefault(entityId, -1);
    }

    public PluginWebslingerConfig(String nameConfigFile)
    {
        super(nameConfigFile, false);

        CodeGeneric.printInitClassToLog(this.getClass());

        loadOrCreateConfig();
    }

    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("block_web_replacement", blockWebReplacement);
        jsonObject.addProperty("web_melee_chance", webMeleeChance);
        jsonObject.addProperty("sling_coolDown", slingCoolDown);
        jsonObject.addProperty("sling_inaccuracy", slingInaccuracy);
        jsonObject.addProperty("sling_variance", slingVariance);
        jsonObject.addProperty("sling_webbing", slingWebbing);
        jsonObject.addProperty("sling_webbing_on_web", slingWebbingOnWeb);
        jsonObject.addProperty("ai_priority_sling_webs", AIPrioritySlingWebs);

        JsonArray entityIdPriorityArray = new JsonArray();

        for (Map.Entry<String, Integer> entry : entityIdPriorityMap.entrySet())
        {
            JsonObject entityObject = new JsonObject();
            entityObject.addProperty("entityId", entry.getKey());
            entityObject.addProperty("priority", entry.getValue());
            entityIdPriorityArray.add(entityObject);
        }

        jsonObject.add("entity_to_attack_web", entityIdPriorityArray);

        return jsonObject;
    }

    @Override
    protected void applyConfig(JsonObject jsonObject)
    {
        if (jsonObject.has("block_web_replacement"))
        {
            blockWebReplacement = jsonObject.get("block_web_replacement").getAsBoolean();
        }

        if (jsonObject.has("web_melee_chance"))
        {
            webMeleeChance = jsonObject.get("web_melee_chance").getAsFloat();
        }

        if (jsonObject.has("sling_coolDown"))
        {
            slingCoolDown = jsonObject.get("sling_coolDown").getAsDouble();
        }

        if (jsonObject.has("sling_inaccuracy"))
        {
            slingInaccuracy = jsonObject.get("sling_inaccuracy").getAsFloat();
        }

        if (jsonObject.has("sling_variance"))
        {
            slingVariance = jsonObject.get("sling_variance").getAsFloat();
        }

        if (jsonObject.has("sling_webbing"))
        {
            slingWebbing = jsonObject.get("sling_webbing").getAsBoolean();
        }

        if (jsonObject.has("sling_webbing_on_web"))
        {
            slingWebbingOnWeb = jsonObject.get("sling_webbing_on_web").getAsBoolean();
        }

        if (jsonObject.has("ai_priority_sling_webs"))
        {
            AIPrioritySlingWebs = jsonObject.get("ai_priority_sling_webs").getAsInt();
        }

        if (jsonObject.has("entity_to_attack_web"))
        {
            JsonArray entityIdPriorityArray = jsonObject.getAsJsonArray("entity_to_attack_web");

            entityIdPriorityMap.clear();

            for (JsonElement element : entityIdPriorityArray)
            {
                JsonObject entityObject = element.getAsJsonObject();
                String entityId = entityObject.get("entityId").getAsString();

                int priority = entityObject.get("priority").getAsInt();
                entityIdPriorityMap.put(entityId, priority);
            }
        }
    }
}