package org.imesense.dynamicspawncontrol.core.register.event.entity;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseregister.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.entity.OnEventEntityJoinWorldEvent;
import org.imesense.dynamicspawncontrol.core.event.entity.OnEventEntityViewRenderEventFogColors;
import org.imesense.dynamicspawncontrol.core.event.entity.OnEventEntityViewRenderEventRenderFogEvent;

@InitLog
public final class EventEntityRegister extends BaseEventRegister
{
    public EventEntityRegister()
    {

    }

    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventEntityJoinWorldEvent.class,
        OnEventEntityViewRenderEventFogColors.class,
        OnEventEntityViewRenderEventRenderFogEvent.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
