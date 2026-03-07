package org.imesense.dynamicspawncontrol.mixins.interfaces;

import net.minecraft.client.gui.GuiIngame;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiIngame.class)
@SuppressWarnings("UnusedMixin")
public interface IGuiIngameAccessor
{
    @Accessor("updateCounter")
    int accessorGetUpdateCounter();
}
