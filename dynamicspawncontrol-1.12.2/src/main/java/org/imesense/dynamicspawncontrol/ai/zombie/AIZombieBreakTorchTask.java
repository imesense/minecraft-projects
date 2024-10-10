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
 * OldSerpskiStalker
 * TODO: Все моменты отвечающие за логику живых сущностей прописывать комментариями
 */
public class AIZombieBreakTorchTask extends EntityAIBase
{
    /**
     *
     */
    private BlockPos targetTorchPos;

    /**
     *
     */
    private final EntityZombie zombie;

    /**
     *
     * @param zombie
     */
    public AIZombieBreakTorchTask(EntityZombie zombie)
    {
        this.zombie = zombie;
        this.setMutexBits(3);  //-' Устанавливаем флаги выполнения задачи (например, нельзя выполнять одновременно с атакой)
    }

    /**
     *
     * @return
     */
    @Override
    public boolean shouldExecute()
    {
        //-' Ищем ближайший факел в радиусе 10 блоков
        List<BlockPos> nearbyTorches = findNearbyTorches();

        if (!nearbyTorches.isEmpty())
        {
            this.targetTorchPos = nearbyTorches.get(0);
            return true;
        }

        return false;
    }

    /**
     *
     */
    @Override
    public void startExecuting()
    {
        if (this.targetTorchPos != null)
        {
            //-' Зомби начинает движение к факелу
            this.zombie.getNavigator().tryMoveToXYZ(this.targetTorchPos.getX(), this.targetTorchPos.getY(), this.targetTorchPos.getZ(), 1.0);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        //-' Продолжает выполнять, пока не достигнет факела
        return !zombie.getNavigator().noPath() && this.targetTorchPos != null;
    }

    /**
     *
     */
    @Override
    public void updateTask()
    {
        //-' Проверяем, что зомби рядом с факелом, и если да, ломаем его
        if (this.targetTorchPos != null && zombie.getDistanceSqToCenter(this.targetTorchPos) < 2.0)
        {
            IBlockState blockState = zombie.world.getBlockState(this.targetTorchPos);

            if (blockState.getBlock() == Blocks.TORCH)
            {
                zombie.world.destroyBlock(this.targetTorchPos, false);  //-' Ломаем факел, не выпадает
                this.targetTorchPos = null; //-' Сбрасываем цель
            }
        }
    }

    /**
     *
     * @return
     */
    private List<BlockPos> findNearbyTorches()
    {
        //-' Поиск факелов в радиусе 10 блоков
        BlockPos zombiePos = new BlockPos(zombie);
        List<BlockPos> torches = new ArrayList<>();

        for (BlockPos pos : BlockPos.getAllInBox(zombiePos.add(-10, -10, -10), zombiePos.add(10, 10, 10)))
        {
            if (zombie.world.getBlockState(pos).getBlock() == Blocks.TORCH)
            {
                torches.add(pos);
            }
        }

        return torches;
    }
}
