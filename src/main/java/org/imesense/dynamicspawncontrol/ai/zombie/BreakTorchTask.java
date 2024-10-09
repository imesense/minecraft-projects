package org.imesense.dynamicspawncontrol.ai.zombie;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BreakTorchTask extends EntityAIBase
{
    private final EntityZombie zombie;
    private BlockPos targetTorchPos;

    public BreakTorchTask(EntityZombie zombie) {
        this.zombie = zombie;
        this.setMutexBits(3);  // Устанавливаем флаги выполнения задачи (например, нельзя выполнять одновременно с атакой)
    }

    @Override
    public boolean shouldExecute() {
        // Ищем ближайший факел в радиусе 10 блоков
        List<BlockPos> nearbyTorches = findNearbyTorches();
        if (!nearbyTorches.isEmpty()) {
            targetTorchPos = nearbyTorches.get(0);
            return true;
        }
        return false;
    }

    @Override
    public void startExecuting() {
        if (targetTorchPos != null) {
            // Зомби начинает движение к факелу
            this.zombie.getNavigator().tryMoveToXYZ(targetTorchPos.getX(), targetTorchPos.getY(), targetTorchPos.getZ(), 1.0);
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        // Продолжает выполнять, пока не достигнет факела
        return !zombie.getNavigator().noPath() && targetTorchPos != null;
    }

    @Override
    public void updateTask() {
        // Проверяем, что зомби рядом с факелом, и если да, ломаем его
        if (targetTorchPos != null && zombie.getDistanceSqToCenter(targetTorchPos) < 2.0) {
            IBlockState blockState = zombie.world.getBlockState(targetTorchPos);
            if (blockState.getBlock() == Blocks.TORCH) {
                zombie.world.destroyBlock(targetTorchPos, false);  // Ломаем факел
                targetTorchPos = null;  // Сбрасываем цель
            }
        }
    }

    private List<BlockPos> findNearbyTorches() {
        // Поиск факелов в радиусе 10 блоков
        BlockPos zombiePos = new BlockPos(zombie);
        List<BlockPos> torches = new ArrayList<>();
        for (BlockPos pos : BlockPos.getAllInBox(zombiePos.add(-10, -10, -10), zombiePos.add(10, 10, 10))) {
            if (zombie.world.getBlockState(pos).getBlock() == Blocks.TORCH) {
                torches.add(pos);
            }
        }
        return torches;
    }
}
