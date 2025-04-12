package org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.data;

public final class PotionEffect
{
    public PotionEffect()
    {

    }

    public static final class Data
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
