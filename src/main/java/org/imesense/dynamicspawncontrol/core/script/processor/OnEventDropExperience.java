package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
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
        World world = entity.world;
        ResourceLocation entityResourceLocation = EntityList.getKey(entity);

        if (entityResourceLocation == null)
        {
            return;
        }

        GeneralDropExperience.getInstance().dropExperienceList.stream()
            .filter(data -> entityResourceLocation.equals(data.entity))
            .filter(data -> data.isTimeValid(world))
            .findFirst()
        .ifPresent(data ->
        {
            if (data.result == Event.Result.DENY)
            {
                event.setCanceled(true);
                return;
            }

            if (data.result == Event.Result.ALLOW || data.result == Event.Result.DEFAULT)
            {
                int modifiedXp;
                int originalXp = event.getDroppedExperience();

                Log.write(0, String.format(
                        "Processing entity: %s, originalXP: %d, time: %d",
                        entity.getName(),
                        originalXp,
                        world.getWorldTime() % 24000
                ));

                if (data.use_default_xp)
                {
                    modifiedXp = (int)(originalXp * data.multi_xp);
                    Log.write(0, "Entity: " + event.getEntity() + " " + "modifiedXp: " + modifiedXp);
                }
                else
                {
                    int baseXp = data.xp != null ? data.xp : originalXp;
                    float adding = data.adding_xp != null ? data.adding_xp : 0;

                    modifiedXp = (int)(baseXp * data.multi_xp + adding);

                    Log.write(0, String.format(
                            "Custom XP mode: (%d * %.1f) + %.1f = %d",
                            baseXp, data.multi_xp, adding, modifiedXp
                    ));
                }

                event.setDroppedExperience(modifiedXp);
            }
        });
    }
}
