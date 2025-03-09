package org.imesense.dynamicspawncontrol.core.register.event.world;

import org.imesense.dynamicspawncontrol.core.baseregister.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.worldevent.OnEventWorldEventPotentialSpawns;

public final class EventWorldRegister extends BaseEventRegister
{
    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventWorldEventPotentialSpawns.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
