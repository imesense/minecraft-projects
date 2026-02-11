package org.imesense.dynamicspawncontrol.satietymanager;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SatietyTooltipHandler
{
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onItemTooltip(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();

        if (stack.isEmpty())
        {
            return;
        }

        String itemId = stack.getItem().getRegistryName().toString();
        FoodSatietyData data = SatietyConfig.getSatietyData(itemId);

        if (data != null)
        {
            event.getToolTip().add(TextFormatting.GOLD + "Характеристики:");

            if (data.isPositive())
            {
                TextFormatting satietyColor = TextFormatting.GREEN;
                String satietyPrefix = "+";

                event.getToolTip().add(TextFormatting.GRAY + "Насыщение: " +
                        satietyColor + satietyPrefix + data.getSatiety() + " %");

                event.getToolTip().add(TextFormatting.GRAY + "Продолжительность: " +
                        TextFormatting.GREEN + data.getSpanTime() + " сек.");
            }
            else
            {
                event.getToolTip().add(TextFormatting.RED + "Не даёт сытости");
                event.getToolTip().add(TextFormatting.RED + "Возможны отрицательные эффекты");
                event.getToolTip().add(TextFormatting.GRAY + "Длительность: " +
                        TextFormatting.RED + data.getSpanTime() + " сек.");
            }
        }
    }
}
