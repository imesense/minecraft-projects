package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.script.storage.dropexperience.storage.GeneralDropExperience;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
public final class OnEventDropExperience
{
    private static volatile OnEventDropExperience _INSTANCE;

    public static OnEventDropExperience getInstance()
    {
        return CodeGeneric.getInstance(OnEventDropExperience.class);
    }

    public OnEventDropExperience()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void handleUpdateLivingExperienceDrop(LivingExperienceDropEvent event)
    {
        Entity entity = event.getEntity();
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
                    Integer originalXp = event.getDroppedExperience();
                    Integer modifyXp = (int) ((data.xp != 0 ? data.xp : originalXp) * data.multi_xp + data.adding_xp);

                    event.setDroppedExperience(modifyXp);
                });
    }
}
