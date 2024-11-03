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
     * @param rightClickBlock
     * @return
     */
    public boolean match(PlayerInteractEvent.RightClickBlock rightClickBlock) { return RULE_EVALUATOR.match(rightClickBlock, EVENT_QUERY); }

    /**
     *
     * @param attributeMap
     */
    private GenericRightClickAction(AttributeMap<?> attributeMap)
    {
        super();

        Log.writeDataToLogFile(0, String.format("Iterator for [%s] number [%d]",
                GenericRightClickAction.class.getName(), countCreatedMaps++));

        this.RULE_EVALUATOR = new ListActionBinary<>(attributeMap);

        this.addActions(attributeMap);

        this.RESULT = RESULT_EVENTS.getResult(attributeMap);
    }

    /**
     *
     * @param jsonElement
     * @return
     */
    public static GenericRightClickAction parse(JsonElement jsonElement)
    {
        if (jsonElement == null)
        {
            return null;
        }
        else
        {
            AttributeMap<Object> attributeMap = ListActionStaticFactoryMouse.FACTORY.parse(jsonElement);

            return new GenericRightClickAction(attributeMap);
        }
    }

    /**
     *
     */
    private static final SignalDataAccessor<PlayerInteractEvent.RightClickBlock> EVENT_QUERY = new SignalDataAccessor<PlayerInteractEvent.RightClickBlock>()
    {
        /**
         *
         * @param rightClickBlock
         * @return
         */
        @Override
        public World getWorld(PlayerInteractEvent.RightClickBlock rightClickBlock)
        {
            return rightClickBlock.getWorld();
        }

        /**
         *
         * @param rightClickBlock
         * @return
         */
        @Override
        public BlockPos getPos(PlayerInteractEvent.RightClickBlock rightClickBlock)
        {
            return rightClickBlock.getPos();
        }

        /**
         *
         * @param rightClickBlock
         * @return
         */
        @Override
        public BlockPos getValidBlockPos(PlayerInteractEvent.RightClickBlock rightClickBlock)
        {
            return rightClickBlock.getPos();
        }

        /**
         *
         * @param rightClickBlock
         * @return
         */
        @Override
        public int getY(PlayerInteractEvent.RightClickBlock rightClickBlock)
        {
            return rightClickBlock.getPos().getY();
        }

        /**
         *
         * @param rightClickBlock
         * @return
         */
        @Override
        public Entity getEntity(PlayerInteractEvent.RightClickBlock rightClickBlock)
        {
            return rightClickBlock.getEntityPlayer();
        }

        /**
         *
         * @param rightClickBlock
         * @return
         */
        @Override
        public DamageSource getSource(PlayerInteractEvent.RightClickBlock rightClickBlock)
        {
            return null;
        }

        /**
         *
         * @param rightClickBlock
         * @return
         */
        @Override
        public Entity getAttacker(PlayerInteractEvent.RightClickBlock rightClickBlock)
        {
            return null;
        }

        /**
         *
         * @param rightClickBlock
         * @return
         */
        @Override
        public EntityPlayerMP getPlayer(PlayerInteractEvent.RightClickBlock rightClickBlock)
        {
            return (EntityPlayerMP) rightClickBlock.getEntityPlayer();
        }

        /**
         *
         * @param rightClickBlock
         * @return
         */
        @Override
        public ItemStack getItem(PlayerInteractEvent.RightClickBlock rightClickBlock)
        {
            return rightClickBlock.getItemStack();
        }
    };

    /**
     * @param rightClickBlock
     */
    public void action(PlayerInteractEvent.RightClickBlock rightClickBlock)
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
                return rightClickBlock.getEntityPlayer();
            }

            /**
             *
             * @return
             */
            @Override
            public EntityPlayerMP getPlayer()
            {
                return (EntityPlayerMP) rightClickBlock.getEntityPlayer();
            }

            /**
             *
             * @return
             */
            @Override
            public World getWorld()
            {
                return rightClickBlock.getWorld();
            }

            /**
             *
             * @return
             */
            @Override
            public Entity getEntity()
            {
                return rightClickBlock.getEntity();
            }

            /**
             *
             * @return
             */
            @Override
            public BlockPos getPosition()
            {
                return rightClickBlock.getPos();
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
