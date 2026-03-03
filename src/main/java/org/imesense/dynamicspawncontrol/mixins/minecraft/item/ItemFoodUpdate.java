package org.imesense.dynamicspawncontrol.mixins.minecraft.item;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemFood.class)
public abstract class ItemFoodUpdate
{
    @Redirect(
            method = "onItemUseFinish",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/FoodStats;addStats(Lnet/minecraft/item/ItemFood;Lnet/minecraft/item/ItemStack;)V"
            )
    )
    private void $onItemUseFinish(FoodStats foodStats, ItemFood food, ItemStack stack)
    {
        String uniqueId = stack.getItem().getRegistryName() + ":" + System.identityHashCode(stack);
        EarlyLogBuffer.log(Logger.DEBUG, "Mixin ItemFood applied - ID: " + uniqueId);
    }
}
