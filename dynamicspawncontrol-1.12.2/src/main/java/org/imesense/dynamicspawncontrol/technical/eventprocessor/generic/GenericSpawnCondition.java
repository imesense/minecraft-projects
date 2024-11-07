package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

import com.google.gson.JsonElement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.imesense.dynamicspawncontrol.technical.attributefactory.Attribute;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMapFactory;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionBinary;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionConsumer;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.api.SignalDataAccessor;
import org.imesense.dynamicspawncontrol.core.api.SignalDataGetter;

import java.util.function.Consumer;

import static org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWord.CommonKeyWorlds.*;
import static org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWord.SpawnCondition.*;

/**
 *
 */
public final class GenericSpawnCondition extends ListActionConsumer<SignalDataGetter>
{
    /**
     *
     */
    private static int countCreatedMaps = 0;

    /**
     *
     */
    private final ListActionBinary RULE_EVALUATOR;

    /**
     *
     */
    private static final AttributeMapFactory<Object> FACTORY = new AttributeMapFactory<>();

    /**
     *
     * @param checkSpawn
     * @return
     */
    public boolean match(LivingSpawnEvent.CheckSpawn checkSpawn) { return RULE_EVALUATOR.match(checkSpawn, EVENT_QUERY); }

    /**
     *
     * @param attributeMap
     */
    private GenericSpawnCondition(AttributeMap<?> attributeMap)
    {
        super();

        Log.writeDataToLogFile(0, String.format("Iterator for [%s] number [%d]",
                GenericSpawnCondition.class.getName(), countCreatedMaps++));

        this.RULE_EVALUATOR = new ListActionBinary<>(attributeMap);

        this.addActions(attributeMap);
    }

    /**
     *
     * @param jsonElement
     * @return
     */
    public static GenericSpawnCondition parse(JsonElement jsonElement)
    {
        if (jsonElement == null)
        {
            return null;
        }
        else
        {
            AttributeMap<?> attributeMap = FACTORY.parse(jsonElement);

            return new GenericSpawnCondition(attributeMap);
        }
    }

    /**
     *
     */
    private static final SignalDataAccessor<LivingSpawnEvent.CheckSpawn> EVENT_QUERY = new SignalDataAccessor<LivingSpawnEvent.CheckSpawn>()
    {
        /**
         *
         * @param checkSpawn
         * @return
         */
        @Override
        public World getWorld(LivingSpawnEvent.CheckSpawn checkSpawn)
        {
            return checkSpawn.getWorld();
        }

        /**
         *
         * @param checkSpawn
         * @return
         */
        @Override
        public BlockPos getPos(LivingSpawnEvent.CheckSpawn checkSpawn)
        {
            return new BlockPos(checkSpawn.getX(), checkSpawn.getY(), checkSpawn.getZ());
        }

        /**
         *
         * @param checkSpawn
         * @return
         */
        @Override
        public BlockPos getValidBlockPos(LivingSpawnEvent.CheckSpawn checkSpawn)
        {
            return new BlockPos(checkSpawn.getX(), checkSpawn.getY() - 1.00, checkSpawn.getZ());
        }

        /**
         *
         * @param checkSpawn
         * @return
         */
        @Override
        public int getY(LivingSpawnEvent.CheckSpawn checkSpawn)
        {
            return (int) checkSpawn.getY();
        }

        /**
         *
         * @param checkSpawn
         * @return
         */
        @Override
        public Entity getEntity(LivingSpawnEvent.CheckSpawn checkSpawn)
        {
            return checkSpawn.getEntity();
        }

        /**
         *
         * @param checkSpawn
         * @return
         */
        @Override
        public DamageSource getSource(LivingSpawnEvent.CheckSpawn checkSpawn)
        {
            return null;
        }

        /**
         *
         * @param checkSpawn
         * @return
         */
        @Override
        public Entity getAttacker(LivingSpawnEvent.CheckSpawn checkSpawn)
        {
            return null;
        }

        /**
         *
         * @param checkSpawn
         * @return
         */
        @Override
        public EntityPlayerMP getPlayer(LivingSpawnEvent.CheckSpawn checkSpawn)
        {
            return getClosestPlayer(checkSpawn.getWorld(),
                    new BlockPos(checkSpawn.getX(), checkSpawn.getY(), checkSpawn.getZ()));
        }

        /**
         *
         * @param checkSpawn
         * @return
         */
        @Override
        public ItemStack getItem(LivingSpawnEvent.CheckSpawn checkSpawn)
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
    private static EntityPlayerMP getClosestPlayer(World world, BlockPos blockPos)
    {
        return (EntityPlayerMP) world.getClosestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 100.00, false);
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
                .attribute(Attribute.create(GET_CURRENT_GAME_DAY_EQUAL))
                .attribute(Attribute.create(GET_CURRENT_GAME_DAY_GREATER))
                .attribute(Attribute.create(GET_CURRENT_GAME_DAY_LESS))
                .attribute(Attribute.create(GET_CURRENT_GAME_DAY_GREATER_OR_EQUAL))
                .attribute(Attribute.create(GET_CURRENT_GAME_DAY_LESS_OR_EQUAL))
                .attribute(Attribute.create(GET_CURRENT_GAME_DAY_INTERVAL))

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
     * @param checkSpawn
     */
    public void action(LivingSpawnEvent.CheckSpawn checkSpawn)
    {
        /**
         *
         */
        SignalDataGetter signalDataGetter = new SignalDataGetter()
        {
            /**
             *
             * @return
             */
            @Override
            public EntityLivingBase getEntityLiving()
            {
                return checkSpawn.getEntityLiving();
            }

            /**
             *
             * @return
             */
            @Override
            public EntityPlayerMP getPlayer()
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
                return checkSpawn.getWorld();
            }

            /**
             *
             * @return
             */
            @Override
            public Entity getEntity()
            {
                return checkSpawn.getEntity();
            }

            /**
             *
             * @return
             */
            @Override
            public BlockPos getPosition()
            {
                return checkSpawn.getEntityLiving().getPosition();
            }
        };

        /**
         *
         */
        for (Consumer<SignalDataGetter> signalDataGetterConsumer : this.ACTIONS)
        {
            signalDataGetterConsumer.accept(signalDataGetter);
        }
    }
}
