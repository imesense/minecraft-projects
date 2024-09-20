package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

import com.google.gson.JsonElement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.imesense.dynamicspawncontrol.technical.attributefactory.Attribute;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMapFactory;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionsBinary;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ListActionsSingleEvent;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.SignalDataAccessor;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.SignalDataGetter;

import java.util.function.Consumer;

import static org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWords.CommonKeyWorlds.*;
import static org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWords.MobsTaskManager.*;

/**
 *
 */
public final class GenericMobsTaskManager extends ListActionsSingleEvent<SignalDataGetter>
{
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
     */
    private static final AttributeMapFactory<Object> FACTORY = new AttributeMapFactory<>();

    /**
     *
     * @param event
     * @return
     */
    public boolean match(EntityJoinWorldEvent event) { return RULE_EVALUATOR.match(event, EVENT_QUERY_JOIN); }

    /**
     *
     * @param map
     * @param nameClass
     */
    private GenericMobsTaskManager(AttributeMap<?> map, String nameClass)
    {
        super(nameClass);

        Log.writeDataToLogFile(Log.TypeLog[0], String.format("Iterator for [%s] number [%d]", nameClass, countCreatedMaps++));

        this.RULE_EVALUATOR = new ListActionsBinary<>(map, nameClass);

        this.addActions(map);
    }

    /**
     *
     * @param element
     * @return
     */
    public static GenericMobsTaskManager parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            AttributeMap<?> map = FACTORY.parse(element);

            return new GenericMobsTaskManager(map, "GenericMobsTaskManager");
        }
    }

    /**
     *
     */
    public static final SignalDataAccessor<EntityJoinWorldEvent> EVENT_QUERY_JOIN = new SignalDataAccessor<EntityJoinWorldEvent>()
    {
        /**
         *
         * @param EntityJoinWorldEvent
         * @return
         */
        @Override
        public World getWorld(EntityJoinWorldEvent EntityJoinWorldEvent)
        {
            return EntityJoinWorldEvent.getWorld();
        }

        /**
         *
         * @param EntityJoinWorldEvent
         * @return
         */
        @Override
        public BlockPos getPos(EntityJoinWorldEvent EntityJoinWorldEvent)
        {
            return EntityJoinWorldEvent.getEntity().getPosition();
        }

        /**
         *
         * @param EntityJoinWorldEvent
         * @return
         */
        @Override
        public BlockPos getValidBlockPos(EntityJoinWorldEvent EntityJoinWorldEvent)
        {
            return EntityJoinWorldEvent.getEntity().getPosition().down();
        }

        /**
         *
         * @param EntityJoinWorldEvent
         * @return
         */
        @Override
        public int getY(EntityJoinWorldEvent EntityJoinWorldEvent)
        {
            return EntityJoinWorldEvent.getEntity().getPosition().getY();
        }

        /**
         *
         * @param EntityJoinWorldEvent
         * @return
         */
        @Override
        public Entity getEntity(EntityJoinWorldEvent EntityJoinWorldEvent)
        {
            return EntityJoinWorldEvent.getEntity();
        }

        /**
         *
         * @param EntityJoinWorldEvent
         * @return
         */
        @Override
        public DamageSource getSource(EntityJoinWorldEvent EntityJoinWorldEvent)
        {
            return null;
        }

        /**
         *
         * @param EntityJoinWorldEvent
         * @return
         */
        @Override
        public Entity getAttacker(EntityJoinWorldEvent EntityJoinWorldEvent)
        {
            return null;
        }

        /**
         *
         * @param EntityJoinWorldEvent
         * @return
         */
        @Override
        public EntityPlayer getPlayer(EntityJoinWorldEvent EntityJoinWorldEvent)
        {
            return getClosestPlayer(EntityJoinWorldEvent.getWorld(), EntityJoinWorldEvent.getEntity().getPosition());
        }

        /**
         *
         * @param EntityJoinWorldEvent
         * @return
         */
        @Override
        public ItemStack getItem(EntityJoinWorldEvent EntityJoinWorldEvent)
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
    private static EntityPlayer getClosestPlayer(World world, BlockPos blockPos)
    {
        return world.getClosestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 100, false);
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
     * @param event
     */
    public void action(EntityJoinWorldEvent event)
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
                return event.getEntity() instanceof EntityLivingBase ? (EntityLivingBase) event.getEntity() : null;
            }

            /**
             *
             * @return
             */
            @Override
            public EntityPlayer getPlayer()
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
                return event.getEntity() != null ? event.getEntity().getPosition() : null;
            }
        };

        /**
         *
         */
        for (Consumer<SignalDataGetter> action : actions)
        {
            action.accept(eventBase);
        }
    }
}
