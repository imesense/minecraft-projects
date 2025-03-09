package org.imesense.dynamicspawncontrol.core.registerevent.entity;

import org.imesense.dynamicspawncontrol.core.api.AbstractConceptBaseRegister;
import org.imesense.dynamicspawncontrol.core.event.entity.OnEventEntityJoinWorldEvent;

public final class Register extends AbstractConceptBaseRegister
{
    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventEntityJoinWorldEvent.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
