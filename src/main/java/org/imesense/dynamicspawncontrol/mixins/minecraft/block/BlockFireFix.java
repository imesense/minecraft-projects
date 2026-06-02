package org.imesense.dynamicspawncontrol.mixins.minecraft.block;

import java.util.Random;

import net.minecraft.block.BlockFire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@TODO(value = "Make documentation for this fix!!!", showOnce = false, priority = TODO.TodoPriority.HIGH)
@Mixin(value = BlockFire.class, remap = false)
@SuppressWarnings("UnusedMixin")
public abstract class BlockFireFix
{
    /**
     * Перехватываем все вызовы setBlockToAir в updateTick и заменяем на setBlockState с обновлением света
     */
    @Redirect(
            method = "updateTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockToAir(Lnet/minecraft/util/math/BlockPos;)Z"
            )
    )
    private boolean onSetBlockToAir(World world, BlockPos pos)
    {
        // Устанавливаем воздух с флагом 3 (update + send to client) и принудительно обновляем свет
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        world.checkLight(pos);

        return true; // setBlockToAir возвращает boolean, но нам не важно
    }

    /**
     * Также перехватываем вызовы setBlockState в tryCatchFire, когда блок сгорает
     */
    @Redirect(
            method = "tryCatchFire",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockToAir(Lnet/minecraft/util/math/BlockPos;)V"
            )
    )
    private void onTryCatchFireSetBlockToAir(World world, BlockPos pos)
    {
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        world.checkLight(pos);
    }

    /**
     * Перехватываем момент, когда огонь достигает максимального возраста и тухнет
     * (специальный случай в строках 121-123)
     */
    @Inject(
            method = "updateTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockToAir(Lnet/minecraft/util/math/BlockPos;)V",
                    ordinal = 2,  // Третий вызов setBlockToAir в методе (строки 121-123)
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onMaxAgeFireDie(World worldIn, BlockPos pos, IBlockState state, Random rand, CallbackInfo ci)
    {
        // Дополнительная проверка света после того, как огонь потух
        worldIn.checkLight(pos);

        // Также проверяем соседние блоки, которые могли потерять источник света
        for (EnumFacing facing : EnumFacing.values())
        {
            worldIn.checkLight(pos.offset(facing));
        }
    }

    /**
     * Перехватываем момент распространения огня на новые блоки
     * и обновляем свет на старом месте, если огонь там потух
     */
    @Inject(
            method = "updateTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Z",
                    ordinal = 1  // Вызов setBlockState для нового огня (строки 171-179)
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onFireSpread(World worldIn, BlockPos pos, IBlockState state, Random rand, CallbackInfo ci)
    {
        // Проверяем свет вокруг, так как старый огонь мог потухнуть
        worldIn.checkLight(pos);
    }
}
