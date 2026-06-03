package org.imesense.dynamicspawncontrol.core.register.event.block;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.base.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.block.OnEventBlockEventBreakEvent;
import org.imesense.dynamicspawncontrol.core.event.block.OnEventBlockEventHarvestDropsEvent;

@InitLog
public final class EventBlockRegister extends BaseEventRegister
{
    public EventBlockRegister()
    {

    }

    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventBlockEventBreakEvent.class,
        OnEventBlockEventHarvestDropsEvent.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
