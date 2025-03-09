package org.imesense.dynamicspawncontrol.core.register.event.block;

import org.imesense.dynamicspawncontrol.core.api.AbstractConceptBaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.block.OnEventBlockEventBreakEvent;

public final class Register extends AbstractConceptBaseEventRegister
{
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
