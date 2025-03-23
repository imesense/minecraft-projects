package org.imesense.dynamicspawncontrol.core.script.actioncollector;

import net.minecraft.entity.EntityLivingBase;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.data.PotionEffect;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.List;
import java.util.Random;

@InitLog
public final class Potion
{
    private static volatile Potion _INSTANCE;

    public static Potion getInstance()
    {
        return CodeGeneric.getInstance(Potion.class);
    }

    public Potion()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
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
