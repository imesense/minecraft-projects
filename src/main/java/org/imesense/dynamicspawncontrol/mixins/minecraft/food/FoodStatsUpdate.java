package org.imesense.dynamicspawncontrol.mixins.minecraft.food;

import net.minecraft.util.FoodStats;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodStats.class)
@SuppressWarnings("UnusedMixin")
public abstract class FoodStatsUpdate
{
    @Shadow
    private int foodLevel;
    @Shadow
    private int prevFoodLevel;

    @Unique
    private int $$clientTickCounter = 0;

    @Inject(
            method = "onUpdate",
            at = @At("TAIL")
    )
    private void $onUpdate(
            net.minecraft.entity.player.EntityPlayer player,
            CallbackInfo ci
    )
    {
        if (!player.world.isRemote)
            return;

        $$clientTickCounter++;

        if ($$clientTickCounter < 15 * 20)
            return;

        $$clientTickCounter = 0;

        this.prevFoodLevel = this.foodLevel - 1;
    }

    @Inject(method = "setFoodSaturationLevel", at = @At("HEAD"), cancellable = true, remap = false)
    public void $setFoodSaturationLevel(float foodSaturationLevelIn, CallbackInfo ci)
    {
    }
}
