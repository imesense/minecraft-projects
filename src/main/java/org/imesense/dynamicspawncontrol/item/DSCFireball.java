package org.imesense.dynamicspawncontrol.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;

import javax.annotation.Nonnull;

/**
 *
 */
public final class DSCFireball extends EntityFireball
{
    /**
     *
     */
    private double explosionStrength = 1.00;

    /**
     *
     */
    private interface FireSpawnAction
    {
        /**
         *
         * @param world
         * @param blockPos
         * @param radius
         */
        void spawnFire(World world, BlockPos blockPos, int radius);
    }

    /**
     *
     * @param worldIn
     */
    public DSCFireball(World worldIn)
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
    public DSCFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
    }

    /**
     *
     * @param worldIn
     * @param entityLivingBase
     * @param accelX
     * @param accelY
     * @param accelZ
     */
    public DSCFireball(World worldIn, EntityLivingBase entityLivingBase, double accelX, double accelY, double accelZ)
    {
        super(worldIn, entityLivingBase, accelX, accelY, accelZ);
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
     * @param rayTraceResult
     */
    @Override
    protected void onImpact(@Nonnull RayTraceResult rayTraceResult)
    {
        if (!this.world.isRemote)
        {
            this.world.createExplosion(this.shootingEntity,
                    this.posX, this.posY, this.posZ, (float)this.explosionStrength, true);

            FireSpawnAction fireSpawnAction = (world, explosionPos, radius) ->
            {
                for (int x = -radius; x <= radius + UniqueField.RANDOM.nextInt(5); x++)
                {
                    for (int y = -radius; y <= radius + UniqueField.RANDOM.nextInt(5); y++)
                    {
                        for (int z = -radius; z <= radius + UniqueField.RANDOM.nextInt(5); z++)
                        {
                            BlockPos blockPos = explosionPos.add(x, y, z);
                            double distanceSq = explosionPos.distanceSq(blockPos);

                            if (distanceSq <= radius * radius && world.getBlockState(blockPos).getBlock() == Blocks.AIR)
                            {
                                if (UniqueField.RANDOM.nextFloat() < 0.1f)
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
