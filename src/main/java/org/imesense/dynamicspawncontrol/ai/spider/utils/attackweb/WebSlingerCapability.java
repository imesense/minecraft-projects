package org.imesense.dynamicspawncontrol.ai.spider.utils.attackweb;

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
import org.imesense.dynamicspawncontrol.ai.spider.task.AISpiderWebAttackTask;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.config.spiderattackweb.DataSpiderAttackWeb;
import org.imesense.dynamicspawncontrol.technical.customlibrary.thing.EntityThing;
import org.imesense.dynamicspawncontrol.technical.customlibrary.thing.IThing;

@Mod.EventBusSubscriber
public class WebSlingerCapability implements IWebSlinger
{
    @CapabilityInject(IWebSlinger.class)
    public static final Capability<IWebSlinger> CAPABILITY = null;

    public static final EnumFacing DEFAULT_FACING = null;

    public static final ResourceLocation ID = new ResourceLocation("dynamicspawncontrol", "webslinger");

    IThing owner;

    public void checkInit(IThing ownerIn, int taskPriority)
    {
        if (this.owner != ownerIn)
        {
            this.owner = ownerIn;
            this.initialize(taskPriority);
        }

    }

    protected EntityLiving getOwner()
    {
        if (this.owner instanceof EntityThing)
        {
            EntityThing thing = (EntityThing) CodeGenericUtils.as(this.owner, EntityThing.class);
            EntityLiving living = (EntityLiving)CodeGenericUtils.as(thing.owner, EntityLiving.class);

            return living;
        }
        else
        {
            return null;
        }
    }

    protected void initialize(int taskPriority)
    {
        if (DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getSlingWebbing())
        {
            EntityLiving living = this.getOwner();

            if (living != null)
            {
                living.tasks.addTask(taskPriority, new AISpiderWebAttackTask(living));
            }
        }

    }

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IWebSlinger.class, new WebSlingerStorage(), () ->
        {
            return new WebSlingerCapability();
        });
    }

    WebSlingerCapability()
    {
        this.owner = null;
        MinecraftForge.EVENT_BUS.register(this);
    }

    WebSlingerCapability(IThing ownerIn, int taskPriority)
    {
        this();
        this.checkInit(ownerIn, taskPriority);
    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event)
    {
        Entity immediateSource = event.getSource().getImmediateSource();
        Entity trueSource = event.getSource().getTrueSource();
        Entity target = event.getEntity();
        EntityLiving myowner = this.getOwner();

        if (immediateSource != null && myowner != null && immediateSource == myowner && immediateSource == trueSource)
        {
            tryAttack(immediateSource, trueSource, target);
        }

    }

    protected static void tryAttack(Entity immediateSource, Entity source, Entity target)
    {
        World world = target.world;

        if (!(DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getWebMeleeChance() <= world.rand.nextDouble()))
        {
            if (target != null && immediateSource != null)
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
