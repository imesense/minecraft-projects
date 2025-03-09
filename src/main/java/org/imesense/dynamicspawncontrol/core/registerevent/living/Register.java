package org.imesense.dynamicspawncontrol.core.registerevent.living;

import org.imesense.dynamicspawncontrol.core.api.AbstractConceptBaseRegister;
import org.imesense.dynamicspawncontrol.core.event.living.*;

public final class Register extends AbstractConceptBaseRegister
{
    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventLivingAttackEvent.class,
        OnEventLivingDeathEvent.class,
        OnEventLivingDropsEvent.class,
        OnEventLivingEventLivingUpdateEvent.class,
        OnEventLivingExperienceDropEvent.class,
        OnEventLivingHurtEvent.class,
        OnEventLivingSpawnEventCheckSpawn.class,
        OnEventLivingSpawnEventSpecialSpawn.class
    };

    @Override
    protected Class<?>[] getEventClasses()
    {
        return EVENT_CLASSES;
    }
}
