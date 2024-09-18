package org.imesense.dynamicspawncontrol.gameplay.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 */
public final class CustomFireball extends EntityFireball
{
    /**
     *
     */
    private double explosionStrength = 1.0;

    /**
     *
     */
    private interface FireSpawnAction
    {
        /**
         *
         * @param world
         * @param explosionPos
         * @param radius
         */
        void spawnFire(World world, BlockPos explosionPos, int radius);
    }

    /**
     *
     * @param worldIn
     */
    public CustomFireball(World worldIn)
    {
        super(worldIn);
    }

    /**
     *
     * @param worldIn
     * @param x
     * @param y
     * @param z
     * @param accelX
     * @param accelY
     * @param accelZ
     */
    public CustomFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
    }

    /**
     *
     * @param worldIn
     * @param shooter
     * @param accelX
     * @param accelY
     * @param accelZ
     */
    public CustomFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn, shooter, accelX, accelY, accelZ);
    }

    /**
     *
     * @param strength
     */
    public void setExplosionStrength(double strength)
    {
        this.explosionStrength = strength;
    }

    /**
     *
     * @param result
     */
    @Override
    protected void onImpact(@Nonnull RayTraceResult result)
    {
        if (!this.world.isRemote)
        {
            this.world.createExplosion(this.shootingEntity, this.posX, this.posY, this.posZ, (float)this.explosionStrength, true);

            FireSpawnAction fireSpawnAction = (world, explosionPos, radius) ->
            {
                for (int x = -radius; x <= radius + new Random().nextInt(5); x++)
                {
                    for (int y = -radius; y <= radius + new Random().nextInt(5); y++)
                    {
                        for (int z = -radius; z <= radius + new Random().nextInt(5); z++)
                        {
                            BlockPos blockPos = explosionPos.add(x, y, z);
                            double distanceSq = explosionPos.distanceSq(blockPos);

                            if (distanceSq <= radius * radius && world.getBlockState(blockPos).getBlock() == Blocks.AIR)
                            {
                                if (new Random().nextFloat() < 0.1f)
                                {
                                    world.setBlockState(blockPos, Blocks.FIRE.getDefaultState());
                                }
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
