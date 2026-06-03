package org.imesense.dynamicspawncontrol.core.event.living;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.ai.zombie.event.ZombieHasShieldEventHandler;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.base.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.eventdescriptions.ComplexityBiomes;
import org.imesense.dynamicspawncontrol.eventdescriptions.UpdateFire;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventLivingEventLivingUpdateEvent extends BaseOnEventInstance
{
    public OnEventLivingEventLivingUpdateEvent()
    {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnLivingEventLivingUpdateEvent(LivingEvent.LivingUpdateEvent event)
    {
        if (event.getEntity() instanceof EntityPlayerMP)
        {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) event.getEntity();
            ComplexityBiomes.getInstance().handleBiomesChange(entityPlayerMP);
        }

        if (!event.getEntity().world.isRemote)
        {
            ZombieHasShieldEventHandler.getInstance().handleLivingUpdate(event);
        }

        UpdateFire.getInstance().handleLivingTick(event);
    }
}
