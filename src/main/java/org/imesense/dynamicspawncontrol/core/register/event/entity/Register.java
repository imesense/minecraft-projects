package org.imesense.dynamicspawncontrol.core.register.event.entity;

import org.imesense.dynamicspawncontrol.core.api.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.entity.OnEventEntityJoinWorldEvent;

public final class Register extends BaseEventRegister
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
