package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventDropExperience
{
    @SubscribeEvent
    public void onUpdateLivingExperienceDrop_0(LivingExperienceDropEvent livingExperienceDropEvent)
    {

    }
}
