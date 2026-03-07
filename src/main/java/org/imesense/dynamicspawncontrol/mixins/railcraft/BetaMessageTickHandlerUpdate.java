package org.imesense.dynamicspawncontrol.mixins.railcraft;

import net.minecraftforge.event.entity.living.LivingEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import mods.railcraft.common.core.BetaMessageTickHandler;

@Mixin(value = BetaMessageTickHandler.class, remap = false)
@SuppressWarnings("UnusedMixin")
public abstract class BetaMessageTickHandlerUpdate
{
    /**
     * @author OldSerpskiStalker
     * @reason Disabling version usage message
     */
    @Overwrite
    @SuppressWarnings("OverwriteModifiers")
    public void tick(LivingEvent.LivingUpdateEvent event)
    {
    }
}
