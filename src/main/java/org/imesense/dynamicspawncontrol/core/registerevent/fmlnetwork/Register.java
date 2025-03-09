package org.imesense.dynamicspawncontrol.core.registerevent.fmlnetwork;

import org.imesense.dynamicspawncontrol.core.api.AbstractConceptBaseRegister;
import org.imesense.dynamicspawncontrol.core.event.fmlnetwork.*;

public final class Register extends AbstractConceptBaseRegister
{
    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventFMLNetworkEventClientConnectedToServerEvent.class,
        OnEventFMLNetworkEventClientDisconnectionFromServerEvent.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
