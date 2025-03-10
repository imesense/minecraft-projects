package org.imesense.dynamicspawncontrol.core.register.attach;

import org.imesense.dynamicspawncontrol.core.baseregister.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.event.attach.OnEventAttachCapabilitiesEvent;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.capability.EventHandler;

public final class AttachRegister extends BaseEventRegister
{
    private static volatile AttachRegister _INSTANCE;

    public static AttachRegister getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (AttachRegister.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new AttachRegister();
                }
            }
        }

        return _INSTANCE;
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
