package org.imesense.dynamicspawncontrol.ai.spider.utils.attackweb;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.technical.config.spiderattackweb.DataSpiderAttackWeb;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ObjectHandler;
import org.imesense.dynamicspawncontrol.technical.network.PlayerInWebMessage;

/**
 *
 */
public final class EntityThrowableWeb extends EntityThrowable
{
    /**
     *
     */
    public static final EnumParticleTypes particleType;

    /**
     *
     * @param worldIn
     */
    public EntityThrowableWeb(World worldIn)
    {
        super(worldIn);
    }

    /**
     *
     * @param worldIn
     * @param throwerIn
     */
    public EntityThrowableWeb(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

    /**
     *
     * @param worldIn
     * @param x
     * @param y
     * @param z
     */
    public EntityThrowableWeb(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
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
    public EntityThrowableWeb(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn);
        this.setSize(1.0f, 1.0f);
        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
        this.setPosition(x, y, z);
    }

    /**
     *
     * @param id
     */
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 3)
        {
            for(byte i = 0; i < 3; ++i)
            {
                this.world.spawnParticle(particleType,
                        this.posX, this.posY, this.posZ, 0.f, 0.f, 0.f);
            }
        }
    }

    /**
     *
     * @param result
     */
    protected void onImpact(RayTraceResult result)
    {
        boolean doIt = true;
        EntityLivingBase thrower = this.getThrower();

        if (result.entityHit != null && result.entityHit != thrower)
        {
            result.entityHit.attackEntityFrom(DamageSource.
                    causeThrownDamage(this, thrower), 0.f);
        }

        if (result.typeOfHit != RayTraceResult.Type.MISS)
        {
            BlockPos pos;

            if (result.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                if (!DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getSlingWebbingOnWeb())
                {
                    IBlockState state = this.world.getBlockState(result.getBlockPos());
                    Block oldBlock = state.getBlock();
                    doIt &= oldBlock != Blocks.WEB;
                }

                pos = result.getBlockPos().offset(result.sideHit);
            }
            else
            {
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

    /**
     *
     * @param worldIn
     * @param entityIn
     * @return
     */
    public static EntityThrowableWeb sling(World worldIn, EntityLivingBase entityIn)
    {
        EntityThrowableWeb entity = null;
        float pitch = 1.0f / (entityIn.getRNG().nextFloat() * 0.4f + 0.8f);

        entityIn.playSound(ObjectHandler.WEBBING_SHOOT, 1.0f, pitch);

        if (!worldIn.isRemote)
        {
            entity = new EntityThrowableWeb(worldIn, entityIn);
            Float inaccuracy = DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getSlingInaccuracy();
            entity.shoot(entityIn, entityIn.rotationPitch, entityIn.rotationYaw, 0.0f, 1.1f, inaccuracy);
            worldIn.spawnEntity(entity);
        }

        return entity;
    }

    /**
     *
     * @param world
     * @param pos
     * @param source
     * @param target
     */
    public static void onHit(World world, BlockPos pos, Entity source, Entity target)
    {
        IBlockState state = world.getBlockState(pos);
        Block oldBlock = state.getBlock();

        boolean stick = oldBlock.isReplaceable(world, pos) &&
                (DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getBlockWebReplacement() || oldBlock.isAir(state, world, pos));

        if (!stick)
        {
            world.playSound(null, pos, ObjectHandler.WEBBING_NONSTICK,
                    SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.rand.nextFloat() * 0.4f + 0.8f));
        }
        else
        {
            world.playSound(null, pos, ObjectHandler.WEBBING_STICK,
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

    /**
     *
     */
    static
    {
        particleType = EnumParticleTypes.SNOWBALL;
    }
}
