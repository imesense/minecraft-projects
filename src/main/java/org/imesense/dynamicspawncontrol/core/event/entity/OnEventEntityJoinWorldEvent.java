package org.imesense.dynamicspawncontrol.core.event.entity;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.ai.spider.event.OnEventAvoidLight;
import org.imesense.dynamicspawncontrol.ai.zombie.event.OnEventBreakTorch;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.memory.MemoryEvents;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventMobTaskManager;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventWorldCache;
import org.imesense.dynamicspawncontrol.eventdescriptions.PlayerNetwork;
import org.imesense.dynamicspawncontrol.eventdescriptions.UpdateFire;

@TODO(value = "Отключена до следующих версий, логика, когда зомби идет разбивать факел. Не правильно работает приоритет",
        showOnce = false,
        priority = TODO.TodoPriority.HIGH)
@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventEntityJoinWorldEvent extends BaseOnEventInstance
{
    public OnEventEntityJoinWorldEvent()
    {

    }

    static boolean isTestLogic = false;

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnEntityJoinWorldEvent_LOW(EntityJoinWorldEvent event)
    {
        PlayerNetwork.getInstance().handlePlayerJoinWorld(event);

        UpdateFire.getInstance().handleEntityJoinWorld(event);

        if (isTestLogic)
        {
            OnEventBreakTorch.getInstance().handleSearchToBreakTorch(event);
        }

        OnEventAvoidLight.getInstance().handleSpiderSpawn(event);

        OnEventMobTaskManager.getInstance().handleUpdateEntityJoinWorld(event);

        OnEventWorldCache.getInstance().handleEntityJoinWorld(event);

        MemoryEvents.handleOnPlayerLogin(event);
    }
}
