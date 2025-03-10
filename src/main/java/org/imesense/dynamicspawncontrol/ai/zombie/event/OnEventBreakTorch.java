package org.imesense.dynamicspawncontrol.ai.zombie.event;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.ai.zombie.action.EntityAIZombieBreakTorch;
import org.imesense.dynamicspawncontrol.core.register.attach.AttachRegister;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventBreakTorch
{
    private static volatile OnEventBreakTorch _INSTANCE;

    public static OnEventBreakTorch getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (OnEventBreakTorch.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new OnEventBreakTorch();
                }
            }
        }

        return _INSTANCE;
    }

    /**
     *
     */
    public OnEventBreakTorch()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    public void handleSearchToBreakTorch(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityZombie)
        {
            EntityZombie eventEntity = (EntityZombie) event.getEntity();
            eventEntity.tasks.addTask(1, new EntityAIZombieBreakTorch(eventEntity));
        }
    }
}
