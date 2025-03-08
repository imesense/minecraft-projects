package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.script.storage.dropexperience.storage.GeneralDropExperience;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventDropExperience
{
    @SubscribeEvent
    public void onUpdateLivingExperienceDrop_0(LivingExperienceDropEvent livingExperienceDropEvent)
    {
        Entity entity = livingExperienceDropEvent.getEntity();
        ResourceLocation entityResourceLocation = EntityList.getKey(entity);

        if (entityResourceLocation == null)
        {
            return;
        }

        GeneralDropExperience.getInstance().dropExperienceList.stream()
                .filter(data -> entityResourceLocation.equals(data.entity))
                .findFirst()
                .ifPresent(data ->
                {
                    Integer originalXp = livingExperienceDropEvent.getDroppedExperience();
                    Integer modifyXp = (int) ((data.xp != 0 ? data.xp : originalXp) * data.multi_xp + data.adding_xp);

                    livingExperienceDropEvent.setDroppedExperience(modifyXp);
                });
    }
}
