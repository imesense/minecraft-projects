package org.imesense.dynamicspawncontrol.core.script.storage;

import net.minecraft.potion.PotionEffect;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.List;

public final class StoringScriptData
{
    public static StoringScriptData Instance;

    public StoringScriptData()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
        Instance = this;
    }

    public static class Equipment
    {
        public String profile;
        public String description;

        public String entityType;
        public int Priority;

        public List<String> HeldItems;
        public List<String> Helmets;
        public List<String> ChestPlates;
        public List<String> Leggings;
        public List<String> Boots;
        public boolean HasShield;
        public boolean isArcher;
        public Boolean seeSky;
        public String commandNbt;
    }

    public List<Equipment> EquipmentConfigs;
    public List<PotionEffectWithChance> Potions;

    public List<Equipment> getEquipmentConfigs()
    {
        return this.EquipmentConfigs;
    }

    public List<PotionEffectWithChance> getPotions()
    {
        return this.Potions;
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
