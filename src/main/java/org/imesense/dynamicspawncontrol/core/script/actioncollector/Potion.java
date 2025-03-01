package org.imesense.dynamicspawncontrol.core.script.actioncollector;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.imesense.dynamicspawncontrol.core.script.storage.AbstractPotionEffect;
import org.imesense.dynamicspawncontrol.core.script.storage.StoringScriptData;

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

    public void applyPotionEffects(EntityLivingBase entity, List<AbstractPotionEffect.Data> potions, Random random)
    {
        if (potions != null && !potions.isEmpty())
        {
            for (AbstractPotionEffect.Data effectWithChance : potions)
            {
                if (random.nextDouble() <= effectWithChance.chance)
                {
                    PotionEffect effect = effectWithChance.effect;
                    PotionEffect newEffect = new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier());
                    entity.addPotionEffect(newEffect);
                }
            }
        }
    }
}
