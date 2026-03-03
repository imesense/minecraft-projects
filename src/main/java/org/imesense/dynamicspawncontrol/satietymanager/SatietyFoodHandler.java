package org.imesense.dynamicspawncontrol.satietymanager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.core.logfile.Logger;
import org.imesense.dynamicspawncontrol.potion.ModPotions;
import net.minecraft.init.MobEffects;
import java.util.Random;

public class SatietyFoodHandler
{
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public void onFoodEaten(LivingEntityUseItemEvent.Finish event)
    {
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();

        if (player.world.isRemote)
            return;

        if (!(event.getEntityLiving() instanceof EntityPlayer))
            return;

        ItemStack stack = event.getItem();

        if (!(stack.getItem() instanceof ItemFood))
            return;

        ResourceLocation rl = stack.getItem().getRegistryName();

        if (rl == null)
            return;

        String itemId = rl.toString();

        FoodSatietyData data = SatietyConfig.getSatietyData(itemId);
        if (data == null)
            return;

        if (!data.isPositive())
        {
            int duration = data.getSpanTime() * 20;

            player.addPotionEffect(new PotionEffect(
                    MobEffects.HUNGER,
                    duration,
                    0
            ));

            if (RANDOM.nextFloat() < 0.5F)
            {
                player.addPotionEffect(new PotionEffect(
                        MobEffects.NAUSEA,
                        duration / 2,
                        0
                ));
            }

            if (RANDOM.nextFloat() < 0.3F)
            {
                player.addPotionEffect(new PotionEffect(
                        MobEffects.BLINDNESS,
                        duration / 3,
                        0
                ));
            }

            if (RANDOM.nextFloat() < 0.4F)
            {
                player.addPotionEffect(new PotionEffect(
                        MobEffects.WEAKNESS,
                        duration,
                        0
                ));
            }

            Logger.info("Bad food consumed: " + itemId);

            return;
        }

        PotionEffect currentEffect = player.getActivePotionEffect(ModPotions.SATIETY);

        int addedDuration = data.getSpanTime() * 20;
        int maxDuration = 60 * 20;
        int newDuration = addedDuration;
        int amplifier = 0;

        if (currentEffect != null)
        {
            int remaining = currentEffect.getDuration();

            newDuration = Math.min(remaining + addedDuration, maxDuration);

            player.removePotionEffect(ModPotions.SATIETY);

            Logger.info("Stacking satiety: old=" + remaining +
                    " added=" + addedDuration +
                    " final=" + newDuration);
        }

        PotionEffect newEffect = new PotionEffect(
                ModPotions.SATIETY,
                newDuration,
                amplifier,
                false,
                true
        );

        player.addPotionEffect(newEffect);

        Logger.info("Satiety applied: " + itemId +
                " | duration=" + newDuration +
                " | amp=" + amplifier);
    }
}
