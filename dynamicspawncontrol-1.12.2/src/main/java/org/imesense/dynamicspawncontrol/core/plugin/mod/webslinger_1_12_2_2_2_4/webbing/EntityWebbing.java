package org.imesense.dynamicspawncontrol.core.plugin.mod.webslinger_1_12_2_2_2_4.webbing;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.core.pluginconfig.webslinger.PluginWebslingerConfig;
import org.imesense.dynamicspawncontrol.core.plugin.mod.webslinger_1_12_2_2_2_4.ObjectHandlerClient;

public final class EntityWebbing extends EntityThrowable
{
    public static final EnumParticleTypes ENUM_PARTICLE_TYPES;

    public EntityWebbing(World worldIn)
    {
        super(worldIn);
    }

    public EntityWebbing(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

    protected void onImpact(RayTraceResult result)
    {
        boolean doIt = true;
        EntityLivingBase thrower = this.getThrower();

        if (result.entityHit != null && result.entityHit != thrower)
        {
            if (!(result.entityHit instanceof EntitySpider))
            {
                result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, thrower), 0.f);
            }
        }

        if (result.typeOfHit != RayTraceResult.Type.MISS)
        {
            BlockPos pos;

            if (result.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                if (!PluginWebslingerConfig.getInstance(PluginWebslingerConfig.class).isSlingWebbingOnWeb())
                {
                    IBlockState state = this.world.getBlockState(result.getBlockPos());
                    Block oldBlock = state.getBlock();
                    doIt &= oldBlock != Blocks.WEB;
                }

                pos = result.getBlockPos().offset(result.sideHit);
            }
            else
            {
                assert result.entityHit != null;

                pos = result.entityHit.getPosition();
            }

            doIt &= this.getThrower() != result.entityHit;

            if (doIt)
            {
                onHit(this.world, pos, thrower, result.entityHit);
            }
        }

        if (doIt && !this.world.isRemote)
        {
            byte thisState = 3;
            this.world.setEntityState(this, thisState);
            this.setDead();
        }
    }

    public static EntityWebbing sling(World worldIn, EntityLivingBase entityIn)
    {
        EntityWebbing entity = null;
        float pitch = 1.0f / (entityIn.getRNG().nextFloat() * 0.4f + 0.8f);

        entityIn.playSound(ObjectHandlerClient.WebbingShoot, 1.0f, pitch);

        if (!worldIn.isRemote)
        {
            entity = new EntityWebbing(worldIn, entityIn);
            float inaccuracy = PluginWebslingerConfig.getInstance(PluginWebslingerConfig.class).getSlingInaccuracy();

            entity.shoot(entityIn, entityIn.rotationPitch, entityIn.rotationYaw, 0.0f, 1.1f, inaccuracy);
            worldIn.spawnEntity(entity);
        }

        return entity;
    }

    public static void onHit(World world, BlockPos pos, Entity source, Entity target)
    {
        IBlockState state = world.getBlockState(pos);
        Block oldBlock = state.getBlock();

        boolean stick = oldBlock.isReplaceable(world, pos) &&
                (PluginWebslingerConfig.getInstance(PluginWebslingerConfig.class).isBlockWebReplacement() ||
                        oldBlock.isAir(state, world, pos));

        if (!stick)
        {
            world.playSound(null, pos, ObjectHandlerClient.WebbingNonStick,
                    SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.rand.nextFloat() * 0.4f + 0.8f));
        }
        else
        {
            world.playSound(null, pos, ObjectHandlerClient.WebbingStick,
                    SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.rand.nextFloat() * 0.4f + 0.8f));

            if (!world.isRemote)
            {
                world.setBlockState(pos, Blocks.WEB.getDefaultState());

                if (target != null)
                {
                    target.setInWeb();

                    if (target instanceof EntityPlayerMP)
                    {
                        DynamicSpawnControl.networkWrapper.sendTo(new PlayerInWebMessage(pos), (EntityPlayerMP)target);
                    }
                }
            }
        }
    }

    static
    {
        ENUM_PARTICLE_TYPES = EnumParticleTypes.SNOWBALL;
    }
}
