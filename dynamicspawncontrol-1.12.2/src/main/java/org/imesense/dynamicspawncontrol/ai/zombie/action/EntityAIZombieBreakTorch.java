package org.imesense.dynamicspawncontrol.ai.zombie.action;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class EntityAIZombieBreakTorch extends EntityAIBase
{
    private BlockPos targetTorchPos;

    private final EntityZombie ZOMBIE;

    public EntityAIZombieBreakTorch(EntityZombie entityZombie)
    {
        this.ZOMBIE = entityZombie;
        this.setMutexBits(3);
    }

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

    @Override
    public void startExecuting()
    {
        if (this.targetTorchPos != null)
        {
            this.ZOMBIE.getNavigator().tryMoveToXYZ(this.targetTorchPos.getX(),
                    this.targetTorchPos.getY(), this.targetTorchPos.getZ(), 1.00);
        }
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return !this.ZOMBIE.getNavigator().noPath() && this.targetTorchPos != null;
    }

    @Override
    public void updateTask()
    {
        if (this.targetTorchPos != null &&
                this.ZOMBIE.getDistanceSqToCenter(this.targetTorchPos) < 2.00)
        {
            IBlockState iBlockState = this.ZOMBIE.world.getBlockState(this.targetTorchPos);

            if (iBlockState.getBlock() == Blocks.TORCH)
            {
                this.ZOMBIE.world.destroyBlock(this.targetTorchPos, false);
                this.targetTorchPos = null;
            }
        }
    }

    private List<BlockPos> findNearbyTorches()
    {
        BlockPos blockPos = new BlockPos(this.ZOMBIE);
        List<BlockPos> listBlockPos = new ArrayList<>();

        for (BlockPos pos : BlockPos.getAllInBox(blockPos.
                add(-10, -10, -10), blockPos.add(10, 10, 10)))
        {
            if (this.ZOMBIE.world.getBlockState(pos).getBlock() == Blocks.TORCH)
            {
                listBlockPos.add(pos);
            }
        }

        return listBlockPos;
    }
}
