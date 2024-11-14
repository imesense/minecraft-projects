package org.imesense.dynamicspawncontrol.core.auxsolid;

import net.minecraft.potion.PotionEffect;

/**
 *
 */
public final class Potion
{
    /**
     *
     */
    public static class PotionEffectWithChance
    {
        /**
         *
         */
        public double Chance;

        /**
         *
         */
        public PotionEffect Effect;

        /**
         *
         * @param effect
         * @param chance
         */
        public PotionEffectWithChance(PotionEffect effect, double chance)
        {
            this.Effect = effect;
            this.Chance = chance;
        }
    }
}
