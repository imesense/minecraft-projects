package org.imesense.dynamicspawncontrol.core.script.storage;

import com.google.gson.JsonObject;
import net.minecraft.potion.PotionEffect;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public final class StoringScriptData
{
    public static StoringScriptData Instance;

    public StoringScriptData()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
        Instance = this;this.DataSupports = new ArrayList<>();
    }

    //-' TODO: переделать это
    public static class Equipment
    {
        public String profile;
        public String description;

        public String entityType;
        public int Priority;

        public List<ItemData> HeldItems;
        public List<ItemData> Helmets;
        public List<ItemData> ChestPlates;
        public List<ItemData> Leggings;
        public List<ItemData> Boots;
        public boolean HasShield;
        public boolean isArcher;
        public Boolean seeSky;
        public String commandNbt;
        public Integer maxHeight;
        public Integer minHeight;
        public List<PotionEffectWithChance> potions;
        public String name;
    }

    public static class DataSupport
    {
        public Boolean seeSky;
        public String entityType;
        public List<PotionEffectWithChance> potions;
    }

    public List<DataSupport> DataSupports;

    public static class ItemData
    {
        public String item;
        public JsonObject nbt;
    }

    public List<Equipment> EquipmentConfigs;
    public List<PotionEffectWithChance> Potions;

    public List<Equipment> getEquipmentConfigs()
    {
        return this.EquipmentConfigs;
    }

    public static class PotionEffectWithChance
    {
        public double Chance;
        public PotionEffect Effect;

        public PotionEffectWithChance(PotionEffect effect, double chance)
        {
            this.Effect = effect;
            this.Chance = chance;
        }
    }
}
