package org.imesense.dynamicspawncontrol.ai.spider.util.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.ai.spider.util.attackweb.IWebSlinger;
import org.imesense.dynamicspawncontrol.ai.spider.util.attackweb.WebSlingerCapability;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.config.spiderattackweb.DataSpiderAttackWeb;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.customlibrary.SimpleCapabilityProvider;
import org.imesense.dynamicspawncontrol.technical.customlibrary.thing.EntityThingBase;
import org.imesense.dynamicspawncontrol.technical.customlibrary.thing.IThingBase;
import org.imesense.dynamicspawncontrol.technical.customlibrary.thing.TileEntityThingBase;

import java.util.Arrays;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnWebAttackEvent
{
    /**
     *
     */
    public OnWebAttackEvent()
    {
        CodeGenericUtil.printInitClassToLog(this.getClass());
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void onAttachCapabilitiesTileEntity_0(AttachCapabilitiesEvent<TileEntity> event)
    {
        TileEntity entity = event.getObject();

        int priority = getEntityPriority(entity);

        if (priority > 0)
        {
            event.addCapability(WebSlingerCapability.ID, new SimpleCapabilityProvider<IWebSlinger>
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
                    final IWebSlinger CAP = super.getInstance();
                    CAP.checkInit(this.OWNER, this.TASK_PRIORITY);

                    return CAP;
                }
            });
        }
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void onAttachCapabilitiesEntity_1(AttachCapabilitiesEvent<Entity> event)
    {
        Entity entity = event.getObject();

        int priority = getEntityPriority(entity);

        if (priority > 0)
        {
            event.addCapability(WebSlingerCapability.ID, new SimpleCapabilityProvider<IWebSlinger>
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
                    final IWebSlinger CAP = super.getInstance();
                    CAP.checkInit(this.OWNER, this.TASK_PRIORITY);

                    return CAP;
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
        //if (entity instanceof EntitySpider)
        //{
        //    return 3;
        //}
        /*else*/ if (entity instanceof EntityLivingBase)
        {
            String entityId = EntityList.getEntityString((EntityLivingBase) entity);

            Log.writeDataToLogFile(0, "entity 1: " + entityId);

            if (entityId != null && Arrays.asList(DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getEntityIds()).contains(entityId))
            {
                Log.writeDataToLogFile(0, "entity 2: " + DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getAIPrioritySlingWebs());

                return DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getAIPrioritySlingWebs();
            }
        }

        return -1;
    }
}
