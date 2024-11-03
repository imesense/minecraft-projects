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
public final class GenericLeftClickAction extends ListActionConsumer<SignalDataGetter>
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
     * @param leftClickBlock
     * @return
     */
    public boolean match(PlayerInteractEvent.LeftClickBlock leftClickBlock) { return RULE_EVALUATOR.match(leftClickBlock, EVENT_QUERY); }

    /**
     *
     * @param attributeMap
     */
    private GenericLeftClickAction(AttributeMap<?> attributeMap)
    {
        super();

        Log.writeDataToLogFile(0, String.format("Iterator for [%s] number [%d]", GenericLeftClickAction.class.getName(), countCreatedMaps++));

        this.RULE_EVALUATOR = new ListActionBinary<>(attributeMap);

        this.addActions(attributeMap);

        this.RESULT = RESULT_EVENTS.getResult(attributeMap);
    }

    /**
     *
     * @param jsonElement
     * @return
     */
    public static GenericLeftClickAction parse(JsonElement jsonElement)
    {
        if (jsonElement == null)
        {
            return null;
        }
        else
        {
            AttributeMap<Object> attributeMap = ListActionStaticFactoryMouse.FACTORY.parse(jsonElement);

            return new GenericLeftClickAction(attributeMap);
        }
    }

    /**
     *
     */
    private static final SignalDataAccessor<PlayerInteractEvent.LeftClickBlock> EVENT_QUERY = new SignalDataAccessor<PlayerInteractEvent.LeftClickBlock>()
    {
        /**
         *
         * @param leftClickBlock
         * @return
         */
        @Override
        public World getWorld(PlayerInteractEvent.LeftClickBlock leftClickBlock)
        {
            return leftClickBlock.getWorld();
        }

        /**
         *
         * @param leftClickBlock
         * @return
         */
        @Override
        public BlockPos getPos(PlayerInteractEvent.LeftClickBlock leftClickBlock)
        {
            return leftClickBlock.getPos();
        }

        /**
         *
         * @param leftClickBlock
         * @return
         */
        @Override
        public BlockPos getValidBlockPos(PlayerInteractEvent.LeftClickBlock leftClickBlock)
        {
            return leftClickBlock.getPos();
        }

        /**
         *
         * @param leftClickBlock
         * @return
         */
        @Override
        public int getY(PlayerInteractEvent.LeftClickBlock leftClickBlock)
        {
            return leftClickBlock.getPos().getY();
        }

        /**
         *
         * @param leftClickBlock
         * @return
         */
        @Override
        public Entity getEntity(PlayerInteractEvent.LeftClickBlock leftClickBlock)
        {
            return leftClickBlock.getEntityPlayer();
        }

        /**
         *
         * @param leftClickBlock
         * @return
         */
        @Override
        public DamageSource getSource(PlayerInteractEvent.LeftClickBlock leftClickBlock)
        {
            return null;
        }

        /**
         *
         * @param leftClickBlock
         * @return
         */
        @Override
        public Entity getAttacker(PlayerInteractEvent.LeftClickBlock leftClickBlock)
        {
            return null;
        }

        /**
         *
         * @param leftClickBlock
         * @return
         */
        @Override
        public EntityPlayerMP getPlayer(PlayerInteractEvent.LeftClickBlock leftClickBlock)
        {
            return (EntityPlayerMP) leftClickBlock.getEntityPlayer();
        }

        /**
         *
         * @param leftClickBlock
         * @return
         */
        @Override
        public ItemStack getItem(PlayerInteractEvent.LeftClickBlock leftClickBlock)
        {
            return leftClickBlock.getItemStack();
        }
    };

    /**
     *
     * @param leftClickBlock
     */
    public void action(PlayerInteractEvent.LeftClickBlock leftClickBlock)
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
                return leftClickBlock.getEntityPlayer();
            }

            /**
             *
             * @return
             */
            @Override
            public EntityPlayerMP getPlayer()
            {
                return (EntityPlayerMP) leftClickBlock.getEntityPlayer();
            }

            /**
             *
             * @return
             */
            @Override
            public World getWorld()
            {
                return leftClickBlock.getWorld();
            }

            /**
             *
             * @return
             */
            @Override
            public Entity getEntity()
            {
                return leftClickBlock.getEntity();
            }

            /**
             *
             * @return
             */
            @Override
            public BlockPos getPosition()
            {
                return leftClickBlock.getPos();
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
