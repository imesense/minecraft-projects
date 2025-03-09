package org.imesense.dynamicspawncontrol.core.register.event.rendergame;

import org.imesense.dynamicspawncontrol.core.baseregister.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.rendergame.*;

public final class EventRenderGame extends BaseEventRegister
{
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
