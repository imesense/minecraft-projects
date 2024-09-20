package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

import com.google.gson.JsonElement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.imesense.dynamicspawncontrol.technical.attributefactory.Attribute;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMapFactory;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionsBinary;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionsSingleEvent;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.SignalDataAccessor;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.SignalDataGetter;

import java.util.function.Consumer;

import static org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWords.CommonKeyWorlds.*;
import static org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWords.SpawnCondition.*;

/**
 *
 */
public final class GenericSpawnConditions extends ListActionsSingleEvent<SignalDataGetter>
{
    /**
     *
     */
    private static int countCreatedMaps = 0;

    /**
     *
     */
    private final ListActionsBinary RULE_EVALUATOR;

    /**
     *
     */
    private static final AttributeMapFactory<Object> FACTORY = new AttributeMapFactory<>();

    /**
     *
     * @param event
     * @return
     */
    public boolean match(LivingSpawnEvent.CheckSpawn event) { return RULE_EVALUATOR.match(event, EVENT_QUERY); }

    /**
     *
     * @param map
     * @param nameClass
     */
    private GenericSpawnConditions(AttributeMap<?> map, String nameClass)
    {
        super(nameClass);

        Log.writeDataToLogFile(0, String.format("Iterator for [%s] number [%d]", nameClass, countCreatedMaps++));

        this.RULE_EVALUATOR = new ListActionsBinary<>(map, nameClass);

        this.addActions(map);
    }

    /**
     *
     * @param element
     * @return
     */
    public static GenericSpawnConditions parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            AttributeMap<?> map = FACTORY.parse(element);

            return new GenericSpawnConditions(map, "GenericSpawnConditions");
        }
    }

    /**
     *
     */
    private static final SignalDataAccessor<LivingSpawnEvent.CheckSpawn> EVENT_QUERY = new SignalDataAccessor<LivingSpawnEvent.CheckSpawn>()
    {
        /**
         *
         * @param CheckSpawn
         * @return
         */
        @Override
        public World getWorld(LivingSpawnEvent.CheckSpawn CheckSpawn)
        {
            return CheckSpawn.getWorld();
        }

        /**
         *
         * @param CheckSpawn
         * @return
         */
        @Override
        public BlockPos getPos(LivingSpawnEvent.CheckSpawn CheckSpawn)
        {
            return new BlockPos(CheckSpawn.getX(), CheckSpawn.getY(), CheckSpawn.getZ());
        }

        /**
         *
         * @param CheckSpawn
         * @return
         */
        @Override
        public BlockPos getValidBlockPos(LivingSpawnEvent.CheckSpawn CheckSpawn)
        {
            return new BlockPos(CheckSpawn.getX(), CheckSpawn.getY() - 1, CheckSpawn.getZ());
        }

        /**
         *
         * @param CheckSpawn
         * @return
         */
        @Override
        public int getY(LivingSpawnEvent.CheckSpawn CheckSpawn)
        {
            return (int) CheckSpawn.getY();
        }

        /**
         *
         * @param CheckSpawn
         * @return
         */
        @Override
        public Entity getEntity(LivingSpawnEvent.CheckSpawn CheckSpawn)
        {
            return CheckSpawn.getEntity();
        }

        /**
         *
         * @param CheckSpawn
         * @return
         */
        @Override
        public DamageSource getSource(LivingSpawnEvent.CheckSpawn CheckSpawn)
        {
            return null;
        }

        /**
         *
         * @param CheckSpawn
         * @return
         */
        @Override
        public Entity getAttacker(LivingSpawnEvent.CheckSpawn CheckSpawn)
        {
            return null;
        }

        /**
         *
         * @param CheckSpawn
         * @return
         */
        @Override
        public EntityPlayer getPlayer(LivingSpawnEvent.CheckSpawn CheckSpawn)
        {
            return getClosestPlayer(CheckSpawn.getWorld(), new BlockPos(CheckSpawn.getX(), CheckSpawn.getY(), CheckSpawn.getZ()));
        }

        /**
         *
         * @param CheckSpawn
         * @return
         */
        @Override
        public ItemStack getItem(LivingSpawnEvent.CheckSpawn CheckSpawn)
        {
            return ItemStack.EMPTY;
        }
    };

    /**
     *
     * @param world
     * @param blockPos
     * @return
     */
    private static EntityPlayer getClosestPlayer(World world, BlockPos blockPos)
    {
        return world.getClosestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 100, false);
    }

    /**
     *
     */
    static
    {
        FACTORY
                .attribute(Attribute.create(ID_RULE))
                .attribute(Attribute.create(SEE_SKY))
                .attribute(Attribute.create(CAN_SPAWN_HERE))
                .attribute(Attribute.create(NOT_COLLIDING))
                .attribute(Attribute.create(SPAWNER))
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

                .attribute(Attribute.createMulti(MOB))

                .attribute(Attribute.create(ANIMALS))
                .attribute(Attribute.create(MONSTERS))

                .attribute(Attribute.createMulti(HELD_ITEM))
                .attribute(Attribute.createMulti(PLAYER_HELD_ITEM))
                .attribute(Attribute.createMulti(OFF_HAND_ITEM))
                .attribute(Attribute.createMulti(BOTH_HANDS_ITEM))

                .attribute(Attribute.create(RANDOM_KEY_0))
                .attribute(Attribute.create(RANDOM_KEY_1))
                .attribute(Attribute.create(RANDOM_KEY_2))
                .attribute(Attribute.create(RANDOM_KEY_3))
                .attribute(Attribute.create(RANDOM_KEY_4))

                .attribute(Attribute.create(ACTION_MESSAGE))
                .attribute(Attribute.create(ACTION_ANGRY))
                .attribute(Attribute.createMulti(ACTION_HELD_ITEM))
                .attribute(Attribute.createMulti(ACTION_ARMOR_BOOTS))
                .attribute(Attribute.createMulti(ACTION_ARMOR_LEGS))
                .attribute(Attribute.createMulti(ACTION_ARMOR_CHEST))
                .attribute(Attribute.createMulti(ACTION_ARMOR_HELMET))
                .attribute(Attribute.create(ACTION_SET_NBT))
                .attribute(Attribute.create(ACTION_HEALTH_MULTIPLY))
                .attribute(Attribute.create(ACTION_HEALTH_ADD))
                .attribute(Attribute.create(ACTION_SPEED_MULTIPLY))
                .attribute(Attribute.create(ACTION_SPEED_ADD))
                .attribute(Attribute.create(ACTION_DAMAGE_MULTIPLY))
                .attribute(Attribute.create(ACTION_DAMAGE_ADD))
                .attribute(Attribute.create(ACTION_CUSTOM_NAME))
                .attribute(Attribute.createMulti(ACTION_POTION))
        ;
    }

    /**
     *
     * @param event
     */
    public void action(LivingSpawnEvent.CheckSpawn event)
    {
        /**
         *
         */
        SignalDataGetter eventBase = new SignalDataGetter()
        {
            /**
             *
             * @return
             */
            @Override
            public EntityLivingBase getEntityLiving()
            {
                return event.getEntityLiving();
            }

            /**
             *
             * @return
             */
            @Override
            public EntityPlayer getPlayer()
            {
                return null;
            }

            /**
             *
             * @return
             */
            @Override
            public World getWorld()
            {
                return event.getWorld();
            }

            /**
             *
             * @return
             */
            @Override
            public Entity getEntity()
            {
                return event.getEntity();
            }

            /**
             *
             * @return
             */
            @Override
            public BlockPos getPosition()
            {
                return event.getEntityLiving().getPosition();
            }
        };

        /**
         *
         */
        for (Consumer<SignalDataGetter> action : this.ACTIONS)
        {
            action.accept(eventBase);
        }
    }
}
