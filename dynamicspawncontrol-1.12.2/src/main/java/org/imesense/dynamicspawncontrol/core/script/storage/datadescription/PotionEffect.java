package org.imesense.dynamicspawncontrol.core.script.storage.datadescription;

public class PotionEffect
{
    public static class Data
    {
        public Double chance;
        public net.minecraft.potion.PotionEffect effect;

        public Data(net.minecraft.potion.PotionEffect effect, Double chance)
        {
            this.effect = effect;
            this.chance = chance;
        }
    }
}
