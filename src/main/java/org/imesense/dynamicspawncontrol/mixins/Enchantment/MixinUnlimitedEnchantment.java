package org.imesense.dynamicspawncontrol.mixins.Enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@TODO(
        value = "Обновить диаграмму классов для пакета",
        showOnce = false,
        priority = TODO.TodoPriority.HIGH)
@Mixin(Enchantment.class)
public abstract class MixinUnlimitedEnchantment
{
    @Inject(method = {"isCompatibleWith"}, at = {@At("HEAD")}, cancellable = true)
    private void isCompatibleWith(Enchantment enchantment, CallbackInfoReturnable<Boolean> booleanCallbackInfoReturnable)
    {
        Enchantment first = (Enchantment)(Object)this;

        if (!Loader.isModLoaded("togenc") && check(first, enchantment, Enchantments.FORTUNE, Enchantments.SILK_TOUCH))
        {
            booleanCallbackInfoReturnable.setReturnValue(false);
        }
        else
        {
            booleanCallbackInfoReturnable.setReturnValue(true);
        }
    }

    @Unique
    private static boolean check(Enchantment enchantmentFirst, Enchantment enchantmentSecond, Enchantment enchantmentFirst1, Enchantment enchantmentSecond2)
    {
        return enchantmentFirst.equals(enchantmentFirst1) && enchantmentSecond.equals(enchantmentSecond2) ||
                enchantmentFirst.equals(enchantmentSecond2) && enchantmentSecond.equals(enchantmentFirst1);
    }

    @Unique
    private static boolean check(Enchantment enchantmentFirst, Enchantment enchantmentSecond, String enchantmentFirst1, String enchantmentSecond2)
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
