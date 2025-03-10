package org.imesense.dynamicspawncontrol.core.register.event.living;

import org.imesense.dynamicspawncontrol.core.baseregister.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.living.*;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.capability.EventHandler;

public final class EventLivingRegister extends BaseEventRegister
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
        OnEventLivingSpawnEventSpecialSpawn.class,
        EventHandler.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
