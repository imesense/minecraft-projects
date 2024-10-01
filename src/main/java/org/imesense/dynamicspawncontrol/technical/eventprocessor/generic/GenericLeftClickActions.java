package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

import com.google.gson.JsonElement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionsBinary;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionsConsumer;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionsStaticFactoryMouse;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.ResultEvents;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataAccessor;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataGetter;

import java.util.function.Consumer;

/**
 *
 */
public final class GenericLeftClickActions extends ListActionsConsumer<SignalDataGetter>
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
    private final ListActionsBinary RULE_EVALUATOR;

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
     * @param event
     * @return
     */
    public boolean match(PlayerInteractEvent.LeftClickBlock event) { return RULE_EVALUATOR.match(event, EVENT_QUERY); }

    /**
     *
     * @param map
     */
    private GenericLeftClickActions(AttributeMap<?> map)
    {
        super();

        Log.writeDataToLogFile(0, String.format("Iterator for [%s] number [%d]", GenericLeftClickActions.class.getName(), countCreatedMaps++));

        this.RULE_EVALUATOR = new ListActionsBinary<>(map);

        this.addActions(map);

        this.RESULT = RESULT_EVENTS.getResult(map);
    }

    /**
     *
     * @param element
     * @return
     */
    public static GenericLeftClickActions parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            AttributeMap<Object> map = ListActionsStaticFactoryMouse.FACTORY.parse(element);

            return new GenericLeftClickActions(map);
        }
    }

    /**
     *
     */
    private static final SignalDataAccessor<PlayerInteractEvent.LeftClickBlock> EVENT_QUERY = new SignalDataAccessor<PlayerInteractEvent.LeftClickBlock>()
    {
        /**
         *
         * @param data
         * @return
         */
        @Override
        public World getWorld(PlayerInteractEvent.LeftClickBlock data)
        {
            return data.getWorld();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public BlockPos getPos(PlayerInteractEvent.LeftClickBlock data)
        {
            return data.getPos();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public BlockPos getValidBlockPos(PlayerInteractEvent.LeftClickBlock data)
        {
            return data.getPos();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public int getY(PlayerInteractEvent.LeftClickBlock data)
        {
            return data.getPos().getY();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public Entity getEntity(PlayerInteractEvent.LeftClickBlock data)
        {
            return data.getEntityPlayer();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public DamageSource getSource(PlayerInteractEvent.LeftClickBlock data)
        {
            return null;
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public Entity getAttacker(PlayerInteractEvent.LeftClickBlock data)
        {
            return null;
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public EntityPlayer getPlayer(PlayerInteractEvent.LeftClickBlock data)
        {
            return data.getEntityPlayer();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public ItemStack getItem(PlayerInteractEvent.LeftClickBlock data)
        {
            return data.getItemStack();
        }
    };

    /**
     *
     * @param event
     */
    public void action(PlayerInteractEvent.LeftClickBlock event)
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
                return event.getEntityPlayer();
            }

            /**
             *
             * @return
             */
            @Override
            public EntityPlayer getPlayer()
            {
                return event.getEntityPlayer();
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
