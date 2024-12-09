package org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.config.DataWebSlinger;
import org.imesense.dynamicspawncontrol.plugin.wumpleutil_1_12_2_2_12_9.util.container.SimpleCapabilityProvider;
import org.imesense.dynamicspawncontrol.plugin.wumpleutil_1_12_2_2_12_9.util.adapter.EntityThingBase;
import org.imesense.dynamicspawncontrol.plugin.wumpleutil_1_12_2_2_12_9.util.adapter.IThingBase;
import org.imesense.dynamicspawncontrol.plugin.wumpleutil_1_12_2_2_12_9.util.adapter.TileEntityThingBase;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class EventHandler
{
    /**
     *
     */
    public EventHandler()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    /**
     *
     * @param attachCapabilitiesEvent
     */
    @SubscribeEvent
    public synchronized void onAttachCapabilitiesTileEntity_0(AttachCapabilitiesEvent<TileEntity> attachCapabilitiesEvent)
    {
        TileEntity entity = attachCapabilitiesEvent.getObject();

        int priority = getEntityPriority(entity);

        if (priority > 0)
        {
            attachCapabilitiesEvent.addCapability(WebSlingerCapability.ID, new SimpleCapabilityProvider<IWebSlinger>
            (
                WebSlingerCapability.CAPABILITY,
                WebSlingerCapability.DEFAULT_FACING,
                WebSlingerCapability.CAPABILITY.getDefaultInstance())
            {
                /**
                 *
                 */
                private final int TASK_PRIORITY = priority;

                /**
                 *
                 */
                private final IThingBase OWNER = new TileEntityThingBase(entity);

                /**
                 *
                 * @return
                 */
                @Override
                public IWebSlinger getInstance()
                {
                    IWebSlinger cap = super.getInstance();
                    cap.checkInit(this.OWNER, this.TASK_PRIORITY);

                    return cap;
                }
            });
        }
    }

    /**
     *
     * @param attachCapabilitiesEvent
     */
    @SubscribeEvent
    public synchronized void onAttachCapabilitiesEntity_1(AttachCapabilitiesEvent<Entity> attachCapabilitiesEvent)
    {
        Entity entity = attachCapabilitiesEvent.getObject();

        int priority = getEntityPriority(entity);

        if (priority > 0)
        {
            attachCapabilitiesEvent.addCapability(WebSlingerCapability.ID, new SimpleCapabilityProvider<IWebSlinger>
            (
                WebSlingerCapability.CAPABILITY,
                WebSlingerCapability.DEFAULT_FACING,
                WebSlingerCapability.CAPABILITY.getDefaultInstance())
            {
                /**
                 *
                 */
                private final int TASK_PRIORITY = priority;

                /**
                 *
                 */
                private final IThingBase OWNER = new EntityThingBase(entity);

                /**
                 *
                 * @return
                 */
                @Override
                public IWebSlinger getInstance()
                {
                    IWebSlinger cap = super.getInstance();
                    cap.checkInit(this.OWNER, this.TASK_PRIORITY);

                    return cap;
                }
            });
        }
    }

    /**
     *
     * @param entity
     * @return
     */
    private static int getEntityPriority(Object entity)
    {
        if (entity instanceof EntityLivingBase)
        {
            ResourceLocation entityId = EntityList.getKey((EntityLivingBase) entity);

            if (entityId != null)
            {
                //Log.writeDataToLogFile(0, "entity 1: " + entityId.toString());

                int priority = DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getEntityPriority(entityId.toString());

                if (priority > 0)
                {
                   // Log.writeDataToLogFile(0, "entity 2: " + priority);
                    return priority;
                }
            }
        }

        return -1;
    }
}
