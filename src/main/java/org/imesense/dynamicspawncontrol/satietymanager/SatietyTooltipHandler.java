package org.imesense.dynamicspawncontrol.satietymanager;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.imesense.dynamicspawncontrol.core.memory.Configuration;

public class SatietyTooltipHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onItemTooltip(ItemTooltipEvent event)
    {
        //if (!Configuration.isSatietyTooltipsEnabled()) {
        //    return;
        //}

        ItemStack stack = event.getItemStack();
        if (stack.isEmpty())
        {
            return;
        }

        String itemId = stack.getItem().getRegistryName().toString();
        FoodSatietyData data = SatietyConfig.getSatietyData(itemId);

        if (data != null)
        {
            // Добавляем пустую строку для разделения
            //event.getToolTip().add("");

            // Добавляем заголовок
            event.getToolTip().add(TextFormatting.GOLD + "Характеристики:");

            // Добавляем насыщение с цветом в зависимости от значения
            TextFormatting satietyColor = data.isPositive() ? TextFormatting.GREEN : TextFormatting.RED;
            String satietyPrefix = data.getSatiety() > 0 ? "+" : "";
            event.getToolTip().add(TextFormatting.GRAY + "Насыщение: " +
                    satietyColor + satietyPrefix + data.getSatiety() + " %");

            // Добавляем время действия (всегда зеленый)
            event.getToolTip().add(TextFormatting.GRAY + "Продолжительность: " +
                    TextFormatting.GREEN + data.getSpanTime() + " сек.");

            // Добавляем подсказку, если зажат Shift
           //if (GuiScreen.isShiftKeyDown()) {
           //    event.getToolTip().add("");
           //    event.getToolTip().add(TextFormatting.DARK_GRAY + "Shift for more info:");
           //    event.getToolTip().add(TextFormatting.DARK_GRAY + "Satiety affects hunger/saturation");
           //    event.getToolTip().add(TextFormatting.DARK_GRAY + "Duration is effect time in seconds");
           //} else {
           //    event.getToolTip().add("");
           //    event.getToolTip().add(TextFormatting.DARK_GRAY + "Hold " +
           //            TextFormatting.YELLOW + "SHIFT" +
           //            TextFormatting.DARK_GRAY + " for more info");
           //}
        }
    }
}
