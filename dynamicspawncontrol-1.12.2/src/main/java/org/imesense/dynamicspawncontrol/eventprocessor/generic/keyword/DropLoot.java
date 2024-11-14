package org.imesense.dynamicspawncontrol.eventprocessor.generic.keyword;

import org.imesense.dynamicspawncontrol.core.attributefactory.AttributeKey;
import org.imesense.dynamicspawncontrol.core.attributefactory.AttributeType;

/**
 *
 */
public final class DropLoot extends CommonKeyWord
{
    public static final AttributeKey<String> ACTION_ITEM = AttributeKey.create(AttributeType.STRING, "item");
    public static final AttributeKey<String> ACTION_REMOVE = AttributeKey.create(AttributeType.JSON, "remove");
    public static final AttributeKey<Boolean> ACTION_REMOVE_ALL = AttributeKey.create(AttributeType.BOOLEAN, "remove_all");
    public static final AttributeKey<String> ACTION_ITEM_NBT = AttributeKey.create(AttributeType.JSON, "item_nbt");
    public static final AttributeKey<String> ACTION_ITEM_COUNT = AttributeKey.create(AttributeType.STRING, "item_count");
}
