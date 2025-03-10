package org.imesense.dynamicspawncontrol.core.event.living;

import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventCheckSpawn;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventLivingSpawnEventCheckSpawn extends BaseOnEventInstance
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void OnLivingSpawnEventCheckSpawn(LivingSpawnEvent.CheckSpawn checkSpawn)
    {
        OnEventCheckSpawn.getInstance().handleLivingSpawnEventCheckSpawn(checkSpawn);
    }
}
