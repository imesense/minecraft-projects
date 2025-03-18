package org.imesense.dynamicspawncontrol.core.event.living;

import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.eventdescriptions.UpdateFire;
import org.imesense.dynamicspawncontrol.core.plugin.mod.webslinger_1_12_2_2_2_4.capability.WebSlingerCapability;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventLivingAttackEvent extends BaseOnEventInstance
{
    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnLivingAttackEvent(LivingAttackEvent event)
    {
        UpdateFire.getInstance().handleLivingAttack(event);

        WebSlingerCapability.getInstance().handleLivingAttack(event);
    }
}
