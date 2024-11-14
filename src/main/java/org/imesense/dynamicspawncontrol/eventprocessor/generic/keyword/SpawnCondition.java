package org.imesense.dynamicspawncontrol.eventprocessor.generic.keyword;

import org.imesense.dynamicspawncontrol.core.attributefactory.AttributeKey;
import org.imesense.dynamicspawncontrol.core.attributefactory.AttributeType;

/**
 *
 */
public final class SpawnCondition extends CommonKeyWord
{
    public static final AttributeKey<Boolean> CAN_SPAWN_HERE = AttributeKey.create(AttributeType.BOOLEAN, "can_spawn_here");
    public static final AttributeKey<Boolean> NOT_COLLIDING = AttributeKey.create(AttributeType.BOOLEAN, "not_colliding");
    public static final AttributeKey<Boolean> SPAWNER = AttributeKey.create(AttributeType.BOOLEAN, "spawner");
}
