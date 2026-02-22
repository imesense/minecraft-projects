package org.imesense.dynamicspawncontrol.ai.zombie.task;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.imesense.dynamicspawncontrol.ai.IZombieLightProfile;

import java.util.Random;

public final class ZombieBreakTorchEntityAI extends EntityAIBase
{
    private final EntityZombie zombie;
    private final IZombieLightProfile profile;
    private final Random rand;

    private BlockPos targetTorch;
    private int cooldown;

    public ZombieBreakTorchEntityAI(EntityZombie zombie, IZombieLightProfile profile)
    {
        this.zombie = zombie;
        this.profile = profile;
        this.rand = zombie.getRNG();
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
        if (!profile.canReactToLight())
            return false;

        if (cooldown-- > 0)
            return false;

        if (zombie.getAttackTarget() != null)
            return false;

        if (zombie.world.isDaytime())
            return false;

        if (zombie.world.getLight(zombie.getPosition()) < 9)
            return false;

        if (rand.nextFloat() > profile.getLightReactionChance())
            return false;

        this.targetTorch = findNearbyTorch();

        if (this.targetTorch != null)
        {
            cooldown = 200 + rand.nextInt(200);
            return true;
        }

        return false;
    }

    @Override
    public void startExecuting()
    {
        zombie.getNavigator().tryMoveToXYZ(
                targetTorch.getX(),
                targetTorch.getY(),
                targetTorch.getZ(),
                1.0
        );
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return targetTorch != null
                && !zombie.getNavigator().noPath()
                && zombie.getAttackTarget() == null;
    }

    @Override
    public void updateTask()
    {
        if (targetTorch == null)
            return;

        if (zombie.getDistanceSqToCenter(targetTorch) > 2.5)
            return;

        IBlockState state = zombie.world.getBlockState(targetTorch);

        if (state.getBlock() == Blocks.TORCH)
        {
            if (rand.nextFloat() < 0.6f)
            {
                zombie.world.destroyBlock(targetTorch, false);
            }
        }

        targetTorch = null;
    }

    private BlockPos findNearbyTorch()
    {
        BlockPos base = zombie.getPosition();
        int r = profile.getLightSearchRadius();

        for (int dx = -r; dx <= r; dx++)
            for (int dz = -r; dz <= r; dz++)
            {
                BlockPos pos = base.add(dx, 0, dz);

                if (zombie.world.getLight(pos) >= 12 &&
                        zombie.world.getBlockState(pos).getBlock() == Blocks.TORCH)
                {
                    return pos;
                }
            }
        return null;
    }
}
