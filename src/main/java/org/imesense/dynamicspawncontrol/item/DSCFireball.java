package org.imesense.dynamicspawncontrol.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;

import javax.annotation.Nonnull;

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

                            BlockPos pos = center.add(x, y, z);

                            if (world.isAirBlock(pos) && UniqueField.RANDOM.nextFloat() < 0.12f)
                            {
                                world.setBlockState(pos, Blocks.FIRE.getDefaultState());
                            }
                        }
                    }
                }
            };

            fireSpawnAction.spawnFire(this.world, new BlockPos(this.posX, this.posY, this.posZ), 10);
            this.setDead();
        }
    }
}
