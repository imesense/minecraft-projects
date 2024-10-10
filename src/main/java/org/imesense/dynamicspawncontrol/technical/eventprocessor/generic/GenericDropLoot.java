package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

import com.google.common.base.Predicate;
import com.google.gson.JsonElement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.imesense.dynamicspawncontrol.technical.attributefactory.Attribute;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMapFactory;
import org.imesense.dynamicspawncontrol.technical.customlibrary.*;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataAccessor;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataGetter;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWords.CommonKeyWorlds.*;
import static org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWords.DroopLoot.*;

/**
 *
 */
public final class GenericDropLoot extends ListActionsConsumer<SignalDataGetter>
{
    /**
     *
     */
    private boolean removeAll = false;

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
    private static final Random RANDOM = new Random();

    /**
     *
     * @return
     */
    public boolean isRemoveAll() { return this.removeAll; }

    /**
     *
     */
    private final List<Predicate<ItemStack>> TO_REMOVE_ITEMS = new ArrayList<>();

    /**
     *
     * @return
     */
    public List<Predicate<ItemStack>> getToRemoveItems() { return this.TO_REMOVE_ITEMS; }

    /**
     *
     */
    private static final AttributeMapFactory<Object> FACTORY = new AttributeMapFactory<>();

    /**
     *
     */
    private final List<Pair<ItemStack, Function<Integer, Integer>>> TO_ADD_ITEMS = new ArrayList<>();

    /**
     *
     * @return
     */
    public List<Pair<ItemStack, Function<Integer, Integer>>> getToAddItems() { return this.TO_ADD_ITEMS; }

    /**
     *
     * @param event
     * @return
     */
    public boolean match(LivingDropsEvent event) { return RULE_EVALUATOR.match(event, EVENT_QUERY); }

    /**
     *
     * @param map
     */
    private GenericDropLoot(AttributeMap<?> map)
    {
        super();

        Log.writeDataToLogFile(0, String.format("Iterator for [%s] number [%d]", GenericDropLoot.class.getName(), countCreatedMaps++));

        this.RULE_EVALUATOR = new ListActionsBinary<>(map);

        this.addActions(map);

        if (map.has(ACTION_ITEM))
        {
            this.addItem(map);
        }

        if (map.has(MultipleKeyWords.DroopLoot.ACTION_REMOVE))
        {
            this.removeItem(map);
        }

        if (map.has(MultipleKeyWords.DroopLoot.ACTION_REMOVE_ALL))
        {
            this.removeAll = (Boolean) map.get(MultipleKeyWords.DroopLoot.ACTION_REMOVE_ALL);
        }
    }

    /**
     *
     * @param element
     * @return
     */
    public static GenericDropLoot parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            AttributeMap<?> map = FACTORY.parse(element);

            return new GenericDropLoot(map);
        }
    }

    /**
     *
     */
    private static final SignalDataAccessor<LivingDropsEvent> EVENT_QUERY = new SignalDataAccessor<LivingDropsEvent>()
    {
        /**
         *
         * @param data
         * @return
         */
        @Override
        public World getWorld(LivingDropsEvent data)
        {
            return data.getEntity().getEntityWorld();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public BlockPos getPos(LivingDropsEvent data)
        {
            return data.getEntity().getPosition();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public BlockPos getValidBlockPos(LivingDropsEvent data)
        {
            return data.getEntity().getPosition().down();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public int getY(LivingDropsEvent data)
        {
            return data.getEntity().getPosition().getY();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public Entity getEntity(LivingDropsEvent data)
        {
            return data.getEntity();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public DamageSource getSource(LivingDropsEvent data)
        {
            return data.getSource();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public Entity getAttacker(LivingDropsEvent data)
        {
            return data.getSource().getTrueSource();
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public EntityPlayerMP getPlayer(LivingDropsEvent data)
        {
            Entity entity = data.getSource().getTrueSource();

            return entity instanceof EntityPlayerMP ? (EntityPlayerMP) entity : null;
        }

        /**
         *
         * @param data
         * @return
         */
        @Override
        public ItemStack getItem(LivingDropsEvent data)
        {
            return ItemStack.EMPTY;
        }
    };

    /**
     *
     */
    static
    {
        FACTORY
                .attribute(Attribute.create(ID_RULE))
                .attribute(Attribute.create(SEE_SKY))
                .attribute(Attribute.create(WEATHER))
                .attribute(Attribute.create(STRUCTURE))
                .attribute(Attribute.createMulti(BIOMES))
                .attribute(Attribute.createMulti(BIOMES_TYPE))
                .attribute(Attribute.createMulti(DIMENSION))

                .attribute(Attribute.createMulti(HELMET))
                .attribute(Attribute.createMulti(CHEST_PLATE))
                .attribute(Attribute.createMulti(LEGGINGS))
                .attribute(Attribute.createMulti(BOOTS))

                .attribute(Attribute.create(MIN_TIME))
                .attribute(Attribute.create(MAX_TIME))

                .attribute(Attribute.create(MIN_LIGHT))
                .attribute(Attribute.create(MAX_LIGHT))

                .attribute(Attribute.create(MIN_HEIGHT))
                .attribute(Attribute.create(MAX_HEIGHT))

                .attribute(Attribute.create(DIFFICULTY))
                .attribute(Attribute.create(MIN_DIFFICULTY))
                .attribute(Attribute.create(MAX_DIFFICULTY))

                .attribute(Attribute.create(MIN_SPAWN_DIST))
                .attribute(Attribute.create(MAX_SPAWN_DIST))

                .attribute(Attribute.createMulti(BLOCK))
                .attribute(Attribute.create(BLOCK_OFFSET))

                .attribute(Attribute.create(GET_MOON_PHASE))

                .attribute(Attribute.createMulti(MOB))

                .attribute(Attribute.create(ANIMALS))
                .attribute(Attribute.create(MONSTERS))

                .attribute(Attribute.create(PLAYER))
                .attribute(Attribute.create(FAKE_PLAYER))
                .attribute(Attribute.create(REAL_PLAYER))
                .attribute(Attribute.createMulti(HELD_ITEM))
                .attribute(Attribute.createMulti(PLAYER_HELD_ITEM))
                .attribute(Attribute.createMulti(OFF_HAND_ITEM))
                .attribute(Attribute.createMulti(BOTH_HANDS_ITEM))

                .attribute(Attribute.create(PROJECTILE))
                .attribute(Attribute.create(EXPLOSION))
                .attribute(Attribute.create(FIRE))
                .attribute(Attribute.create(MAGIC))
                .attribute(Attribute.createMulti(SOURCE))

                .attribute(Attribute.create(RANDOM_KEY_0))
                .attribute(Attribute.create(RANDOM_KEY_1))
                .attribute(Attribute.create(RANDOM_KEY_2))
                .attribute(Attribute.create(RANDOM_KEY_3))
                .attribute(Attribute.create(RANDOM_KEY_4))

                .attribute(Attribute.createMulti(ACTION_ITEM))
                .attribute(Attribute.createMulti(ACTION_REMOVE))
                .attribute(Attribute.create(ACTION_REMOVE_ALL))
                .attribute(Attribute.create(ACTION_ITEM_NBT))
                .attribute(Attribute.create(ACTION_ITEM_COUNT))
        ;
    }

    /**
     *
     * @param itemCount
     * @return
     */
    private static Function<Integer, Integer> getCountFunction(@Nullable String itemCount)
    {
        if (itemCount == null)
        {
            return looting -> 1;
        }

        String[] lootTable = StringUtils.split(itemCount, '/');

        int[] min = new int[lootTable.length];
        int[] max = new int[lootTable.length];

        for (int i = 0; i < lootTable.length; i++)
        {
            String[] splitMinMax = StringUtils.split(lootTable[i], '-');

            if (splitMinMax.length == 1)
            {
                try
                {
                    min[i] = max[i] = Integer.parseInt(splitMinMax[0]);
                }
                catch (NumberFormatException exception)
                {
                    Log.writeDataToLogFile(2, "Bad amount specified in loot rule: " + splitMinMax[0]);
                    min[i] = max[i] = 1;
                }
            }
            else if (splitMinMax.length == 2)
            {
                try
                {
                    min[i] = Integer.parseInt(splitMinMax[0]);
                    max[i] = Integer.parseInt(splitMinMax[1]);
                }
                catch (NumberFormatException exception)
                {
                    Log.writeDataToLogFile(2, "Bad amounts specified in loot rule: " + splitMinMax[0]);
                    min[i] = max[i] = 1;
                }
            }
            else
            {
                Log.writeDataToLogFile(2, "Bad amount range specified in loot rule: " + splitMinMax[0]);
                min[i] = max[i] = 1;
            }
        }

        if (lootTable.length == 1)
        {
            if (min[0] == max[0])
            {
                return looting -> min[0];
            }
            else
            {
                return looting -> RANDOM.nextInt(max[0] - min[0] + 1) + min[0];
            }
        }
        else
        {
            return looting ->
            {
                if (looting >= min.length)
                {
                    return RANDOM.nextInt(max[min.length - 1] - min[min.length - 1] + 1) + min[min.length - 1];
                }
                else if (looting >= 0)
                {
                    return RANDOM.nextInt(max[looting] - min[looting] + 1) + min[looting];
                }
                else
                {
                    return RANDOM.nextInt(max[0] - min[0] + 1) + min[0];
                }
            };
        }
    }

    /**
     *
     * @param itemNames
     * @param nbtJson
     * @param itemCount
     * @return
     */
    private static List<Pair<ItemStack, Function<Integer, Integer>>> getItems(List<String> itemNames, @Nullable String nbtJson,
                                                                              @Nullable String itemCount)
    {
        Function<Integer, Integer> countFunction = getCountFunction(itemCount);

        List<Pair<ItemStack, Function<Integer, Integer>>> items = new ArrayList<>();

        for (String name : itemNames)
        {
            ItemStack stack = ItemStackBuilder.parseStack(name);

            if (stack.isEmpty())
            {
                Log.writeDataToLogFile(2, "Unknown item '" + name + "'!");
            }
            else
            {
                if (nbtJson != null)
                {
                    try
                    {
                        stack.setTagCompound(JsonToNBT.getTagFromJson(nbtJson));
                    }
                    catch (NBTException exception)
                    {
                        Log.writeDataToLogFile(2, "Bad nbt for '" + name + "'!");
                    }
                }

                items.add(Pair.of(stack, countFunction));
            }
        }

        return items;
    }

    /**
     *
     * @param map
     */
    private void addItem(AttributeMap<?> map)
    {
        Object nbt = map.get(MultipleKeyWords.DroopLoot.ACTION_ITEM_NBT);
        Object itemCount = map.get(MultipleKeyWords.DroopLoot.ACTION_ITEM_COUNT);

        this.TO_ADD_ITEMS.addAll(getItems(map.getList(ACTION_ITEM), (String)nbt, (String)itemCount));
    }

    /**
     *
     * @param map
     */
    private void removeItem(AttributeMap<?> map)
    {
        this.TO_REMOVE_ITEMS.addAll(AuxFunctions.getItems((JsonElement)map.getList(MultipleKeyWords.DroopLoot.ACTION_REMOVE)));
    }
}