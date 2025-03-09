package org.imesense.dynamicspawncontrol.core.register.event.populatechunk;

import org.imesense.dynamicspawncontrol.core.api.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.populatechunk.OnEventPopulateChunkEventPre;

public final class Register extends BaseEventRegister
{
    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventPopulateChunkEventPre.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
