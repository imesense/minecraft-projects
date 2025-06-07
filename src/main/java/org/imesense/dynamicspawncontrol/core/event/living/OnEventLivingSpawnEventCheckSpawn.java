package org.imesense.dynamicspawncontrol.core.event.living;

import com.dhanantry.scapeandrunparasites.entity.EntityBody;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityCrux;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityHeed;
import com.dhanantry.scapeandrunparasites.entity.monster.hijacked.EntityHiGolem;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityFlog;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityGanro;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityOrch;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventCheckSpawnOld;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventWorldCacheOld;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@InitLog
@TODO(
        value = "Перенести в отдельный класс: onCheckSpawn(LivingSpawnEvent.CheckSpawn event). По возможности сделать еще один парсер, который будет управлять правилом спавна",
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
        OnEventWorldCacheOld.getInstance().handleEntitySpawnEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void OnLivingSpawnEventCheckSpawnLOW(LivingSpawnEvent.CheckSpawn event)
    {
        OnEventCheckSpawnOld.getInstance().handleLivingSpawnEventCheckSpawn(event);
    }

    private static final Set<Class<? extends Entity>> ALLOWED_ENTITIES = new HashSet<>(Arrays.asList(
            EntityHiGolem.class,
            EntityGanro.class,
            EntityBody.class,
            EntityFlog.class,
            EntityCrux.class,
            EntityHeed.class,
            EntityOrch.class
    ));

    @SubscribeEvent
    public void onCheckSpawn(LivingSpawnEvent.CheckSpawn event)
    {
        if (ALLOWED_ENTITIES.contains(event.getEntity().getClass()))
        {
            event.setResult(Event.Result.ALLOW);
        }
    }
}
