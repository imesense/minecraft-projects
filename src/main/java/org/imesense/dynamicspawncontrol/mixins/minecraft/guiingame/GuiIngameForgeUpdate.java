package org.imesense.dynamicspawncontrol.mixins.minecraft.guiingame;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.imesense.dynamicspawncontrol.mixins.interfaces.IGuiIngameAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SideOnly(Side.CLIENT)
@Mixin(GuiIngameForge.class)
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
            int ticks = accessor.getUpdateCounter();
            return (ticks % 200) < 40;
        }

        return player.isPotionActive(potion);
    }
}