package org.imesense.dynamicspawncontrol.core.register.event.entity;

import org.imesense.dynamicspawncontrol.core.baseregister.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.entity.OnEventEntityJoinWorldEvent;

public final class EventEntityRegister extends BaseEventRegister
{
    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventEntityJoinWorldEvent.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
