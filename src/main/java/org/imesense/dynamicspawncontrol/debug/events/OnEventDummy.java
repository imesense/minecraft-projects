package org.imesense.dynamicspawncontrol.debug.events;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ai.zombie.AIZombieBreakTorchTask;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
@Mod.EventBusSubscriber
public final class OnEventDummy
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnEventDummy()
    {
		CodeGenericUtils.printInitClassToLog(this.getClass());

        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    // перенести это в отдельный класс
    @SubscribeEvent
    public static void onZombieSpawn(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityZombie)
        {
            EntityZombie zombie = (EntityZombie) event.getEntity();
            zombie.tasks.addTask(1, new AIZombieBreakTorchTask(zombie));  //-' Добавляем новую задачу для зомби
        }
    }
}
