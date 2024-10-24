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
     * @param event
     * @return
     */
    public boolean match(BlockEvent.BreakEvent event) { return RULE_EVALUATOR.match(event, EVENT_QUERY); }

    /**
     *
     * @param map
     */
    private GenericBlockBreakAction(AttributeMap<?> map)
    {
        super();

        Log.writeDataToLogFile(0, String.format("Iterator for [%s] number [%d]", GenericBlockBreakAction.class.getName(), countCreatedMaps++));

        this.RULE_EVALUATOR = new ListActionBinary<>(map);

        this.addActions(map);

        this.RESULT = RESULT_EVENTS.getResult(map);
    }

    /**
     *
     * @param element
     * @return
     */
    public static GenericBlockBreakAction parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            AttributeMap<Object> map = ListActionStaticFactoryBlock.FACTORY.parse(element);

            return new GenericBlockBreakAction(map);
        }
    }

    /**
     *
     */
    private static final SignalDataAccessor<BlockEvent.BreakEvent> EVENT_QUERY = new SignalDataAccessor<BlockEvent.BreakEvent>()
    {
        /**
         *
         * @param data
         * @return
         */
        @Override
        public int getY(BlockEvent.BreakEvent data)
        {
            return data.getPos().getY();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public World getWorld(BlockEvent.BreakEvent data)
        {
            return data.getWorld();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public BlockPos getPos(BlockEvent.BreakEvent data)
        {
            return data.getPos();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public Entity getEntity(BlockEvent.BreakEvent data)
        {
            return data.getPlayer();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public ItemStack getItem(BlockEvent.BreakEvent data)
        {
            return ItemStack.EMPTY;
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public Entity getAttacker(BlockEvent.BreakEvent data)
        {
            return null;
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public EntityPlayerMP getPlayer(BlockEvent.BreakEvent data)
        {
            return (EntityPlayerMP) data.getPlayer();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public DamageSource getSource(BlockEvent.BreakEvent data)
        {
            return null;
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public BlockPos getValidBlockPos(BlockEvent.BreakEvent data)
        {
            return data.getPos();
        }
    };

    /**
     *
     * @param event
     */
    public void action(BlockEvent.BreakEvent event)
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
                return event.getPlayer();
            }

            /**
             *
             * @return
             */
            @Override
            public EntityPlayerMP getPlayer()
            {
                return (EntityPlayerMP) event.getPlayer();
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
                return null;
            }

            /**
             *
             * @return
             */
            @Override
            public BlockPos getPosition()
            {
                return event.getPos();
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