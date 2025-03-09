package org.imesense.dynamicspawncontrol.core.register.event.player;

import org.imesense.dynamicspawncontrol.core.baseregister.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.player.*;

public final class EventPlayerRegister extends BaseEventRegister
{
    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventPlayerEventPlayerLoggedInEvent.class,
        OnEventPlayerEventPlayerLoggedOutEvent.class,
        OnEventPlayerEventPlayerRespawnEvent.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
