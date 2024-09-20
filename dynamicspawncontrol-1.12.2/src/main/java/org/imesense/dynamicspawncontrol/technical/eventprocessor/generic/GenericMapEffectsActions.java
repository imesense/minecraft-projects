package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Since;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.technical.attributefactory.Attribute;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMapFactory;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionsBinary;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionsSingleEvent;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.customlibrary.SingleKeyWords;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.SignalDataAccessor;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.SignalDataGetter;

import java.util.function.Consumer;

import static org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWords.CommonKeyWorlds.*;

/**
 *
 */
public final class GenericMapEffectsActions extends ListActionsSingleEvent<SignalDataGetter>
{
    /**
     *
     */
    private final int TIMEOUT;

    /**
     *
     */
    private static int countCreatedMaps = 0;

    /**
     *
     * @return
     */
    public int getTimeout() { return this.TIMEOUT; }

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
    public boolean match(TickEvent.PlayerTickEvent event) { return RULE_EVALUATOR.match(event, EVENT_QUERY); }

    /**
     *
     * @param map
     * @param timeout
     * @param nameClass
     */
    private GenericMapEffectsActions(AttributeMap<?> map, int timeout, String nameClass)
    {
        super(nameClass);

        Log.writeDataToLogFile(Log.TypeLog[0], String.format("Iterator for [%s] number [%d]", nameClass, countCreatedMaps++));

        this.RULE_EVALUATOR = new ListActionsBinary<>(map, nameClass);

        this.addActions(map);

        this.TIMEOUT = timeout > 0 ? timeout : 1;
    }

    /**
     *
     * @param element
     * @return
     */
    public static GenericMapEffectsActions parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            AttributeMap<?> map = FACTORY.parse(element);

            int localTimeOut = element.getAsJsonObject().has(SingleKeyWords.EVENT_EFFECTS.KEYWORD_TIMEOUT)
                    ? element.getAsJsonObject().get(SingleKeyWords.EVENT_EFFECTS.KEYWORD_TIMEOUT).getAsInt() : 20;

            return new GenericMapEffectsActions(map, localTimeOut, "GenericMapEffectsActions");
        }
    }

    /**
     *
     */
    private static final SignalDataAccessor<TickEvent.PlayerTickEvent> EVENT_QUERY = new SignalDataAccessor<TickEvent.PlayerTickEvent>()
    {
        /**
         *
         * @param PlayerTickEvent
         * @return
         */
        @Override
        public World getWorld(TickEvent.PlayerTickEvent PlayerTickEvent)
        {
            return PlayerTickEvent.player.getEntityWorld();
        }

        /**
         *
         * @param PlayerTickEvent
         * @return
         */
        @Override
        public BlockPos getPos(TickEvent.PlayerTickEvent PlayerTickEvent)
        {
            return PlayerTickEvent.player.getPosition();
        }

        /**
         *
         * @param PlayerTickEvent
         * @return
         */
        @Override
        public BlockPos getValidBlockPos(TickEvent.PlayerTickEvent PlayerTickEvent)
        {
            return PlayerTickEvent.player.getPosition().down();
        }

        /**
         *
         * @param PlayerTickEvent
         * @return
         */
        @Override
        public int getY(TickEvent.PlayerTickEvent PlayerTickEvent)
        {
            return PlayerTickEvent.player.getPosition().getY();
        }

        /**
         *
         * @param PlayerTickEvent
         * @return
         */
        @Override
        public Entity getEntity(TickEvent.PlayerTickEvent PlayerTickEvent)
        {
            return PlayerTickEvent.player;
        }

        /**
         *
         * @param _PlayerTickEvent
         * @return
         */
        @Override
        public DamageSource getSource(TickEvent.PlayerTickEvent _PlayerTickEvent)
        {
            return null;
        }

        /**
         *
         * @param _PlayerTickEvent
         * @return
         */
        @Override
        public Entity getAttacker(TickEvent.PlayerTickEvent _PlayerTickEvent)
        {
            return null;
        }

        /**
         *
         * @param PlayerTickEvent
         * @return
         */
        @Override
        public EntityPlayer getPlayer(TickEvent.PlayerTickEvent PlayerTickEvent)
        {
            return PlayerTickEvent.player;
        }

        /**
         *
         * @param _PlayerTickEvent
         * @return
         */
        @Override
        public ItemStack getItem(TickEvent.PlayerTickEvent _PlayerTickEvent)
        {
            return ItemStack.EMPTY;
        }
    };

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

    /**
     *
     * @param event
     */
    public void action(TickEvent.PlayerTickEvent event)
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
                return event.player;
            }

            /**
             *
             * @return
             */
            @Override
            public EntityPlayer getPlayer()
            {
                return event.player;
            }

            /**
             *
             * @return
             */
            @Override
            public World getWorld()
            {
                return event.player.getEntityWorld();
            }

            /**
             *
             * @return
             */
            @Override
            public Entity getEntity()
            {
                return null;
            }

            /**
             *
             * @return
             */
            @Override
            public BlockPos getPosition()
            {
                return event.player.getPosition();
            }
        };

        /**
         *
         */
        for (Consumer<SignalDataGetter> action : actions)
        {
            action.accept(eventBase);
        }
    }
}
