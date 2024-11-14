package org.imesense.dynamicspawncontrol.eventprocessor.generic.keyword;

import org.imesense.dynamicspawncontrol.core.attributefactory.AttributeKey;
import org.imesense.dynamicspawncontrol.core.attributefactory.AttributeType;

/**
 *
 */
public final class MobTaskManager extends CommonKeyWord
{
    public static final AttributeKey<String> ENEMIES_TO = AttributeKey.create(AttributeType.STRING, "enemies_to");
    public static final AttributeKey<String> ENEMY_ID = AttributeKey.create(AttributeType.STRING, "enemy_id");
    public static final AttributeKey<String> PANIC_TO = AttributeKey.create(AttributeType.STRING, "panic_to");
    public static final AttributeKey<String> PANIC_ID = AttributeKey.create(AttributeType.STRING, "panic_id");
    public static final AttributeKey<String> TO_THEM = AttributeKey.create(AttributeType.STRING, "to_them");
    public static final AttributeKey<String> THEM_ID = AttributeKey.create(AttributeType.STRING, "them_id");
}
