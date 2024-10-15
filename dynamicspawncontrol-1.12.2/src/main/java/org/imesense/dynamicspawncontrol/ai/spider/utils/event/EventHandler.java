package org.imesense.dynamicspawncontrol.ai.spider.utils.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ai.spider.utils.attackweb.WebSlingerCapability;
import org.imesense.dynamicspawncontrol.ai.spider.utils.attackweb.WebSlingerProvider;
import org.imesense.dynamicspawncontrol.technical.customlibrary.thing.EntityThing;
import org.imesense.dynamicspawncontrol.technical.customlibrary.thing.TileEntityThing;

@Mod.EventBusSubscriber
public class EventHandler
{
    @SubscribeEvent
    public static void attachCapabilitiesTileEntity(AttachCapabilitiesEvent<TileEntity> event)
    {
        TileEntity entity = (TileEntity)event.getObject();

        int priority = getEntityPriority(entity);

        if (doesIt(priority))
        {
            WebSlingerProvider provider = new WebSlingerProvider(
                    WebSlingerCapability.CAPABILITY,
                    WebSlingerCapability.DEFAULT_FACING,
                    new TileEntityThing(entity),
                    priority
            );

            event.addCapability(WebSlingerCapability.ID, provider);
        }
    }

    @SubscribeEvent
    public static void attachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event)
    {
        Entity entity = (Entity)event.getObject();

        int priority = getEntityPriority(entity);

        if (doesIt(priority))
        {
            WebSlingerProvider provider = new WebSlingerProvider(
                    WebSlingerCapability.CAPABILITY,
                    WebSlingerCapability.DEFAULT_FACING,
                    new EntityThing(entity),
                    priority
            );

            event.addCapability(WebSlingerCapability.ID, provider);
        }
    }

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

    private static boolean doesIt(int priority)
    {
        return priority > 0;
    }
}
