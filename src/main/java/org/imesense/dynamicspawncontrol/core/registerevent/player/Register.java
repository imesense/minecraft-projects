package org.imesense.dynamicspawncontrol.core.registerevent.player;

import org.imesense.dynamicspawncontrol.core.api.AbstractConceptBaseRegister;
import org.imesense.dynamicspawncontrol.core.event.player.*;

public final class Register extends AbstractConceptBaseRegister
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
