package org.imesense.dynamicspawncontrol.technical.customlibrary;

import org.imesense.dynamicspawncontrol.technical.attributefactory.Attribute;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMapFactory;

import static org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWords.CommonKeyWorlds.*;

/**
 *
 */
public class ListActionsStaticFactoryMouse
{
    /**
     *
     */
    public static final AttributeMapFactory<Object> FACTORY = new AttributeMapFactory<>();

    /**
     *
     */
    static
    {
        FACTORY
                .attribute(Attribute.create(ID_RULE))
                .attribute(Attribute.create(SEE_SKY))
                .attribute(Attribute.create(WEATHER))
                .attribute(Attribute.create(STRUCTURE))
                .attribute(Attribute.createMulti(BIOMES))
                .attribute(Attribute.createMulti(BIOMES_TYPE))
                .attribute(Attribute.createMulti(DIMENSION))

                .attribute(Attribute.createMulti(HELMET))
                .attribute(Attribute.createMulti(CHEST_PLATE))
                .attribute(Attribute.createMulti(LEGGINGS))
                .attribute(Attribute.createMulti(BOOTS))

                .attribute(Attribute.create(MIN_TIME))
                .attribute(Attribute.create(MAX_TIME))

                .attribute(Attribute.create(MIN_LIGHT))
                .attribute(Attribute.create(MAX_LIGHT))

                .attribute(Attribute.create(MIN_HEIGHT))
                .attribute(Attribute.create(MAX_HEIGHT))

                .attribute(Attribute.create(DIFFICULTY))
                .attribute(Attribute.create(MIN_DIFFICULTY))
                .attribute(Attribute.create(MAX_DIFFICULTY))

                .attribute(Attribute.create(MIN_SPAWN_DIST))
                .attribute(Attribute.create(MAX_SPAWN_DIST))

                .attribute(Attribute.createMulti(BLOCK))
                .attribute(Attribute.create(BLOCK_OFFSET))

                .attribute(Attribute.create(GET_MOON_PHASE))

                .attribute(Attribute.createMulti(HELD_ITEM))
                .attribute(Attribute.createMulti(PLAYER_HELD_ITEM))
                .attribute(Attribute.createMulti(OFF_HAND_ITEM))
                .attribute(Attribute.createMulti(BOTH_HANDS_ITEM))

                .attribute(Attribute.create(RANDOM_KEY_0))
                .attribute(Attribute.create(RANDOM_KEY_1))
                .attribute(Attribute.create(RANDOM_KEY_2))
                .attribute(Attribute.create(RANDOM_KEY_3))
                .attribute(Attribute.create(RANDOM_KEY_4))

                .attribute(Attribute.create(ACTION_RESULT))

                .attribute(Attribute.create(ACTION_MESSAGE))
                .attribute(Attribute.createMulti(ACTION_POTION))
                .attribute(Attribute.createMulti(ACTION_GIVE))
                .attribute(Attribute.createMulti(ACTION_DROP))
                .attribute(Attribute.create(ACTION_COMMAND))
                .attribute(Attribute.create(ACTION_FIRE))
                .attribute(Attribute.create(ACTION_EXPLOSION))
                .attribute(Attribute.create(ACTION_CLEAR))
                .attribute(Attribute.create(ACTION_DAMAGE))
                .attribute(Attribute.create(ACTION_SET_BLOCK))
                .attribute(Attribute.create(ACTION_SET_HELD_ITEM))
                .attribute(Attribute.create(ACTION_SET_HELD_AMOUNT))
        ;
    }
}
