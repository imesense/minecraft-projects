package org.imesense.dynamicspawncontrol.core.event.living;

import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventDropItem;
import org.imesense.dynamicspawncontrol.eventdescriptions.DropSkeletonItem;
import org.imesense.dynamicspawncontrol.eventdescriptions.DropZombieItem;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventLivingDropsEvent extends BaseOnEventInstance
{
    public OnEventLivingDropsEvent()
    {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnLivingDropsEvent(LivingDropsEvent event)
    {
        DropSkeletonItem.getInstance().handleLivingDrops(event);
        DropZombieItem.getInstance().handleZombieDrops(event);

        OnEventDropItem.getInstance().handleUpdateLivingDrops(event);
    }
}
