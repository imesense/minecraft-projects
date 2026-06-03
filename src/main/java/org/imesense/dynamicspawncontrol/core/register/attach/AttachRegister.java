package org.imesense.dynamicspawncontrol.core.register.attach;

import org.imesense.dynamicspawncontrol.core.base.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.attach.OnEventAttachCapabilitiesEvent;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

public final class AttachRegister extends BaseEventRegister
{
    private static volatile AttachRegister _INSTANCE;

    public static AttachRegister getInstance()
    {
        return CodeGeneric.getInstance(AttachRegister.class);
    }

    public AttachRegister()
    {

    }

    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventAttachCapabilitiesEvent.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
