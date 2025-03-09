package org.imesense.dynamicspawncontrol.core.register.event.tickevent;

import org.imesense.dynamicspawncontrol.core.api.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.tickevent.OnEventTickEventWorldTickEvent;

public final class Register extends BaseEventRegister
{
    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventTickEventWorldTickEvent.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
