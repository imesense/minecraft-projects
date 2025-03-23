package org.imesense.dynamicspawncontrol.core.register.event.rendergame;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseregister.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.rendergame.*;

@InitLog
public final class EventRenderGame extends BaseEventRegister
{
    public EventRenderGame()
    {

    }

    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventRenderGameOverlayEventPost.class,
        OnEventRenderGameOverlayEventText.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
