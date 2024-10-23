package org.imesense.dynamicspawncontrol.ai.zombie.entityaibase;

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
    /**
     *
     */
    private BlockPos targetTorchPos;

    /**
     *
     */
    private final EntityZombie ZOMBIE;

    /**
     *
     * @param zombie
     */
    public BreakTorchTask(EntityZombie zombie)
    {
        this.ZOMBIE = zombie;
        this.setMutexBits(3);
    }

    /**
     *
     * @return
     */
    @Override
    public boolean shouldExecute()
    {
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
            this.ZOMBIE.getNavigator().tryMoveToXYZ(this.targetTorchPos.getX(),
                    this.targetTorchPos.getY(), this.targetTorchPos.getZ(), 1.0);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        return !this.ZOMBIE.getNavigator().noPath() && this.targetTorchPos != null;
    }

    /**
     *
     */
    @Override
    public void updateTask()
    {
        if (this.targetTorchPos != null &&
                this.ZOMBIE.getDistanceSqToCenter(this.targetTorchPos) < 2.0)
        {
            IBlockState blockState = this.ZOMBIE.world.getBlockState(this.targetTorchPos);

            if (blockState.getBlock() == Blocks.TORCH)
            {
                this.ZOMBIE.world.destroyBlock(this.targetTorchPos, false);
                this.targetTorchPos = null;
            }
        }
    }

    /**
     *
     * @return
     */
    private List<BlockPos> findNearbyTorches()
    {
        BlockPos zombiePos = new BlockPos(this.ZOMBIE);
        List<BlockPos> torchesListPos = new ArrayList<>();

        for (BlockPos pos : BlockPos.getAllInBox(zombiePos.
                add(-10, -10, -10), zombiePos.add(10, 10, 10)))
        {
            if (this.ZOMBIE.world.getBlockState(pos).getBlock() == Blocks.TORCH)
            {
                torchesListPos.add(pos);
            }
        }

        return torchesListPos;
    }
}
