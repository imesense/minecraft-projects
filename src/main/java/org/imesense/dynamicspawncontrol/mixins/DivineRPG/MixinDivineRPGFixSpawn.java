package org.imesense.dynamicspawncontrol.mixins.DivineRPG;

import divinerpg.registry.EntitySpawnRegistry;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * OldSerpskiStalker
 * Заметка:
 * - Данный код отключает спавн морских монстров из мода Divine RPG, которые были привязаны к механизму спавна спрутов.
 * - При попытке добавить ограничение на спавн этих существ через кеш, возникала ошибка, которая ломала механизм их спавна в Divine RPG.
 * - Это приводило к хаотичному и бесконтрольному спавну, так как сущности типа спрутов отсутствовали, что вызывало постоянные попытки их создания.
 */
@TODO(
        value = "Обновить диаграмму классов для пакета",
        showOnce = false,
        priority = TODO.TodoPriority.HIGH)
@Mixin(EntitySpawnRegistry.class)
public abstract class MixinDivineRPGFixSpawn
{
    @Inject(
            method = "onLivingSpawn(Lnet/minecraftforge/event/entity/living/LivingSpawnEvent$CheckSpawn;)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void onLivingSpawn(LivingSpawnEvent.CheckSpawn event, CallbackInfo callbackInfo)
    {
        callbackInfo.cancel();
    }
}
