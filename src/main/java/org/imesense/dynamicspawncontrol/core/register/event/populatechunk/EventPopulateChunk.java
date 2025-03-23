package org.imesense.dynamicspawncontrol.core.register.event.populatechunk;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseregister.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.populatechunk.OnEventPopulateChunkEventPre;

@InitLog
public final class EventPopulateChunk extends BaseEventRegister
{
    public EventPopulateChunk()
    {

    }

    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventPopulateChunkEventPre.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
