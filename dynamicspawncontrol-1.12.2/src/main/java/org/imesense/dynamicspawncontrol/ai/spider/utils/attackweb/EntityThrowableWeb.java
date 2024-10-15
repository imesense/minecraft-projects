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

public class EntityThrowableWeb extends EntityThrowable
{
    public static final EnumParticleTypes particleType;

    public EntityThrowableWeb(World worldIn)
    {
        super(worldIn);
        this.init();
    }

    public EntityThrowableWeb(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
        this.init();
    }

    public EntityThrowableWeb(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
        this.init();
    }

    public EntityThrowableWeb(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
        this.setPosition(x, y, z);
        this.init();
    }

    protected void init()
    {

    }

    public static void registerFixesWebbing(DataFixer fixer)
    {
        EntityThrowable.registerFixesThrowable(fixer, "webbing");
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 3)
        {
            for(int i = 0; i < 3; ++i)
            {
                this.world.spawnParticle(particleType, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }

    }

    protected void onImpact(RayTraceResult result)
    {
        boolean doit = true;
        EntityLivingBase thrower = this.getThrower();

        if (result.entityHit != null && result.entityHit != thrower)
        {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, thrower), 0.0F);
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
                    doit &= oldBlock != Blocks.WEB;
                }

                pos = result.getBlockPos().offset(result.sideHit);
            }
            else
            {
                pos = result.entityHit.getPosition();
            }

            doit &= this.getThrower() != result.entityHit;

            if (doit)
            {
                onHit(this.world, pos, thrower, result.entityHit);
            }
        }

        if (doit && !this.world.isRemote)
        {
            this.world.setEntityState(this, (byte)3);
            this.setDead();
        }

    }

    public static EntityThrowableWeb sling(World worldIn, EntityLivingBase entityIn)
    {
        EntityThrowableWeb entity = null;
        float pitch = 1.0F / (entityIn.getRNG().nextFloat() * 0.4F + 0.8F);

        entityIn.playSound(ObjectHandler.WEBBING_SHOOT, 1.0F, pitch);

        if (!worldIn.isRemote)
        {
            entity = new EntityThrowableWeb(worldIn, entityIn);
            float inaccuracy = DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getSlingInaccuracy();
            entity.shoot(entityIn, entityIn.rotationPitch, entityIn.rotationYaw, 0.0F, 1.1F, inaccuracy);
            worldIn.spawnEntity(entity);
        }

        return entity;
    }

    public static void onHit(World world, BlockPos pos, Entity source, Entity target)
    {
        IBlockState state = world.getBlockState(pos);
        Block oldBlock = state.getBlock();
        boolean stick = true;

        if (!oldBlock.isReplaceable(world, pos) ||
                !DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getBlockWebReplacement() && !oldBlock.isAir(state, world, pos))
        {
            stick = false;
        }

        if (!stick)
        {
            world.playSound((EntityPlayer)null, pos, ObjectHandler.WEBBING_NONSTICK,
                    SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
        }
        else
        {
            world.playSound((EntityPlayer)null, pos, ObjectHandler.WEBBING_STICK,
                    SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));

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
        particleType = EnumParticleTypes.SNOWBALL;
    }
}
