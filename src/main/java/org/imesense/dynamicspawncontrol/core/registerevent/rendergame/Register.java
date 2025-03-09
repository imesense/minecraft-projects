package org.imesense.dynamicspawncontrol.core.registerevent.rendergame;

import org.imesense.dynamicspawncontrol.core.api.AbstractConceptBaseRegister;
import org.imesense.dynamicspawncontrol.core.event.rendergame.*;

public final class Register extends AbstractConceptBaseRegister
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
