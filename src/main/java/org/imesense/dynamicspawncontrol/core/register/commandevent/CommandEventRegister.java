package org.imesense.dynamicspawncontrol.core.register.commandevent;

import org.imesense.dynamicspawncontrol.core.base.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.command.OnEventCommandEvent;

public final class CommandEventRegister extends BaseEventRegister
{
    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventCommandEvent.class
    };

    public CommandEventRegister()
    {

    }

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
