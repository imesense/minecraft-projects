package org.imesense.dynamicspawncontrol.mixins.divinerpg.event;

import divinerpg.registry.EntitySpawnRegistry;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntitySpawnRegistry.class)
public abstract class EntitySpawnRegistryFix
{
    @Inject(
            method = "onLivingSpawn(Lnet/minecraftforge/event/entity/living/LivingSpawnEvent$CheckSpawn;)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void $onLivingSpawn(LivingSpawnEvent.CheckSpawn event, CallbackInfo callbackInfo)
    {
        callbackInfo.cancel();
    }
}
