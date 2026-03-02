package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.dropexperience.data.EntityDropExperience;
import org.imesense.dynamicspawncontrol.core.script.storage.dropexperience.storage.GeneralDropExperience;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
@TODO(value = "Поломана оптимизация, к тому же переделать класс на схеме", showOnce = false, priority = TODO.TodoPriority.HIGH)
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
        if (DisableEventBooleansTest.test)
            return;

        Entity entity = event.getEntity();
        World world = entity.world;
        ResourceLocation entityResourceLocation = EntityList.getKey(entity);

        if (entityResourceLocation == null)
        {
            return;
        }

        int currentDimension = world.provider.getDimension();

        GeneralDropExperience.getInstance().dropExperienceList.stream()
            .filter(data -> entityResourceLocation.equals(data.entity))
            .filter(data -> isDimensionValid(data, currentDimension))
            .filter(data -> data.isTimeValid(world))
            .findFirst()
        .ifPresent(data ->
        {
            if (data.result == Event.Result.DENY)
            {
                event.setCanceled(true);

                Log.write(0, String.format("XP drop DENIED for %s in dimension %d", entity.getName(), currentDimension));

                return;
            }

            if (data.result == Event.Result.ALLOW || data.result == Event.Result.DEFAULT)
            {
                int modifiedXp;
                int originalXp = event.getDroppedExperience();

                    Log.write(0, String.format("Processing entity: %s, dimension: %d, originalXP: %d, time: %d",
                            entity.getName(), currentDimension, originalXp, world.getWorldTime() % 24000));

                if (data.use_default_xp)
                {
                    modifiedXp = (int)(originalXp * data.multi_xp);

                    Log.write(0, String.format("Entity: %s, default XP mode: %d * %.2f = %d (dimension: %d)",
                            entity.getName(), originalXp, data.multi_xp, modifiedXp, currentDimension));
                }
                else
                {
                    int baseXp = data.xp != null ? data.xp : originalXp;
                    float adding = data.adding_xp != null ? data.adding_xp : 0;

                    modifiedXp = (int)(baseXp * data.multi_xp + adding);

                    Log.write(0, String.format(
                            "Custom XP mode: (%d * %.1f) + %.1f = %d (dimension: %d)",
                            baseXp, data.multi_xp, adding, modifiedXp, currentDimension
                    ));
                }

                event.setDroppedExperience(modifiedXp);

                Log.write(0, String.format("XP set to %d for %s in dimension %d",
                        modifiedXp, entity.getName(), currentDimension));
            }
        });
    }

    private boolean isDimensionValid(EntityDropExperience.Data data, int currentDimension)
    {
        if (data.idDimension == null)
        {
            return true;
        }

        boolean isValid = data.idDimension == currentDimension;

        Log.write(0, String.format("Dimension check for rule: expected %s, current %d = %s",
                data.idDimension != null ? data.idDimension.toString() : "any",
                currentDimension,
                isValid ? "VALID" : "INVALID"));

        return isValid;
    }
}