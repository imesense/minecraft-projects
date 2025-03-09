package org.imesense.dynamicspawncontrol.core.event.living;

import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.eventdescriptions.UpdateFire;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventLivingAttackEvent
{
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onLivingAttackEvent(LivingAttackEvent event)
    {
        UpdateFire.getInstance().handleLivingAttack(event);
    }
}
