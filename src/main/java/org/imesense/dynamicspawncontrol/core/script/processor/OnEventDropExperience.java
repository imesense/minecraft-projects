package org.imesense.dynamicspawncontrol.core.script.processor;

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
        String entityName = livingExperienceDropEvent.getEntity().getName();
        List<GeneralDropExperience.Data> dropExperienceList = GeneralDropExperience.getInstance().dropExperienceList;

        Log.writeDataToLogFile(0, "Entity name from event: " + entityName);

        for (GeneralDropExperience.Data data : dropExperienceList)
        {
            Log.writeDataToLogFile(0, "Entity name from config: " + data.entity);

            if (entityName.equals(data.entity))
            {
                int originalXp = livingExperienceDropEvent.getDroppedExperience();
                int modifyXp = modifyXp(originalXp, data);
                livingExperienceDropEvent.setDroppedExperience(modifyXp);

                Log.writeDataToLogFile(0, "Original XP: " + originalXp);
                Log.writeDataToLogFile(0, "Modified XP: " + modifyXp);

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

        Log.writeDataToLogFile(0, "Calculated modified XP: " + modifiedXp);

        return modifiedXp;
    }
}
