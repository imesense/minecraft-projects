package org.imesense.dynamicspawncontrol.ai.zombie.event;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.ai.zombie.action.EntityAIZombieBreakTorch;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventBreakTorch
{
    /**
     *
     */
    public OnEventBreakTorch()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    /**
     *
     * @param entityJoinWorldEvent
     */
    @SubscribeEvent
    public static void onSearchToBreakTorch_0(EntityJoinWorldEvent entityJoinWorldEvent)
    {
        if (entityJoinWorldEvent.getEntity() instanceof EntityZombie)
        {
            EntityZombie eventEntity = (EntityZombie) entityJoinWorldEvent.getEntity();
            eventEntity.tasks.addTask(1, new EntityAIZombieBreakTorch(eventEntity));
        }
    }
}
