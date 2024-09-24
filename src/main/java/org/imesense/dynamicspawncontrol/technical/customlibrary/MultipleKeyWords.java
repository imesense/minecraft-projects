package org.imesense.dynamicspawncontrol.technical.customlibrary;

import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeKey;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeType;

/**
 *
 */
public final class MultipleKeyWords
{
    /*
     * *********************************************************************************************************************
     * Общие ключевые слова
     * *********************************************************************************************************************
     */
    public static class CommonKeyWorlds
    {
        /*
         * Текущий игровой мир
         */
        public static final AttributeKey<Integer> ID_RULE = AttributeKey.create(AttributeType.INTEGER, "id_rule");
        public static final AttributeKey<Boolean> SEE_SKY = AttributeKey.create(AttributeType.BOOLEAN, "see_sky");
        public static final AttributeKey<String> WEATHER = AttributeKey.create(AttributeType.STRING, "weather");
        public static final AttributeKey<String> STRUCTURE = AttributeKey.create(AttributeType.STRING, "structure");
        public static final AttributeKey<String> BIOMES = AttributeKey.create(AttributeType.STRING, "biomes");
        public static final AttributeKey<String> BIOMES_TYPE = AttributeKey.create(AttributeType.STRING, "biomes_type");
        public static final AttributeKey<Integer> DIMENSION = AttributeKey.create(AttributeType.INTEGER, "dimension");

        /*
         * Экипировка игрока
         */
        public static final AttributeKey<String> HELMET = AttributeKey.create(AttributeType.JSON, "helmet");
        public static final AttributeKey<String> CHEST_PLATE = AttributeKey.create(AttributeType.JSON, "chest_plate");
        public static final AttributeKey<String> LEGGINGS = AttributeKey.create(AttributeType.JSON, "leggings");
        public static final AttributeKey<String> BOOTS = AttributeKey.create(AttributeType.JSON, "boots");

        /*
         * Минимальное/максимальное время
         */
        public static final AttributeKey<Integer> MIN_TIME = AttributeKey.create(AttributeType.INTEGER, "min_world_time");
        public static final AttributeKey<Integer> MAX_TIME = AttributeKey.create(AttributeType.INTEGER, "max_world_time");

        /*
         * Минимальный/максимальный свет
         */
        public static final AttributeKey<Integer> MIN_LIGHT = AttributeKey.create(AttributeType.INTEGER, "min_light");
        public static final AttributeKey<Integer> MAX_LIGHT = AttributeKey.create(AttributeType.INTEGER, "max_light");

        /*
         * Минимальная/максимальная высота
         */
        public static final AttributeKey<Integer> MIN_HEIGHT = AttributeKey.create(AttributeType.INTEGER, "min_height");
        public static final AttributeKey<Integer> MAX_HEIGHT = AttributeKey.create(AttributeType.INTEGER, "max_height");

        /*
         * Минимальная/максимальная сложность в мире
         */
        public static final AttributeKey<String> DIFFICULTY = AttributeKey.create(AttributeType.STRING, "difficulty");
        public static final AttributeKey<Float> MIN_DIFFICULTY = AttributeKey.create(AttributeType.FLOAT, "min_difficulty");
        public static final AttributeKey<Float> MAX_DIFFICULTY = AttributeKey.create(AttributeType.FLOAT, "max_difficulty");

        /*
         * Минимальная/максимальная дистанция спавна
         */
        public static final AttributeKey<Float> MIN_SPAWN_DIST = AttributeKey.create(AttributeType.FLOAT, "min_spawn_dist");
        public static final AttributeKey<Float> MAX_SPAWN_DIST = AttributeKey.create(AttributeType.FLOAT, "max_spawn_dist");

        /*
         * Блок и положение блока в мире
         */
        public static final AttributeKey<String> BLOCK = AttributeKey.create(AttributeType.JSON, "block");
        public static final AttributeKey<String> BLOCK_OFFSET = AttributeKey.create(AttributeType.JSON, "block_offset");

        /*
         * Активная фаза луны
         */
        public static final AttributeKey<Integer> GET_MOON_PHASE = AttributeKey.create(AttributeType.INTEGER, "moon_phase");

        /*
         * Определение живого существа
         */
        public static final AttributeKey<String> MOB = AttributeKey.create(AttributeType.STRING, "mob");

        /*
         * Глобальный тип живых существ
         */
        public static final AttributeKey<Boolean> ANIMALS = AttributeKey.create(AttributeType.BOOLEAN, "animals");
        public static final AttributeKey<Boolean> MONSTERS = AttributeKey.create(AttributeType.BOOLEAN, "monsters");

        /*
         * Определение того, что действие выполнил игрок
         */
        public static final AttributeKey<Boolean> PLAYER = AttributeKey.create(AttributeType.BOOLEAN, "player");
        public static final AttributeKey<Boolean> FAKE_PLAYER = AttributeKey.create(AttributeType.BOOLEAN, "fake_player");
        public static final AttributeKey<Boolean> REAL_PLAYER = AttributeKey.create(AttributeType.BOOLEAN, "real_player");
        public static final AttributeKey<String> HELD_ITEM = AttributeKey.create(AttributeType.JSON, "held_item");
        public static final AttributeKey<String> PLAYER_HELD_ITEM = AttributeKey.create(AttributeType.JSON, "player_held_item");
        public static final AttributeKey<String> OFF_HAND_ITEM = AttributeKey.create(AttributeType.JSON, "off_hand_item");
        public static final AttributeKey<String> BOTH_HANDS_ITEM = AttributeKey.create(AttributeType.JSON, "both_hands_item");

        /*
         * Проверка на вид повреждения
         */
        public static final AttributeKey<Boolean> EXPLOSION = AttributeKey.create(AttributeType.BOOLEAN, "explosion");
        public static final AttributeKey<Boolean> PROJECTILE = AttributeKey.create(AttributeType.BOOLEAN, "projectile");
        public static final AttributeKey<Boolean> FIRE = AttributeKey.create(AttributeType.BOOLEAN, "fire");
        public static final AttributeKey<Boolean> MAGIC = AttributeKey.create(AttributeType.BOOLEAN, "magic");
        public static final AttributeKey<String> SOURCE = AttributeKey.create(AttributeType.STRING, "source");

        /*
         * Ключи рандомайзера для определения шансов действия
         */
        public static final AttributeKey<Float> RANDOM_KEY_0 = AttributeKey.create(AttributeType.FLOAT, "random_key_0");
        public static final AttributeKey<Float> RANDOM_KEY_1 = AttributeKey.create(AttributeType.FLOAT, "random_key_1");
        public static final AttributeKey<Float> RANDOM_KEY_2 = AttributeKey.create(AttributeType.FLOAT, "random_key_2");
        public static final AttributeKey<Float> RANDOM_KEY_3 = AttributeKey.create(AttributeType.FLOAT, "random_key_3");
        public static final AttributeKey<Float> RANDOM_KEY_4 = AttributeKey.create(AttributeType.FLOAT, "random_key_4");

        /*
         * Условия на срабатывания блоков кода
         */
        public static final AttributeKey<String> ACTION_RESULT = AttributeKey.create(AttributeType.STRING, "return");

        /*
         * Мировые действия
         */
        public static final AttributeKey<String> ACTION_MESSAGE = AttributeKey.create(AttributeType.STRING, "message");
        public static final AttributeKey<Boolean> ACTION_ANGRY = AttributeKey.create(AttributeType.BOOLEAN, "angry");
        public static final AttributeKey<String> ACTION_HELD_ITEM = AttributeKey.create(AttributeType.JSON, "mob_held_item");
        public static final AttributeKey<String> ACTION_ARMOR_CHEST = AttributeKey.create(AttributeType.JSON, "mob_armor_chest");
        public static final AttributeKey<String> ACTION_ARMOR_HELMET = AttributeKey.create(AttributeType.JSON, "mob_armor_helmet");
        public static final AttributeKey<String> ACTION_ARMOR_LEGS = AttributeKey.create(AttributeType.JSON, "mob_armor_legs");
        public static final AttributeKey<String> ACTION_ARMOR_BOOTS = AttributeKey.create(AttributeType.JSON, "mob_armor_boots");
        public static final AttributeKey<String> ACTION_SET_NBT = AttributeKey.create(AttributeType.JSON, "set_nbt");
        public static final AttributeKey<Float> ACTION_HEALTH_MULTIPLY = AttributeKey.create(AttributeType.FLOAT, "health_multiply");
        public static final AttributeKey<Float> ACTION_HEALTH_ADD = AttributeKey.create(AttributeType.FLOAT, "health_add");
        public static final AttributeKey<Float> ACTION_SPEED_MULTIPLY = AttributeKey.create(AttributeType.FLOAT, "speed_multiply");
        public static final AttributeKey<Float> ACTION_SPEED_ADD = AttributeKey.create(AttributeType.FLOAT, "speed_add");
        public static final AttributeKey<Float> ACTION_DAMAGE_MULTIPLY = AttributeKey.create(AttributeType.FLOAT, "damage_multiply");
        public static final AttributeKey<Float> ACTION_DAMAGE_ADD = AttributeKey.create(AttributeType.FLOAT, "damage_add");
        public static final AttributeKey<String> ACTION_CUSTOM_NAME = AttributeKey.create(AttributeType.STRING, "custom_name");
        public static final AttributeKey<String> ACTION_POTION = AttributeKey.create(AttributeType.STRING, "potion");
        public static final AttributeKey<String> ACTION_GIVE = AttributeKey.create(AttributeType.JSON, "give");
        public static final AttributeKey<String> ACTION_DROP = AttributeKey.create(AttributeType.JSON, "drop");
        public static final AttributeKey<String> ACTION_COMMAND = AttributeKey.create(AttributeType.STRING, "command");
        public static final AttributeKey<String> ACTION_SET_BLOCK = AttributeKey.create(AttributeType.JSON, "set_block");
        public static final AttributeKey<String> ACTION_SET_HELD_ITEM = AttributeKey.create(AttributeType.JSON, "set_held_item");
        public static final AttributeKey<String> ACTION_SET_HELD_AMOUNT = AttributeKey.create(AttributeType.STRING, "set_held_amount");
        public static final AttributeKey<String> ACTION_EXPLOSION = AttributeKey.create(AttributeType.STRING, "explosion");
        public static final AttributeKey<Integer> ACTION_FIRE = AttributeKey.create(AttributeType.INTEGER, "fire");
        public static final AttributeKey<Boolean> ACTION_CLEAR = AttributeKey.create(AttributeType.BOOLEAN, "clear");
        public static final AttributeKey<String> ACTION_DAMAGE = AttributeKey.create(AttributeType.STRING, "damage");
    }

    /*
     * *********************************************************************************************************************
     * Индивидуальные ключевые слова (SpawnConditions.json/ZombieSummonAid.json)
     * *********************************************************************************************************************
     */
    public static class SpawnCondition
    {
        public static final AttributeKey<Boolean> CAN_SPAWN_HERE = AttributeKey.create(AttributeType.BOOLEAN, "can_spawn_here");
        public static final AttributeKey<Boolean> NOT_COLLIDING = AttributeKey.create(AttributeType.BOOLEAN, "not_colliding");
        public static final AttributeKey<Boolean> SPAWNER = AttributeKey.create(AttributeType.BOOLEAN, "spawner");
    }

    /*
     * *********************************************************************************************************************
     * Индивидуальные ключевые слова (MainOverrideSpawn.json)
     * *********************************************************************************************************************
     */
    public static class PotentialSpawn
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

    /*
     * *********************************************************************************************************************
     * Индивидуальные ключевые слова (MobTaskManager.json)
     * *********************************************************************************************************************
     */
    public static class MobTaskManager
    {
        public static final AttributeKey<String> ENEMIES_TO = AttributeKey.create(AttributeType.STRING, "enemies_to");
        public static final AttributeKey<String> ENEMY_ID = AttributeKey.create(AttributeType.STRING, "enemy_id");
        public static final AttributeKey<String> PANIC_TO = AttributeKey.create(AttributeType.STRING, "panic_to");
        public static final AttributeKey<String> PANIC_ID = AttributeKey.create(AttributeType.STRING, "panic_id");
        public static final AttributeKey<String> TO_THEM = AttributeKey.create(AttributeType.STRING, "to_them");
        public static final AttributeKey<String> THEM_ID = AttributeKey.create(AttributeType.STRING, "them_id");
    }

    /*
     * *********************************************************************************************************************
     * Индивидуальные ключевые слова (MainOverrideSpawn.json)
     * *********************************************************************************************************************
     */
    public static class DroopLoot
    {
        public static final AttributeKey<String> ACTION_ITEM = AttributeKey.create(AttributeType.STRING, "item");
        public static final AttributeKey<String> ACTION_REMOVE = AttributeKey.create(AttributeType.JSON, "remove");
        public static final AttributeKey<Boolean> ACTION_REMOVE_ALL = AttributeKey.create(AttributeType.BOOLEAN, "remove_all");
        public static final AttributeKey<String> ACTION_ITEM_NBT = AttributeKey.create(AttributeType.JSON, "item_nbt");
        public static final AttributeKey<String> ACTION_ITEM_COUNT = AttributeKey.create(AttributeType.STRING, "item_count");
    }
}
