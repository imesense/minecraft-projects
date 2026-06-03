package org.imesense.dynamicspawncontrol.core.register.event.living;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.base.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.living.*;

@InitLog
public final class EventLivingRegister extends BaseEventRegister
{
    public EventLivingRegister()
    {

    }

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
