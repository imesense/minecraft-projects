package org.imesense.dynamicspawncontrol.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.logfile.Logger;

import java.util.Collections;

public class SatietyPotion extends PotionBase
{
    public SatietyPotion()
    {
        super(false, 0, "satiety");

        setIconIndex(0, 0);
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        int interval = 60;
        return duration % interval == 0;
    }

    @Override
    public void performEffect(EntityLivingBase entityLiving, int amplifier)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entityLiving;
            World world = player.world;

            if (!world.isRemote)
            {
                if (player.getFoodStats().needFood())
                {
                    player.getFoodStats().addStats(1, amplifier * 0.5F);
                }

                if (player.getFoodStats().getFoodLevel() >= 20 && player.getHealth() < player.getMaxHealth())
                {
                    player.heal(1.0F);
                }
            }

            Logger.debug(
                    "Satiety tick | player=" + player.getName() +
                            " food=" + player.getFoodStats().getFoodLevel() +
                            " saturation=" + player.getFoodStats().getSaturationLevel() +
                            " amp=" + amplifier
            );
        }
    }

    @Override
    public boolean isInstant()
    {
        return false;
    }

    @Override
    public java.util.List<ItemStack> getCurativeItems()
    {
        return Collections.emptyList();
    }
}