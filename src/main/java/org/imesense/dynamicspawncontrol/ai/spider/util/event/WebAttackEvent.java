package org.imesense.dynamicspawncontrol.ai.spider.util.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.ai.spider.util.attackweb.WebSlingerCapability;
import org.imesense.dynamicspawncontrol.ai.spider.util.attackweb.WebSlingerProvider;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.thing.EntityThingBase;
import org.imesense.dynamicspawncontrol.technical.customlibrary.thing.TileEntityThingBase;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class WebAttackEvent
{
    /**
     *
     */
    public WebAttackEvent()
    {
        CodeGenericUtils.printInitClassToLog(this.getClass());
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void attachCapabilitiesTileEntity(AttachCapabilitiesEvent<TileEntity> event)
    {
        TileEntity entity = event.getObject();

        int priority = getEntityPriority(entity);

        if (doesIt(priority))
        {
            WebSlingerProvider provider = new WebSlingerProvider(
                    WebSlingerCapability.CAPABILITY,
                    WebSlingerCapability.DEFAULT_FACING,
                    new TileEntityThingBase(entity),
                    priority
            );

            event.addCapability(WebSlingerCapability.ID, provider);
        }
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void attachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event)
    {
        Entity entity = event.getObject();

        int priority = getEntityPriority(entity);

        if (doesIt(priority))
        {
            WebSlingerProvider provider = new WebSlingerProvider(
                    WebSlingerCapability.CAPABILITY,
                    WebSlingerCapability.DEFAULT_FACING,
                    new EntityThingBase(entity),
                    priority
            );

            event.addCapability(WebSlingerCapability.ID, provider);
        }
    }

    /**
     *
     * @param entity
     * @return
     */
    private static int getEntityPriority(Object entity)
    {
        if (entity instanceof EntitySpider)
        {
            return 3;
        }
        else
        {
            return -1;
        }
    }

    /**
     *
     * @param priority
     * @return
     */
    private static boolean doesIt(int priority)
    {
        return priority > 0;
    }
}
