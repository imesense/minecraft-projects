package org.imesense.dynamicspawncontrol.eventprocessor.generic;

import com.google.gson.JsonElement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.imesense.dynamicspawncontrol.technical.attributefactory.Attribute;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMapFactory;
import org.imesense.dynamicspawncontrol.eventprocessor.listaction.ListActionBinary;
import org.imesense.dynamicspawncontrol.eventprocessor.listaction.ListActionConsumerMobTaskManager;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.api.SignalDataAccessor;
import org.imesense.dynamicspawncontrol.core.api.SignalDataGetter;

import java.util.function.Consumer;

import static org.imesense.dynamicspawncontrol.eventprocessor.generic.keyword.CommonKeyWord.*;
import static org.imesense.dynamicspawncontrol.eventprocessor.generic.keyword.MobTaskManager.*;

/**
 *
 */
public final class GenericMobTaskManager extends ListActionConsumerMobTaskManager<SignalDataGetter>
{
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
     */
    private static final AttributeMapFactory<Object> FACTORY = new AttributeMapFactory<>();

    /**
     *
     * @param entityJoinWorldEvent
     * @return
     */
    public boolean match(EntityJoinWorldEvent entityJoinWorldEvent) { return RULE_EVALUATOR.match(entityJoinWorldEvent, EVENT_QUERY_JOIN); }

    /**
     *
     * @param attributeMap
     */
    private GenericMobTaskManager(AttributeMap<?> attributeMap)
    {
        super();

        Log.writeDataToLogFile(0, String.format("Iterator for [%s] number [%d]",
                GenericMobTaskManager.class.getName(), countCreatedMaps++));

        this.RULE_EVALUATOR = new ListActionBinary<>(attributeMap);

        this.addActions(attributeMap);
    }

    /**
     *
     * @param jsonElement
     * @return
     */
    public static GenericMobTaskManager parse(JsonElement jsonElement)
    {
        if (jsonElement == null)
        {
            return null;
        }
        else
        {
            AttributeMap<?> attributeMap = FACTORY.parse(jsonElement);

            return new GenericMobTaskManager(attributeMap);
        }
    }

    /**
     *
     */
    public static final SignalDataAccessor<EntityJoinWorldEvent> EVENT_QUERY_JOIN = new SignalDataAccessor<EntityJoinWorldEvent>()
    {
        /**
         *
         * @param entityJoinWorldEvent
         * @return
         */
        @Override
        public World getWorld(EntityJoinWorldEvent entityJoinWorldEvent)
        {
            return entityJoinWorldEvent.getWorld();
        }

        /**
         *
         * @param entityJoinWorldEvent
         * @return
         */
        @Override
        public BlockPos getPos(EntityJoinWorldEvent entityJoinWorldEvent)
        {
            return entityJoinWorldEvent.getEntity().getPosition();
        }

        /**
         *
         * @param entityJoinWorldEvent
         * @return
         */
        @Override
        public BlockPos getValidBlockPos(EntityJoinWorldEvent entityJoinWorldEvent)
        {
            return entityJoinWorldEvent.getEntity().getPosition().down();
        }

        /**
         *
         * @param entityJoinWorldEvent
         * @return
         */
        @Override
        public int getY(EntityJoinWorldEvent entityJoinWorldEvent)
        {
            return entityJoinWorldEvent.getEntity().getPosition().getY();
        }

        /**
         *
         * @param entityJoinWorldEvent
         * @return
         */
        @Override
        public Entity getEntity(EntityJoinWorldEvent entityJoinWorldEvent)
        {
            return entityJoinWorldEvent.getEntity();
        }

        /**
         *
         * @param entityJoinWorldEvent
         * @return
         */
        @Override
        public DamageSource getSource(EntityJoinWorldEvent entityJoinWorldEvent)
        {
            return null;
        }

        /**
         *
         * @param entityJoinWorldEvent
         * @return
         */
        @Override
        public Entity getAttacker(EntityJoinWorldEvent entityJoinWorldEvent)
        {
            return null;
        }

        /**
         *
         * @param entityJoinWorldEvent
         * @return
         */
        @Override
        public EntityPlayerMP getPlayer(EntityJoinWorldEvent entityJoinWorldEvent)
        {
            return getClosestPlayer(entityJoinWorldEvent.getWorld(), entityJoinWorldEvent.getEntity().getPosition());
        }

        /**
         *
         * @param entityJoinWorldEvent
         * @return
         */
        @Override
        public ItemStack getItem(EntityJoinWorldEvent entityJoinWorldEvent)
        {
            return ItemStack.EMPTY;
        }
    };

    /**
     *
     * @param world
     * @param blockPos
     * @return
     */
    private static EntityPlayerMP getClosestPlayer(World world, BlockPos blockPos)
    {
        return (EntityPlayerMP) world.getClosestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 100, false);
    }

    /**
     *
     */
    static
    {
        FACTORY
                .attribute(Attribute.create(ID_RULE))

                .attribute(Attribute.create(SEE_SKY))

                .attribute(Attribute.createMulti(DIMENSION))

                .attribute(Attribute.create(GET_MOON_PHASE))

                .attribute(Attribute.createMulti(ENEMIES_TO))

                .attribute(Attribute.createMulti(ENEMY_ID))

                .attribute(Attribute.createMulti(PANIC_TO))

                .attribute(Attribute.createMulti(PANIC_ID))

                .attribute(Attribute.createMulti(TO_THEM))

                .attribute(Attribute.createMulti(THEM_ID))
        ;
    }

    /**
     *
     * @param entityJoinWorldEvent
     */
    public void action(EntityJoinWorldEvent entityJoinWorldEvent)
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
                return entityJoinWorldEvent.getEntity() instanceof EntityLivingBase ? (EntityLivingBase) entityJoinWorldEvent.getEntity() : null;
            }

            /**
             *
             * @return
             */
            @Override
            public EntityPlayerMP getPlayer()
            {
                return null;
            }

            /**
             *
             * @return
             */
            @Override
            public World getWorld()
            {
                return entityJoinWorldEvent.getWorld();
            }

            /**
             *
             * @return
             */
            @Override
            public Entity getEntity()
            {
                return entityJoinWorldEvent.getEntity();
            }

            /**
             *
             * @return
             */
            @Override
            public BlockPos getPosition()
            {
                return entityJoinWorldEvent.getEntity() != null ? entityJoinWorldEvent.getEntity().getPosition() : null;
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
