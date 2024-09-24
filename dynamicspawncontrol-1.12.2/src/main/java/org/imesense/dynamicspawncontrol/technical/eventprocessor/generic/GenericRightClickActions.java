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
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionsSingleEvent;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionsStaticFactoryMouse;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.ResultEvents;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataAccessor;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataGetter;

import java.util.function.Consumer;

/**
 *
 */
public final class GenericRightClickActions extends ListActionsSingleEvent<SignalDataGetter>
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
    public boolean match(PlayerInteractEvent.RightClickBlock event) { return RULE_EVALUATOR.match(event, EVENT_QUERY); }

    /**
     *
     * @param map
     */
    private GenericRightClickActions(AttributeMap<?> map)
    {
        super();

        Log.writeDataToLogFile(0, String.format("Iterator for [%s] number [%d]", GenericRightClickActions.class.getName(), countCreatedMaps++));

        this.RULE_EVALUATOR = new ListActionsBinary<>(map);

        this.addActions(map);

        this.RESULT = RESULT_EVENTS.getResult(map);
    }

    /**
     *
     * @param element
     * @return
     */
    public static GenericRightClickActions parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            AttributeMap<Object> map = ListActionsStaticFactoryMouse.FACTORY.parse(element);

            return new GenericRightClickActions(map);
        }
    }

    /**
     *
     */
    private static final SignalDataAccessor<PlayerInteractEvent.RightClickBlock> EVENT_QUERY = new SignalDataAccessor<PlayerInteractEvent.RightClickBlock>()
    {
        /**
         *
         * @param RightClickBlock
         * @return
         */
        @Override
        public World getWorld(PlayerInteractEvent.RightClickBlock RightClickBlock)
        {
            return RightClickBlock.getWorld();
        }

        /**
         *
         * @param RightClickBlock
         * @return
         */
        @Override
        public BlockPos getPos(PlayerInteractEvent.RightClickBlock RightClickBlock)
        {
            return RightClickBlock.getPos();
        }

        /**
         *
         * @param RightClickBlock
         * @return
         */
        @Override
        public BlockPos getValidBlockPos(PlayerInteractEvent.RightClickBlock RightClickBlock)
        {
            return RightClickBlock.getPos();
        }

        /**
         *
         * @param RightClickBlock
         * @return
         */
        @Override
        public int getY(PlayerInteractEvent.RightClickBlock RightClickBlock)
        {
            return RightClickBlock.getPos().getY();
        }

        /**
         *
         * @param RightClickBlock
         * @return
         */
        @Override
        public Entity getEntity(PlayerInteractEvent.RightClickBlock RightClickBlock)
        {
            return RightClickBlock.getEntityPlayer();
        }

        /**
         *
         * @param RightClickBlock
         * @return
         */
        @Override
        public DamageSource getSource(PlayerInteractEvent.RightClickBlock RightClickBlock)
        {
            return null;
        }

        /**
         *
         * @param RightClickBlock
         * @return
         */
        @Override
        public Entity getAttacker(PlayerInteractEvent.RightClickBlock RightClickBlock)
        {
            return null;
        }

        /**
         *
         * @param RightClickBlock
         * @return
         */
        @Override
        public EntityPlayer getPlayer(PlayerInteractEvent.RightClickBlock RightClickBlock)
        {
            return RightClickBlock.getEntityPlayer();
        }

        /**
         *
         * @param RightClickBlock
         * @return
         */
        @Override
        public ItemStack getItem(PlayerInteractEvent.RightClickBlock RightClickBlock)
        {
            return RightClickBlock.getItemStack();
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
