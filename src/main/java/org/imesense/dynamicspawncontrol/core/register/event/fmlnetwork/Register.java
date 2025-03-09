package org.imesense.dynamicspawncontrol.core.register.event.fmlnetwork;

import org.imesense.dynamicspawncontrol.core.baseregister.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.fmlnetwork.*;

public final class Register extends BaseEventRegister
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
