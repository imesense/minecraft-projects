package org.imesense.dynamicspawncontrol.mixins;

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
public abstract class MixinUnlimitedEnchantment
{
    @Inject(method = {"isCompatibleWith"}, at = {@At("HEAD")}, cancellable = true)
    private void isCompatibleWith(Enchantment second, CallbackInfoReturnable<Boolean> info)
    {
        Enchantment first = (Enchantment) (Object) this;

        if (!Loader.isModLoaded("togenc") && check(first, second, Enchantments.FORTUNE, Enchantments.SILK_TOUCH))
        {
            info.setReturnValue(false);
        }
        else
        {
            info.setReturnValue(true);
        }
    }

    @Unique
    private static boolean check(Enchantment first, Enchantment second, Enchantment firstEnchant, Enchantment secondEnchant)
    {
        return first.equals(firstEnchant) && second.equals(secondEnchant) ||
                first.equals(secondEnchant) && second.equals(firstEnchant);
    }

    @Unique
    private static boolean check(Enchantment first, Enchantment second, String firstEnchant, String secondEnchant)
    {
        return first.getName().equals(firstEnchant) &&
                second.getName().equals(secondEnchant) ||
                second.getName().equals(firstEnchant) && first.getName().equals(secondEnchant);
    }

    @Inject(method = {"canApply"}, at = {@At("HEAD")}, cancellable = true)
    private void canEnchant(ItemStack stack, CallbackInfoReturnable<Boolean> info)
    {
        info.setReturnValue(true);
    }
}
