package org.imesense.dynamicspawncontrol.core.register.event.living;

import org.imesense.dynamicspawncontrol.core.api.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.living.*;

public final class Register extends BaseEventRegister
{
    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventLivingAttackEvent.class,
        OnEventLivingDeathEvent.class,
        OnEventLivingDropsEvent.class,
        OnEventLivingEventLivingUpdateEvent.class,
        OnEventLivingExperienceDropEvent.class,
        OnEventLivingHurtEvent.class,
        OnEventLivingSpawnEventCheckSpawn.class,
        OnEventLivingSpawnEventSpecialSpawn.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
