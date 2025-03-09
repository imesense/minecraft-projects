package org.imesense.dynamicspawncontrol.core.registerevent.world;

import org.imesense.dynamicspawncontrol.core.api.AbstractConceptBaseRegister;
import org.imesense.dynamicspawncontrol.core.event.worldevent.OnEventWorldEventPotentialSpawns;

public final class Register extends AbstractConceptBaseRegister
{
    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventWorldEventPotentialSpawns.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
