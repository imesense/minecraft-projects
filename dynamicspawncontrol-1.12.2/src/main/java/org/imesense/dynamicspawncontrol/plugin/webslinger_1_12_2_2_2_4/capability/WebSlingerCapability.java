package org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
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
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.webbing.AIWebbingAttack;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.webbing.EntityWebbing;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.config.DataWebSlinger;
import org.imesense.dynamicspawncontrol.plugin.wumpleutil_1_12_2_2_12_9.util.adapter.EntityThingBase;
import org.imesense.dynamicspawncontrol.plugin.wumpleutil_1_12_2_2_12_9.util.adapter.IThingBase;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
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
            EntityThingBase thing = CodeGeneric.as(this.owner, EntityThingBase.class);

            assert thing != null;

            return CodeGeneric.as(thing.Owner, EntityLiving.class);
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
        if (DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getSlingWebbing())
        {
            EntityLiving entityLiving = this.getOwner();

            if (entityLiving != null)
            {
                entityLiving.tasks.addTask(taskPriority, new AIWebbingAttack(entityLiving));
            }
        }
    }

    /**
     *
     */
    public static void register()
    {
        /**
         *
         */
        CapabilityManager.INSTANCE.register(IWebSlinger.class, new Capability.IStorage<IWebSlinger>()
        {
            /**
             *
             * @param iWebSlingerCapability
             * @param iWebSlinger
             * @param enumFacing
             * @return
             */
            @Override
            public NBTBase writeNBT(Capability<IWebSlinger> iWebSlingerCapability, IWebSlinger iWebSlinger, EnumFacing enumFacing)
            {
                return new NBTTagCompound();
            }

            /**
             *
             * @param iWebSlingerCapability
             * @param iWebSlinger
             * @param enumFacing
             * @param nbtBase
             */
            @Override
            public void readNBT(Capability<IWebSlinger> iWebSlingerCapability, IWebSlinger iWebSlinger, EnumFacing enumFacing, NBTBase nbtBase)
            {

            }

        }, WebSlingerCapability::new);
    }

    /**
     *
     * @param livingAttackEvent
     */
    @SubscribeEvent
    public synchronized void onLivingAttack(LivingAttackEvent livingAttackEvent)
    {
        Entity targetEntity = livingAttackEvent.getEntity();
        EntityLiving ownerEntity = this.getOwner();
        Entity attackerEntity = livingAttackEvent.getSource().getTrueSource();
        Entity damageSourceEntity = livingAttackEvent.getSource().getImmediateSource();

        if (ownerEntity != null && damageSourceEntity == ownerEntity && damageSourceEntity == attackerEntity)
        {
            tryAttack(damageSourceEntity, attackerEntity, targetEntity);
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

        if (!(DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getWebMeleeChance() <= world.rand.nextDouble()))
        {
            if (immediateSource != null)
            {
                double distance = immediateSource.getDistanceSq(target);

                if (distance > 2.0D)
                {
                    return;
                }
            }

            BlockPos blockPos = new BlockPos(target.posX, target.posY, target.posZ);
            EntityWebbing.onHit(world, blockPos, source, target);
        }
    }
}
