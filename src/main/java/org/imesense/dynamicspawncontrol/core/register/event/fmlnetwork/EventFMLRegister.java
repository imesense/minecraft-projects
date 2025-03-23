package org.imesense.dynamicspawncontrol.core.register.event.fmlnetwork;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseregister.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.fmlnetwork.*;

@InitLog
public final class EventFMLRegister extends BaseEventRegister
{
    public EventFMLRegister()
    {

    }

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
