package org.imesense.dynamicspawncontrol.mixins.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.Loader;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
@SuppressWarnings("UnusedMixin")
public abstract class EnchantmentUpdate
{
    @Inject(method = {"isCompatibleWith"}, at = {@At("HEAD")}, cancellable = true)
    private void $isCompatibleWith(Enchantment enchantment, CallbackInfoReturnable<Boolean> booleanCallbackInfoReturnable)
    {
        Enchantment first = (Enchantment)(Object)this;

        if (!Loader.isModLoaded("togenc") && $$check(first, enchantment, Enchantments.FORTUNE, Enchantments.SILK_TOUCH))
        {
            booleanCallbackInfoReturnable.setReturnValue(false);
        }
        else
        {
            booleanCallbackInfoReturnable.setReturnValue(true);
        }
    }

    @Unique
    private static boolean $$check(Enchantment enchantmentFirst, Enchantment enchantmentSecond, Enchantment enchantmentFirst1, Enchantment enchantmentSecond2)
    {
        return enchantmentFirst.equals(enchantmentFirst1) && enchantmentSecond.equals(enchantmentSecond2) ||
                enchantmentFirst.equals(enchantmentSecond2) && enchantmentSecond.equals(enchantmentFirst1);
    }

    @Unique
    private static boolean $$check(Enchantment enchantmentFirst, Enchantment enchantmentSecond, String enchantmentFirst1, String enchantmentSecond2)
    {
        return enchantmentFirst.getName().equals(enchantmentFirst1) &&
                enchantmentSecond.getName().equals(enchantmentSecond2) ||
                enchantmentSecond.getName().equals(enchantmentFirst1) && enchantmentFirst.getName().equals(enchantmentSecond2);
    }

    @Inject(method = {"canApply"}, at = {@At("HEAD")}, cancellable = true)
    private void canEnchant(ItemStack itemStack, CallbackInfoReturnable<Boolean> booleanCallbackInfoReturnable)
    {
        booleanCallbackInfoReturnable.setReturnValue(true);
    }
}
