package org.imesense.dynamicspawncontrol.core.register.event.player;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.base.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.player.*;

@InitLog
public final class EventPlayerRegister extends BaseEventRegister
{
    public EventPlayerRegister()
    {

    }

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
