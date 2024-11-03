package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

import com.google.gson.JsonElement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionBinary;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionConsumer;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionStaticFactoryBlock;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.ResultEvents;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataAccessor;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataGetter;

import java.util.function.Consumer;

/**
 *
 */
public final class GenericBlockPlaceAction extends ListActionConsumer<SignalDataGetter>
{
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
    private final ListActionBinary RULE_EVALUATOR;

    /**
     *
     * @return
     */
    public Event.Result getResult() { return this.RESULT; }

    /**
     *
     */
    /* TODO: удалить в будущем */
    private static final ResultEvents RESULT_EVENTS = new ResultEvents();

    /**
     *
     * @param placeEvent
     * @return
     */
    @Deprecated
    public boolean match(BlockEvent.PlaceEvent placeEvent) { return RULE_EVALUATOR.match(placeEvent, EVENT_QUERY); }

    /**
     *
     * @param attributeMap
     */
    private GenericBlockPlaceAction(AttributeMap<?> attributeMap)
    {
        super();

        Log.writeDataToLogFile(0, String.format("Iterator for [%s] number [%d]",
                GenericBlockPlaceAction.class.getName(), countCreatedMaps++));

        this.RULE_EVALUATOR = new ListActionBinary<>(attributeMap);

        this.addActions(attributeMap);

        this.RESULT = RESULT_EVENTS.getResult(attributeMap);
    }

    /**
     *
     * @param jsonElement
     * @return
     */
    public static GenericBlockPlaceAction parse(JsonElement jsonElement)
    {
        if (jsonElement == null)
        {
            return null;
        }
        else
        {
            AttributeMap<Object> attributeMap = ListActionStaticFactoryBlock.FACTORY.parse(jsonElement);

            return new GenericBlockPlaceAction(attributeMap);
        }
    }

    /**
     *
     */
    @Deprecated
    private static final SignalDataAccessor<BlockEvent.PlaceEvent> EVENT_QUERY = new SignalDataAccessor<BlockEvent.PlaceEvent>()
    {
        /**
         *
         * @param placeEvent
         * @return
         */
        @Override
        public int getY(BlockEvent.PlaceEvent placeEvent)
        {
            return placeEvent.getPos().getY();
        }

        /**
         *
         * @param placeEvent
         * @return
         */
        @Override
        public World getWorld(BlockEvent.PlaceEvent placeEvent)
        {
            return placeEvent.getWorld();
        }

        /**
         *
         * @param placeEvent
         * @return
         */
        @Override
        public BlockPos getPos(BlockEvent.PlaceEvent placeEvent)
        {
            return placeEvent.getPos();
        }

        /**
         *
         * @param placeEvent
         * @return
         */
        @Override
        public Entity getEntity(BlockEvent.PlaceEvent placeEvent)
        {
            return placeEvent.getPlayer();
        }

        /**
         *
         * @param placeEvent
         * @return
         */
        @Override
        public ItemStack getItem(BlockEvent.PlaceEvent placeEvent)
        {
            return placeEvent.getItemInHand();
        }

        /**
         *
         * @param placeEvent
         * @return
         */
        @Override
        public Entity getAttacker(BlockEvent.PlaceEvent placeEvent)
        {
            return null;
        }

        /**
         *
         * @param placeEvent
         * @return
         */
        @Override
        public EntityPlayerMP getPlayer(BlockEvent.PlaceEvent placeEvent)
        {
            return (EntityPlayerMP) placeEvent.getPlayer();
        }

        /**
         *
         * @param placeEvent
         * @return
         */
        @Override
        public DamageSource getSource(BlockEvent.PlaceEvent placeEvent)
        {
            return null;
        }

        /**
         *
         * @param placeEvent
         * @return
         */
        @Override
        public BlockPos getValidBlockPos(BlockEvent.PlaceEvent placeEvent)
        {
            return placeEvent.getPos();
        }
    };

    /**
     *
     * @param placeEvent
     */
    @Deprecated
    public void action(BlockEvent.PlaceEvent placeEvent)
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
                return placeEvent.getPlayer();
            }

            /**
             *
             * @return
             */
            @Override
            public EntityPlayerMP getPlayer()
            {
                return (EntityPlayerMP) placeEvent.getPlayer();
            }

            /**
             *
             * @return
             */
            @Override
            public World getWorld()
            {
                return placeEvent.getWorld();
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
                return placeEvent.getPos();
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
