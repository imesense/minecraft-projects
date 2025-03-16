package org.imesense.dynamicspawncontrol.mixins;

import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import divinerpg.registry.ModSpawns;

/**
 * OldSerpskiStalker
 * Заметка:
 * - Данный код отключает спавн морских монстров из мода Divine RPG, которые были привязаны к механизму спавна спрутов.
 * - При попытке добавить ограничение на спавн этих существ через кеш, возникала ошибка, которая ломала механизм их спавна в Divine RPG.
 * - Это приводило к хаотичному и бесконтрольному спавну, так как сущности типа спрутов отсутствовали, что вызывало постоянные попытки их создания.
 */
@Mixin(ModSpawns.class)
public abstract class MixinDivineRPGFixSpawn
{
    /**
     *
     * @param livingSpawnEvent
     * @param callbackInfo
     */
    @Inject(method = "init", at = @At("HEAD"), cancellable = true, remap = false)
    private static void mixinInjectInit(LivingSpawnEvent livingSpawnEvent, CallbackInfo callbackInfo)
    {
        callbackInfo.cancel();
    }

    /**
     *
     * @param livingSpawnEvent
     * @param callbackInfo
     */
    @Inject(method = "onLivingSpawn", at = @At("HEAD"), cancellable = true, remap = false)
    private static void mixinInjectOnLivingSpawn(LivingSpawnEvent livingSpawnEvent, CallbackInfo callbackInfo)
    {
        callbackInfo.cancel();
    }
}
