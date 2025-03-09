package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.storage.GeneralMobTaskManager;

import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventMobTaskManager
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onUpdateEntityJoinWorld_0(EntityJoinWorldEvent event)
    {
        if (!(event.getEntity() instanceof EntityLiving))
        {
            return;
        }

        GeneralMobTaskManager.getInstance().applyHostility(event);
        GeneralMobTaskManager.getInstance().applyHostilityByIdPrefix(event);
        GeneralMobTaskManager.getInstance().applyPanicByIdPrefix(event);
        GeneralMobTaskManager.getInstance().applyHostilityToIdThemToId(event);
    }
}
