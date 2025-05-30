package org.imesense.dynamicspawncontrol.core.event.living;

import com.dhanantry.scapeandrunparasites.entity.EntityBody;
import com.dhanantry.scapeandrunparasites.entity.monster.hijacked.EntityHiGolem;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityFlog;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityGanro;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventCheckSpawn;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventWorldCache;

@InitLog
@TODO(
        value = "Перенести в отдельный класс: onCheckSpawn(LivingSpawnEvent.CheckSpawn event)",
        priority = TODO.TodoPriority.HIGH,
        showOnce = false
)
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventLivingSpawnEventCheckSpawn extends BaseOnEventInstance
{
    public OnEventLivingSpawnEventCheckSpawn()
    {

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void OnLivingSpawnEventCheckSpawnHIGHEST(LivingSpawnEvent.CheckSpawn event)
    {
        OnEventWorldCache.getInstance().handleEntitySpawnEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void OnLivingSpawnEventCheckSpawnLOW(LivingSpawnEvent.CheckSpawn event)
    {
        OnEventCheckSpawn.getInstance().handleLivingSpawnEventCheckSpawn(event);
    }

    @SubscribeEvent
    public void onCheckSpawn(LivingSpawnEvent.CheckSpawn event)
    {
        if (event.getEntity() instanceof EntityHiGolem)
        {
            event.setResult(Event.Result.ALLOW);
        }

        if (event.getEntity() instanceof EntityGanro)
        {
            event.setResult(Event.Result.ALLOW);
        }

        if (event.getEntity() instanceof EntityBody)
        {
            event.setResult(Event.Result.ALLOW);
        }

        if (event.getEntity() instanceof EntityFlog)
        {
            event.setResult(Event.Result.ALLOW);
        }
    }
}
