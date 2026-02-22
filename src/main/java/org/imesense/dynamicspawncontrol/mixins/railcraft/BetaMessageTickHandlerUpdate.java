package org.imesense.dynamicspawncontrol.mixins.railcraft;

import mods.railcraft.common.core.BetaMessageTickHandler;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = BetaMessageTickHandler.class, remap = false)
public abstract class BetaMessageTickHandlerUpdate
{
    @Overwrite
    public void tick(LivingEvent.LivingUpdateEvent event)
    {

    }
}
