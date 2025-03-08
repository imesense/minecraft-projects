package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.dropexperience.storage.GeneralDropExperience;

import java.util.List;

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

        List<GeneralDropExperience.Data> dropExperienceList = GeneralDropExperience.getInstance().dropExperienceList;

        for (GeneralDropExperience.Data data : dropExperienceList)
        {
            if (entityResourceLocation.equals(data.entity))
            {
                int originalXp = livingExperienceDropEvent.getDroppedExperience();
                int modifyXp = modifyXp(originalXp, data);

                livingExperienceDropEvent.setDroppedExperience(modifyXp);

                return;
            }
        }
    }

    private int modifyXp(int xpIn, GeneralDropExperience.Data data)
    {
        if (data.xp != 0)
        {
            xpIn = data.xp;
        }

        int modifiedXp = (int) (xpIn * data.multi_xp + data.adding_xp);

        return modifiedXp;
    }
}
