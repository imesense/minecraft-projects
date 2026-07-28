package org.imesense.dynamicspawncontrol.mixins.minecraft.guiingame;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import org.imesense.dynamicspawncontrol.mixins.minecraft.IGuiIngameAccessor;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(Side.CLIENT)
@Mixin(GuiIngameForge.class)
@SuppressWarnings("UnusedMixin")
public abstract class GuiIngameForgeUpdate
{
    @Redirect(
            method = "renderHealth",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/EntityPlayer;isPotionActive(Lnet/minecraft/potion/Potion;)Z"
            )
    )
    private boolean $renderHealth(EntityPlayer player, Potion potion)
    {
        if (potion == MobEffects.REGENERATION && !player.isPotionActive(MobEffects.REGENERATION))
        {
            IGuiIngameAccessor accessor = (IGuiIngameAccessor) (Object) this;
            int ticks = accessor.accessorGetUpdateCounter();
            return (ticks % 200) < 40;
        }

        return player.isPotionActive(potion);
    }

    @Inject(method = "renderPotionIcons", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private void onRenderPotionIcons(ScaledResolution resolution, CallbackInfo ci)
    {
        if (Minecraft.getMinecraft().gameSettings.showDebugInfo)
        {
            ci.cancel();
        }
    }
}
