package org.imesense.dynamicspawncontrol.ai.zombie.event;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.ai.ZombieLightProfile;
import org.imesense.dynamicspawncontrol.ai.zombie.task.ZombieBreakTorchEntityAI;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class ZombieBreakTorchEventHandler
{
    private static volatile ZombieBreakTorchEventHandler _INSTANCE;

    public static ZombieBreakTorchEventHandler getInstance()
    {
        return CodeGeneric.getInstance(ZombieBreakTorchEventHandler.class);
    }

    public ZombieBreakTorchEventHandler()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void handleSearchToBreakTorch(EntityJoinWorldEvent event)
    {
        if (!(event.getEntity() instanceof EntityZombie))
            return;

        EntityZombie zombie = (EntityZombie) event.getEntity();

        ZombieLightProfile profile = new ZombieLightProfile(zombie);

        zombie.tasks.addTask(5, new ZombieBreakTorchEntityAI(zombie, profile));
    }
}
