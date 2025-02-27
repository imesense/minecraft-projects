package org.imesense.dynamicspawncontrol.parser.algo;

import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.List;

public final class GeneralStorageData
{
    public static GeneralStorageData Instance;

    public GeneralStorageData()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
        Instance = this;
    }

    public static class Equipment
    {
        public String entityType;
        public int Priority;

        public List<String> HeldItems;
        public List<String> Helmets;
        public List<String> ChestPlates;
        public List<String> Leggings;
        public List<String> Boots;
        public boolean HasShield;
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
