package org.imesense.dynamicspawncontrol.core.config.DropItem;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseJsonConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@Getter
@Setter
@ConceptConfig(fileName = "cfg_zombie_drop_item")
public final class ZombieDropConfig extends BaseJsonConfig
{
    private float breakItem = 0.15f;
    private float handItemDamageFactor = 0.85f;
    private float headDamageFactor = 0.9f;
    private float chestDamageFactor = 0.9f;
    private float legsDamageFactor = 0.9f;
    private float feetDamageFactor = 0.9f;
    private float damageSpreadFactor = 0.2f;

    public ZombieDropConfig(String configPath)
    {
        super(configPath, true);

        CodeGeneric.printInitClassToLog(this.getClass());

        loadOrCreateConfig();
    }

    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject config = new JsonObject();

        config.addProperty("break_item", breakItem);
        config.addProperty("hand_item_damage_factor", handItemDamageFactor);
        config.addProperty("head_damage_factor", headDamageFactor);
        config.addProperty("chest_damage_factor", chestDamageFactor);
        config.addProperty("legs_damage_factor", legsDamageFactor);
        config.addProperty("feet_damage_factor", feetDamageFactor);
        config.addProperty("damage_spread_factor", damageSpreadFactor);

        return config;
    }

    @Override
    protected void applyConfig(JsonObject jsonObject)
    {
        if (jsonObject.has("break_item"))
        {
            breakItem = jsonObject.get("break_item").getAsFloat();
        }

        if (jsonObject.has("hand_item_damage_factor"))
        {
            handItemDamageFactor = jsonObject.get("hand_item_damage_factor").getAsFloat();
        }

        if (jsonObject.has("head_damage_factor"))
        {
            headDamageFactor = jsonObject.get("head_damage_factor").getAsFloat();
        }

        if (jsonObject.has("chest_damage_factor"))
        {
            chestDamageFactor = jsonObject.get("chest_damage_factor").getAsFloat();
        }

        if (jsonObject.has("legs_damage_factor"))
        {
            legsDamageFactor = jsonObject.get("legs_damage_factor").getAsFloat();
        }

        if (jsonObject.has("feet_damage_factor"))
        {
            feetDamageFactor = jsonObject.get("feet_damage_factor").getAsFloat();
        }

        if (jsonObject.has("damage_spread_factor"))
        {
            damageSpreadFactor = jsonObject.get("damage_spread_factor").getAsFloat();
        }
    }
}
