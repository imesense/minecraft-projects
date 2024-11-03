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
public final class GenericBlockBreakAction extends ListActionConsumer<SignalDataGetter>
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
    /* TODO: убрать этот код в будущем */
    private static final ResultEvents RESULT_EVENTS = new ResultEvents();

    /**
     *
     * @param breakEvent
     * @return
     */
    public boolean match(BlockEvent.BreakEvent breakEvent) { return RULE_EVALUATOR.match(breakEvent, EVENT_QUERY); }

    /**
     *
     * @param attributeMap
     */
    private GenericBlockBreakAction(AttributeMap<?> attributeMap)
    {
        super();

        Log.writeDataToLogFile(0, String.format("Iterator for [%s] number [%d]",
                GenericBlockBreakAction.class.getName(), countCreatedMaps++));

        this.RULE_EVALUATOR = new ListActionBinary<>(attributeMap);

        this.addActions(attributeMap);

        this.RESULT = RESULT_EVENTS.getResult(attributeMap);
    }

    /**
     *
     * @param jsonElement
     * @return
     */
    public static GenericBlockBreakAction parse(JsonElement jsonElement)
    {
        if (jsonElement == null)
        {
            return null;
        }
        else
        {
            AttributeMap<Object> attributeMap = ListActionStaticFactoryBlock.FACTORY.parse(jsonElement);

            return new GenericBlockBreakAction(attributeMap);
        }
    }

    /**
     *
     */
    private static final SignalDataAccessor<BlockEvent.BreakEvent> EVENT_QUERY = new SignalDataAccessor<BlockEvent.BreakEvent>()
    {
        /**
         *
         * @param breakEvent
         * @return
         */
        @Override
        public int getY(BlockEvent.BreakEvent breakEvent)
        {
            return breakEvent.getPos().getY();
        }

        /**
         *
         * @param breakEvent
         * @return
         */
        @Override
        public World getWorld(BlockEvent.BreakEvent breakEvent)
        {
            return breakEvent.getWorld();
        }

        /**
         *
         * @param breakEvent
         * @return
         */
        @Override
        public BlockPos getPos(BlockEvent.BreakEvent breakEvent)
        {
            return breakEvent.getPos();
        }

        /**
         *
         * @param breakEvent
         * @return
         */
        @Override
        public Entity getEntity(BlockEvent.BreakEvent breakEvent)
        {
            return breakEvent.getPlayer();
        }

        /**
         *
         * @param breakEvent
         * @return
         */
        @Override
        public ItemStack getItem(BlockEvent.BreakEvent breakEvent)
        {
            return ItemStack.EMPTY;
        }

        /**
         *
         * @param breakEvent
         * @return
         */
        @Override
        public Entity getAttacker(BlockEvent.BreakEvent breakEvent)
        {
            return null;
        }

        /**
         *
         * @param breakEvent
         * @return
         */
        @Override
        public EntityPlayerMP getPlayer(BlockEvent.BreakEvent breakEvent)
        {
            return (EntityPlayerMP) breakEvent.getPlayer();
        }

        /**
         *
         * @param breakEvent
         * @return
         */
        @Override
        public DamageSource getSource(BlockEvent.BreakEvent breakEvent)
        {
            return null;
        }

        /**
         *
         * @param breakEvent
         * @return
         */
        @Override
        public BlockPos getValidBlockPos(BlockEvent.BreakEvent breakEvent)
        {
            return breakEvent.getPos();
        }
    };

    /**
     *
     * @param breakEvent
     */
    public void action(BlockEvent.BreakEvent breakEvent)
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
                return breakEvent.getPlayer();
            }

            /**
             *
             * @return
             */
            @Override
            public EntityPlayerMP getPlayer()
            {
                return (EntityPlayerMP) breakEvent.getPlayer();
            }

            /**
             *
             * @return
             */
            @Override
            public World getWorld()
            {
                return breakEvent.getWorld();
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
                return breakEvent.getPos();
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
