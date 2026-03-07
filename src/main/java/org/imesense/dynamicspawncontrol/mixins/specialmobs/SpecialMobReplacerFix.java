package org.imesense.dynamicspawncontrol.mixins.specialmobs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fathertoast.specialmobs.SpecialMobReplacer;
import fathertoast.specialmobs.bestiary.EnumMobFamily;

import org.imesense.dynamicspawncontrol.core.mixinconfig.specialmobs.SpecialMobsReplacerData;

@Mixin(value = SpecialMobReplacer.class, remap = false)
@SuppressWarnings("UnusedMixin")
public abstract class SpecialMobReplacerFix
{
    @Inject(method = "shouldReplace", at = @At("RETURN"), cancellable = true, remap = false)
    private void $shouldReplace(EnumMobFamily mobFamily, boolean isSpecial, CallbackInfoReturnable<Boolean> cir)
    {
        if (isSpecial)
        {
            return;
        }

        if (!SpecialMobsReplacerData.shouldReplaceVanilla())
        {
            cir.setReturnValue(false);
        }
    }
}
