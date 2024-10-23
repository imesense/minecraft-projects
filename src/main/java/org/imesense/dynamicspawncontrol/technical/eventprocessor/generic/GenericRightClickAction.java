package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

import com.google.gson.JsonElement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionBinary;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionConsumer;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionStaticFactoryMouse;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.ResultEvents;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataAccessor;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataGetter;

import java.util.function.Consumer;

/**
 *
 */
public final class GenericRightClickAction extends ListActionConsumer<SignalDataGetter>
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
     * @param event
     * @return
     */
    public boolean match(PlayerInteractEvent.RightClickBlock event) { return RULE_EVALUATOR.match(event, EVENT_QUERY); }

    /**
     *
     * @param map
     */
    private GenericRightClickAction(AttributeMap<?> map)
    {
        super();

        Log.writeDataToLogFile(0, String.format("Iterator for [%s] number [%d]", GenericRightClickAction.class.getName(), countCreatedMaps++));

        this.RULE_EVALUATOR = new ListActionBinary<>(map);

        this.addActions(map);

        this.RESULT = RESULT_EVENTS.getResult(map);
    }

    /**
     *
     * @param element
     * @return
     */
    public static GenericRightClickAction parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            AttributeMap<Object> map = ListActionStaticFactoryMouse.FACTORY.parse(element);

            return new GenericRightClickAction(map);
        }
    }

    /**
     *
     */
    private static final SignalDataAccessor<PlayerInteractEvent.RightClickBlock> EVENT_QUERY = new SignalDataAccessor<PlayerInteractEvent.RightClickBlock>()
    {
        /**
         *
         * @param data
         * @return
         */
        @Override
        public World getWorld(PlayerInteractEvent.RightClickBlock data)
        {
            return data.getWorld();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public BlockPos getPos(PlayerInteractEvent.RightClickBlock data)
        {
            return data.getPos();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public BlockPos getValidBlockPos(PlayerInteractEvent.RightClickBlock data)
        {
            return data.getPos();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public int getY(PlayerInteractEvent.RightClickBlock data)
        {
            return data.getPos().getY();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public Entity getEntity(PlayerInteractEvent.RightClickBlock data)
        {
            return data.getEntityPlayer();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public DamageSource getSource(PlayerInteractEvent.RightClickBlock data)
        {
            return null;
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public Entity getAttacker(PlayerInteractEvent.RightClickBlock data)
        {
            return null;
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public EntityPlayerMP getPlayer(PlayerInteractEvent.RightClickBlock data)
        {
            return (EntityPlayerMP) data.getEntityPlayer();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public ItemStack getItem(PlayerInteractEvent.RightClickBlock data)
        {
            return data.getItemStack();
        }
    };

    /**
     * @param event
     */
    public void action(PlayerInteractEvent.RightClickBlock event)
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
            public EntityPlayerMP getPlayer()
            {
                return (EntityPlayerMP) event.getEntityPlayer();
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
