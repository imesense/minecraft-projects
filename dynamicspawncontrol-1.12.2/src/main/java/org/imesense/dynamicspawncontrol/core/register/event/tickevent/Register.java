package org.imesense.dynamicspawncontrol.core.register.event.tickevent;

import org.imesense.dynamicspawncontrol.core.api.AbstractConceptBaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.tickevent.OnEventTickEventWorldTickEvent;

public final class Register extends AbstractConceptBaseEventRegister
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
