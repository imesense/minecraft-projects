package org.imesense.dynamicspawncontrol.core.register.event.tickevent;

import org.imesense.dynamicspawncontrol.core.baseregister.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.tickevent.OnEventTickEventClientTickEvent;
import org.imesense.dynamicspawncontrol.core.event.tickevent.OnEventTickEventPlayerTickEvent;
import org.imesense.dynamicspawncontrol.core.event.tickevent.OnEventTickEventWorldTickEvent;

public final class EventTickEventRegister extends BaseEventRegister
{
    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventTickEventWorldTickEvent.class,
        OnEventTickEventClientTickEvent.class,
        OnEventTickEventPlayerTickEvent.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
