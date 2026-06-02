package org.imesense.dynamicspawncontrol.item;

import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
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
            this.world.createExplosion(null,
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
                            double distSq = x*x + y*y + z*z;

                            if (distSq > rSq) continue;

                            if (Math.abs(Math.sqrt(distSq) - radius) > 1.5)
                            {
                                if (UniqueField.RANDOM.nextFloat() > 0.3f) continue;
                            }

                            BlockPos pos = centerPos.add(x, y, z);

                            if (world.isAirBlock(pos) && isPositionVisibleFromCenter(world, centerPos, pos))
                            {
                                validFirePositions.add(pos);
                            }
                        }
                    }
                }

                for (BlockPos pos : validFirePositions)
                {
                    if (UniqueField.RANDOM.nextFloat() < 0.4f)
                    {
                        world.setBlockState(pos, Blocks.FIRE.getDefaultState());
                    }
                }
            };

            fireSpawnAction.spawnFire(this.world, new BlockPos(this.posX, this.posY, this.posZ), 10);

            spawnSurfaceSmoke(new BlockPos(this.posX, this.posY, this.posZ), 10);

            this.setDead();
        }
    }

    private void spawnSurfaceSmoke(BlockPos center, int radius)
    {
        int rSq = radius * radius;
        List<BlockPos> smokePositions = new ArrayList<>();

        for (int x = -radius; x <= radius; x++)
        {
            for (int z = -radius; z <= radius; z++)
            {
                int distSq = x*x + z*z;
                if (distSq > rSq) continue;

                double dist = Math.sqrt(distSq);
                double edgeFactor = dist / radius;

                if (UniqueField.RANDOM.nextFloat() > (0.15f + edgeFactor * 0.5f))
                    continue;

                for (int y = 4; y >= -4; y--)
                {
                    BlockPos pos = center.add(x, y, z);

                    if (!world.isAirBlock(pos)) continue;

                    BlockPos ground = pos.down();

                    if (!world.isAirBlock(ground) &&
                            world.getBlockState(ground).getMaterial().blocksMovement() &&
                            world.getBlockState(ground).getBlock() != Blocks.FIRE &&
                            world.getBlockState(ground).getBlock() != Blocks.LAVA &&
                            world.getBlockState(ground).getBlock() != Blocks.FLOWING_LAVA)
                    {
                        smokePositions.add(pos);
                        break;
                    }
                }
            }
        }

        for (BlockPos pos : smokePositions)
        {
            if (UniqueField.RANDOM.nextFloat() < 0.65f)
            {
                spawnSmallSmokeCloud(pos);
            }
        }
    }

    private void spawnSmallSmokeCloud(BlockPos pos)
    {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.1;
        double z = pos.getZ() + 0.5;

        EntityAreaEffectCloud cloud = new EntityAreaEffectCloud(world, x, y, z);

        cloud.setRadius(1.0f + UniqueField.RANDOM.nextFloat() * 1.2f);
        cloud.setRadiusPerTick(-0.0003f - UniqueField.RANDOM.nextFloat() * 0.0005f);

        cloud.setDuration(1800 + UniqueField.RANDOM.nextInt(1200));
        cloud.setWaitTime(0);

        cloud.setParticle(EnumParticleTypes.SMOKE_LARGE);
        cloud.setColor(0x3A3A3A);

        cloud.motionX = 0;
        cloud.motionZ = 0;
        cloud.motionY = 0.015 + UniqueField.RANDOM.nextDouble() * 0.02;

        cloud.setPosition(x + (UniqueField.RANDOM.nextDouble() - 0.5) * 0.3,
                y, z + (UniqueField.RANDOM.nextDouble() - 0.5) * 0.3);

        world.spawnEntity(cloud);
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
