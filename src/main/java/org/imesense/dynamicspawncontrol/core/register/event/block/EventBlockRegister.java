package org.imesense.dynamicspawncontrol.core.register.event.block;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseregister.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.block.OnEventBlockEventBreakEvent;

@InitLog
public final class EventBlockRegister extends BaseEventRegister
{
    public EventBlockRegister()
    {

    }

    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventBlockEventBreakEvent.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
