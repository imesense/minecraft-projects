package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

import com.google.gson.JsonElement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.technical.attributefactory.Attribute;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMapFactory;
import org.imesense.dynamicspawncontrol.technical.customlibrary.*;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.ResultEvents;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataAccessor;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataGetter;

import static org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWords.CommonKeyWorlds.*;

/**
 *
 */
public final class GenericExperience extends ListActionsConsumer<SignalDataGetter>
{
    /**
     *
     */
    private final int XP;

    /**
     *
     */
    private final float MULTI_XP;

    /**
     *
     */
    private final float ADDING_XP;

    /**
     *
     */
    private final Event.Result RESULT;

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
     * @return
     */
    public Event.Result getResult() { return this.RESULT; }

    /**
     *
     */
    /* TODO: удалить этот код в будущем */
    private static final ResultEvents RESULT_EVENTS = new ResultEvents();

    /**
     *
     */
    private static final AttributeMapFactory<Object> FACTORY = new AttributeMapFactory<>();

    /**
     *
     * @param event
     * @return
     */
    public boolean match(LivingExperienceDropEvent event) { return RULE_EVALUATOR.match(event, EVENT_QUERY); }

    /**
     *
     * @param map
     * @param xp
     * @param multiXp
     * @param addingXp
     */
    private GenericExperience(AttributeMap<?> map, int xp, float multiXp, float addingXp)
    {
        super();

        Log.writeDataToLogFile(0, String.format("Iterator for [%s] number [%d]", GenericExperience.class.getName(), countCreatedMaps++));

        this.RULE_EVALUATOR = new ListActionsBinary<>(map);

        this.addActions(map);

        this.RESULT = RESULT_EVENTS.getResult(map);

        this.XP = xp;
        this.MULTI_XP = multiXp;
        this.ADDING_XP = addingXp;
    }

    /**
     *
     * @param element
     * @return
     */
    public static GenericExperience parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            AttributeMap<?> map = FACTORY.parse(element);

            int localSetXp = JsonServices.getValueFromJson(
                    element.getAsJsonObject(),
                    SingleKeyWords.DROP_ALL_EXPERIENCE.SET_XP,
                    0,
                    (_element, defaultValue) ->
                            _element.getAsJsonPrimitive().isNumber() ? _element.getAsInt() : defaultValue
            );

            float localMultiXp = JsonServices.getValueFromJson(
                    element.getAsJsonObject(),
                    SingleKeyWords.DROP_ALL_EXPERIENCE.MULTI_XP,
                    0.f,
                    (_element, defaultValue) ->
                            _element.getAsJsonPrimitive().isNumber() ? _element.getAsFloat() : defaultValue
            );

            float localAddXp = JsonServices.getValueFromJson(
                    element.getAsJsonObject(),
                    SingleKeyWords.DROP_ALL_EXPERIENCE.ADD_XP,
                    0.f,
                    (_element, defaultValue) ->
                            _element.getAsJsonPrimitive().isNumber() ? _element.getAsFloat() : defaultValue
            );

            return new GenericExperience(map, localSetXp, localMultiXp, localAddXp);
        }
    }

    /**
     *
     */
    private static final SignalDataAccessor<LivingExperienceDropEvent> EVENT_QUERY = new SignalDataAccessor<LivingExperienceDropEvent>()
    {
        /**
         *
         * @param LivingExperienceDropEvent
         * @return
         */
        @Override
        public World getWorld(LivingExperienceDropEvent LivingExperienceDropEvent)
        {
            return LivingExperienceDropEvent.getEntity().getEntityWorld();
        }

        /**
         *
         * @param LivingExperienceDropEvent
         * @return
         */
        @Override
        public BlockPos getPos(LivingExperienceDropEvent LivingExperienceDropEvent)
        {
            return LivingExperienceDropEvent.getEntity().getPosition();
        }

        /**
         *
         * @param LivingExperienceDropEvent
         * @return
         */
        @Override
        public BlockPos getValidBlockPos(LivingExperienceDropEvent LivingExperienceDropEvent)
        {
            return LivingExperienceDropEvent.getEntity().getPosition().down();
        }

        /**
         *
         * @param LivingExperienceDropEvent
         * @return
         */
        @Override
        public int getY(LivingExperienceDropEvent LivingExperienceDropEvent)
        {
            return LivingExperienceDropEvent.getEntity().getPosition().getY();
        }

        /**
         *
         * @param LivingExperienceDropEvent
         * @return
         */
        @Override
        public Entity getEntity(LivingExperienceDropEvent LivingExperienceDropEvent)
        {
            return LivingExperienceDropEvent.getEntity();
        }

        /**
         *
         * @param LivingExperienceDropEvent
         * @return
         */
        @Override
        public DamageSource getSource(LivingExperienceDropEvent LivingExperienceDropEvent)
        {
            return null;
        }

        /**
         *
         * @param LivingExperienceDropEvent
         * @return
         */
        @Override
        public Entity getAttacker(LivingExperienceDropEvent LivingExperienceDropEvent)
        {
            return LivingExperienceDropEvent.getAttackingPlayer();
        }

        /**
         *
         * @param LivingExperienceDropEvent
         * @return
         */
        @Override
        public EntityPlayer getPlayer(LivingExperienceDropEvent LivingExperienceDropEvent)
        {
            return LivingExperienceDropEvent.getAttackingPlayer();
        }

        /**
         *
         * @param LivingExperienceDropEvent
         * @return
         */
        @Override
        public ItemStack getItem(LivingExperienceDropEvent LivingExperienceDropEvent)
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

                .attribute(Attribute.create(PLAYER))
                .attribute(Attribute.create(FAKE_PLAYER))
                .attribute(Attribute.create(REAL_PLAYER))
                .attribute(Attribute.createMulti(HELD_ITEM))
                .attribute(Attribute.createMulti(PLAYER_HELD_ITEM))
                .attribute(Attribute.createMulti(OFF_HAND_ITEM))
                .attribute(Attribute.createMulti(BOTH_HANDS_ITEM))

                .attribute(Attribute.create(PROJECTILE))
                .attribute(Attribute.create(EXPLOSION))
                .attribute(Attribute.create(FIRE))
                .attribute(Attribute.create(MAGIC))
                .attribute(Attribute.createMulti(SOURCE))

                .attribute(Attribute.create(RANDOM_KEY_0))
                .attribute(Attribute.create(RANDOM_KEY_1))
                .attribute(Attribute.create(RANDOM_KEY_2))
                .attribute(Attribute.create(RANDOM_KEY_3))
                .attribute(Attribute.create(RANDOM_KEY_4))

                .attribute(Attribute.create(ACTION_RESULT))
        ;
    }

    /**
     *
     * @param xpIn
     * @return
     */
    public int modifyXp(int xpIn)
    {
        if (this.XP != 0)
        {
            xpIn = this.XP;
        }

        return (int) (xpIn * this.MULTI_XP + this.ADDING_XP);
    }
}
