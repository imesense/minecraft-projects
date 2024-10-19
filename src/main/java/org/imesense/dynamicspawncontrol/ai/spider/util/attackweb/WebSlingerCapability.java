package org.imesense.dynamicspawncontrol.ai.spider.util.attackweb;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.ai.spider.entityaibase.WebAttackTask;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.config.spiderattackweb.DataSpiderAttackWeb;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.customlibrary.thing.EntityThingBase;
import org.imesense.dynamicspawncontrol.technical.customlibrary.thing.IThingBase;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class WebSlingerCapability implements IWebSlinger
{
    /**
     *
     */
    private IThingBase owner;

    /**
     *
     */
    public static final EnumFacing DEFAULT_FACING = null;

    /**
     *
     */
    @CapabilityInject(IWebSlinger.class)
    public static final Capability<IWebSlinger> CAPABILITY = null;

    /**
     *
     */
    public static final ResourceLocation ID =
            new ResourceLocation("dynamicspawncontrol", "webslinger");

    /**
     *
     */
    public WebSlingerCapability()
    {
        this.owner = null;
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     *
     * @param ownerIn
     * @param taskPriority
     */
    public void checkInit(IThingBase ownerIn, int taskPriority)
    {
        if (this.owner != ownerIn)
        {
            this.owner = ownerIn;
            this.initialize(taskPriority);
        }
    }

    /**
     *
     * @return
     */
    private EntityLiving getOwner()
    {
        if (this.owner instanceof EntityThingBase)
        {
            EntityThingBase thing = CodeGenericUtils.as(this.owner, EntityThingBase.class);

            assert thing != null;

            return CodeGenericUtils.as(thing.owner, EntityLiving.class);
        }
        else
        {
            return null;
        }
    }

    /**
     *
     * @param taskPriority
     */
    private void initialize(int taskPriority)
    {
        if (DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getSlingWebbing())
        {
            EntityLiving living = this.getOwner();

            if (living != null)
            {
                living.tasks.addTask(taskPriority, new WebAttackTask(living));
            }
        }
    }

    /**
     *
     */
    public static void register()
    {
        CapabilityManager.INSTANCE.register(IWebSlinger.class, new WebSlingerStorage(), WebSlingerCapability::new);
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void onLivingAttack(LivingAttackEvent event)
    {
        Entity target = event.getEntity();
        EntityLiving getOwner = this.getOwner();
        Entity trueSource = event.getSource().getTrueSource();
        Entity immediateSource = event.getSource().getImmediateSource();

        if (getOwner != null && immediateSource == getOwner && immediateSource == trueSource)
        {
            tryAttack(immediateSource, trueSource, target);
        }
    }

    /**
     *
     * @param immediateSource
     * @param source
     * @param target
     */
    private static void tryAttack(Entity immediateSource, Entity source, Entity target)
    {
        World world = target.world;

        if (!(DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getWebMeleeChance() <= world.rand.nextDouble()))
        {
            if (immediateSource != null)
            {
                double distance = immediateSource.getDistanceSq(target);

                if (distance > 2.0D)
                {
                    return;
                }
            }

            BlockPos pos = new BlockPos(target.posX, target.posY, target.posZ);
            EntityThrowableWeb.onHit(world, pos, source, target);
        }
    }
}
