package org.imesense.dynamicspawncontrol.core.script.storage;

import net.minecraft.potion.PotionEffect;

public class AbstractPotionEffect
{
    public static class Data
    {
        public Double chance;
        public PotionEffect effect;

        public Data(PotionEffect effect, Double chance)
        {
            this.effect = effect;
            this.chance = chance;
        }
    }
}
