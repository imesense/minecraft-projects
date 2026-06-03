package org.imesense.dynamicspawncontrol.core.register.event.world;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.base.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.worldevent.OnEventWorldEventLoad;
import org.imesense.dynamicspawncontrol.core.event.worldevent.OnEventWorldEventPotentialSpawns;

@InitLog
public final class EventWorldRegister extends BaseEventRegister
{
    public EventWorldRegister()
    {

    }

    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventWorldEventPotentialSpawns.class,
        OnEventWorldEventLoad.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
