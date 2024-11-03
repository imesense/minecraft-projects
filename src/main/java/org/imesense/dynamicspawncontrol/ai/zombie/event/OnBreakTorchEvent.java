package org.imesense.dynamicspawncontrol.ai.zombie.event;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.ai.zombie.entityaibase.BreakTorchTask;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnBreakTorchEvent
{
    /**
     *
     */
    public OnBreakTorchEvent()
    {
        CodeGenericUtil.printInitClassToLog(this.getClass());
    }

    /**
     *
     * @param entityJoinWorldEvent
     */
    @SubscribeEvent
    public synchronized void onSearchToBreakTorch_0(EntityJoinWorldEvent entityJoinWorldEvent)
    {
        if (entityJoinWorldEvent.getEntity() instanceof EntityZombie)
        {
            EntityZombie eventEntity = (EntityZombie) entityJoinWorldEvent.getEntity();
            eventEntity.tasks.addTask(1, new BreakTorchTask(eventEntity));
        }
    }
}
