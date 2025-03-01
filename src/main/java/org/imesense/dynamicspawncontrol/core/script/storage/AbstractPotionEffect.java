package org.imesense.dynamicspawncontrol.core.script.storage;

import net.minecraft.potion.PotionEffect;

public class AbstractPotionEffect
{
    public static class Data
    {
        public double Chance;
        public PotionEffect Effect;

        public Data(PotionEffect effect, double chance)
        {
            this.Effect = effect;
            this.Chance = chance;
        }
    }
}
