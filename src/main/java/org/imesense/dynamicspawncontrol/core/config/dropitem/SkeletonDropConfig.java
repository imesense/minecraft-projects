package org.imesense.dynamicspawncontrol.core.config.dropitem;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseJsonConfig;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@Getter
@Setter
@InitLog
@ConceptConfig(fileName = "cfg_skeleton_drop_item")
@TODO(value = "Rework this config in 0.2 ver", showOnce = false, priority = TODO.TodoPriority.HIGH)
public final class SkeletonDropConfig extends BaseJsonConfig
{
    private float breakItem = 0.15f;
    private float handItemDamageFactor = 0.85f;
    private float headDamageFactor = 0.9f;
    private float chestDamageFactor = 0.9f;
    private float legsDamageFactor = 0.9f;
    private float feetDamageFactor = 0.9f;
    private float damageSpreadFactor = 0.2f;

    private byte arrowsToDrops = (byte)(1 + UniqueField.RANDOM.nextInt(3));

    public SkeletonDropConfig(String configPath)
    {
        super(configPath, true);

        loadOrCreateConfig();
    }

    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("break_item", breakItem);
        jsonObject.addProperty("hand_item_damage_factor", handItemDamageFactor);
        jsonObject.addProperty("head_damage_factor", headDamageFactor);
        jsonObject.addProperty("chest_damage_factor", chestDamageFactor);
        jsonObject.addProperty("legs_damage_factor", legsDamageFactor);
        jsonObject.addProperty("feet_damage_factor", feetDamageFactor);
        jsonObject.addProperty("damage_spread_factor", damageSpreadFactor);
        jsonObject.addProperty("arrows_to_drops", arrowsToDrops);

        return jsonObject;
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

        if (jsonObject.has("arrows_to_drops"))
        {
            arrowsToDrops = jsonObject.get("arrows_to_drops").getAsByte();
        }
    }
}
