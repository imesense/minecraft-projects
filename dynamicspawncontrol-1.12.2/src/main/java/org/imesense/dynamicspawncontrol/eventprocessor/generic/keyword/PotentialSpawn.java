package org.imesense.dynamicspawncontrol.eventprocessor.generic.keyword;

import org.imesense.dynamicspawncontrol.technical.attributefactory.*;

public final class PotentialSpawn extends CommonKeyWord
{
    public static AttributeKey<?> MOB_STRUCT = AttributeKey.create(AttributeType.MAP, "struct");
    public static AttributeKey<String> MOB_NAME = AttributeKey.create(AttributeType.STRING, "mob");
    public static AttributeKey<Integer> MOB_WEIGHT = AttributeKey.create(AttributeType.INTEGER, "frequency");
    public static AttributeKey<Integer> MOB_MAX_HEIGHT = AttributeKey.create(AttributeType.INTEGER, "max_height");
    public static AttributeKey<Integer> MOB_MIN_HEIGHT = AttributeKey.create(AttributeType.INTEGER, "min_height");
    public static AttributeKey<Float> MOB_SPAWN_CHANCE = AttributeKey.create(AttributeType.FLOAT, "spawn_chance");
    public static AttributeKey<Integer> MOB_GROUP_COUNT_MIN = AttributeKey.create(AttributeType.INTEGER, "group_count_min");
    public static AttributeKey<Integer> MOB_GROUP_COUNT_MAX = AttributeKey.create(AttributeType.INTEGER, "group_count_max");
}
