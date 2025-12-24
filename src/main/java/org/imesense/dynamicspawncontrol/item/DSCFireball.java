package org.imesense.dynamicspawncontrol.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class DSCFireball extends EntityFireball
{
    private double explosionStrength = 1.00;

    private interface FireSpawnAction
    {
        void spawnFire(World world, BlockPos blockPos, int radius);
    }

    public DSCFireball(World worldIn)
    {
        super(worldIn);
    }

    public DSCFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
    }

    public DSCFireball(World worldIn, EntityLivingBase entityLivingBase, double accelX, double accelY, double accelZ)
    {
        super(worldIn, entityLivingBase, accelX, accelY, accelZ);
    }

    public void setExplosionStrength(double strength)
    {
        this.explosionStrength = strength;
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult rayTraceResult)
    {
        if (!this.world.isRemote)
        {
            this.world.createExplosion(this.shootingEntity,
                    this.posX, this.posY, this.posZ, (float)this.explosionStrength, true);

            FireSpawnAction fireSpawnAction = (world, center, radius) ->
            {
                int rSq = radius * radius;
                BlockPos centerPos = new BlockPos(center);
                List<BlockPos> validFirePositions = new ArrayList<>();

                for (int x = -radius; x <= radius; x++)
                {
                    for (int y = -radius; y <= radius; y++)
                    {
                        for (int z = -radius; z <= radius; z++)
                        {
                            double distSq = x * x + y * y + z * z;

                            if (distSq > rSq)
                                continue;

                            double noise = UniqueField.RANDOM.nextDouble() * radius * 0.8;

                            if (distSq + noise > rSq)
                                continue;

                            BlockPos pos = centerPos.add(x, y, z);

                            if (isPositionVisibleFromCenter(world, centerPos, pos))
                            {
                                validFirePositions.add(pos);
                            }
                        }
                    }
                }

                for (BlockPos pos : validFirePositions)
                {
                    if (world.isAirBlock(pos) && UniqueField.RANDOM.nextFloat() < 0.12f)
                    {
                        world.setBlockState(pos, Blocks.FIRE.getDefaultState());
                    }
                }
            };

            fireSpawnAction.spawnFire(this.world, new BlockPos(this.posX, this.posY, this.posZ), 10);
            this.setDead();
        }
    }

    private boolean isPositionVisibleFromCenter(World world, BlockPos center, BlockPos target)
    {
        double distance = center.distanceSq(target);
        if (distance <= 4.0)
        {
            return true;
        }

        Vec3d startVec = new Vec3d(center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5);
        Vec3d endVec = new Vec3d(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5);

        Vec3d direction = endVec.subtract(startVec).normalize();
        double totalDistance = Math.sqrt(distance);

        for (double d = 0.5; d < totalDistance - 0.5; d += 0.5)
        {
            Vec3d checkPos = startVec.add(direction.scale(d));
            BlockPos blockPos = new BlockPos(checkPos.x, checkPos.y, checkPos.z);

            if (blockPos.equals(center) || blockPos.equals(target))
            {
                continue;
            }

            if (!world.isAirBlock(blockPos) &&
                    world.getBlockState(blockPos).getMaterial().blocksMovement() &&
                    world.getBlockState(blockPos).getBlock() != Blocks.FIRE)
            {
                return false;
            }
        }

        return true;
    }
}