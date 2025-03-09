package org.imesense.dynamicspawncontrol.core.register.event.rendergame;

import org.imesense.dynamicspawncontrol.core.api.AbstractConceptBaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.rendergame.*;

public final class Register extends AbstractConceptBaseEventRegister
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
