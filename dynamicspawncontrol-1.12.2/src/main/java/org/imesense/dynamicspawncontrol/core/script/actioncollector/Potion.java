package org.imesense.dynamicspawncontrol.core.script.actioncollector;

import net.minecraft.entity.EntityLivingBase;
import org.imesense.dynamicspawncontrol.core.script.storage.datadescription.PotionEffect;

import java.util.List;
import java.util.Random;

public final class Potion
{
    private static volatile Potion _INSTANCE;

    public static Potion getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (Potion.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new Potion();
                }
            }
        }

        return _INSTANCE;
    }

    public void applyPotionEffects(EntityLivingBase entityLivingBase, List<PotionEffect.Data> listPotionEffectData, Random random)
    {
        if (listPotionEffectData != null && !listPotionEffectData.isEmpty())
        {
            for (PotionEffect.Data effectWithChance : listPotionEffectData)
            {
                if (random.nextDouble() <= effectWithChance.chance)
                {
                    net.minecraft.potion.PotionEffect effect = effectWithChance.effect;
                    net.minecraft.potion.PotionEffect newEffect = new net.minecraft.potion.PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier());
                    entityLivingBase.addPotionEffect(newEffect);
                }
            }
        }
    }
}
