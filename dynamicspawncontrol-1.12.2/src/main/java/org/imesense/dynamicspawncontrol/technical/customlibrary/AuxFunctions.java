package org.imesense.dynamicspawncontrol.technical.customlibrary;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.plexus.util.StringUtils;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataAccessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 *
 */
public class AuxFunctions
{
    /**
     *
     */
    public static class PotionEffectWithChance
    {
        /**
         *
         */
        public double Chance;

        /**
         *
         */
        public PotionEffect Effect;

        /**
         *
         * @param effect
         * @param chance
         */
        public PotionEffectWithChance(PotionEffect effect, double chance)
        {
            this.Effect = effect;
            this.Chance = chance;
        }
    }

    /**
     *
     */
    public static final Map<String, DamageSource> DamageMap = new HashMap<>();

    /**
     *
     */
    static
    {
        for
        (
            DamageSource source : new DamageSource[]
            {
                DamageSource.IN_FIRE, DamageSource.LIGHTNING_BOLT, DamageSource.ON_FIRE,
                DamageSource.LAVA, DamageSource.HOT_FLOOR, DamageSource.IN_WALL,
                DamageSource.CRAMMING, DamageSource.DROWN, DamageSource.STARVE,
                DamageSource.CACTUS, DamageSource.FALL, DamageSource.FLY_INTO_WALL,
                DamageSource.OUT_OF_WORLD, DamageSource.GENERIC, DamageSource.MAGIC,
                DamageSource.WITHER, DamageSource.ANVIL, DamageSource.FALLING_BLOCK,
                DamageSource.DRAGON_BREATH, DamageSource.FIREWORKS
            }
        )
        {
            DamageMap.put(source.getDamageType(), source);
        }
    }

    /**
     *
     * @param oreIDs
     * @param oreId
     * @return
     */
    public static boolean isMatchingOreId(int[] oreIDs, int oreId)
    {
        for (int id : oreIDs)
        {
            if (id == oreId)
            {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param oreId
     * @param block
     * @return
     */
    public static boolean isMatchingOreDict(int oreId, Block block)
    {
        ItemStack stack = new ItemStack(block);

        int[] oreIDs = stack.isEmpty() ? new int[0] : OreDictionary.getOreIDs(stack);

        return isMatchingOreId(oreIDs, oreId);
    }

    /**
     *
     * @param state
     * @param property
     * @param value
     * @return
     * @param <T>
     */
    public static <T extends Comparable<T>> IBlockState set(IBlockState state, IProperty<T> property, String value)
    {
        Optional<T> optionalValue = property.parseValue(value);

        if (optionalValue.isPresent())
        {
            return state.withProperty(property, optionalValue.get());
        }
        else
        {
            return state;
        }
    }

    /**
     *
     * @param json
     * @return
     */
    @Nonnull
    public static BiFunction<Event, SignalDataAccessor, BlockPos> parseOffset(String json)
    {
        int offsetX, offsetY, offsetZ;

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        JsonObject obj = element.getAsJsonObject();

        if (obj.has(EnumGameProperties.BlockProperties.OFFSET.getValue()))
        {
            JsonObject offset =
                    obj.getAsJsonObject(EnumGameProperties.BlockProperties.OFFSET.getValue());

            offsetX = offset.has(EnumGameProperties.Coordinates.X.getValue()) ?
                    offset.get(EnumGameProperties.Coordinates.X.getValue()).getAsInt() : 0;

            offsetY = offset.has(EnumGameProperties.Coordinates.Y.getValue()) ?
                    offset.get(EnumGameProperties.Coordinates.Y.getValue()).getAsInt() : 0;

            offsetZ = offset.has(EnumGameProperties.Coordinates.Z.getValue()) ?
                    offset.get(EnumGameProperties.Coordinates.Z.getValue()).getAsInt() : 0;
        }
        else
        {
            offsetX = 0;
            offsetY = 0;
            offsetZ = 0;
        }

        if (obj.has(EnumGameProperties.BlockProperties.STEP.getValue()))
        {
            return (event, query) ->
            {
                boolean isStandingOnBlock = RayTrace.isPlayerStandingOnBlock(query.getWorld(event), query.getPlayer(event));

                if (isStandingOnBlock)
                {
                    BlockPos blockPosBelow = RayTrace.getBlockPosBelowPlayer(query.getPlayer(event));
                    return blockPosBelow.add(offsetX, offsetY, offsetZ);
                }
                else
                {
                    return query.getPos(event).add(offsetX, offsetY, offsetZ);
                }
            };
        }

        if (obj.has(EnumGameProperties.BlockProperties.LOOK.getValue()))
        {
            return (event, query) ->
            {
                RayTraceResult result =
                        RayTrace.getMovingObjectPositionFromPlayer
                                (query.getWorld(event), query.getPlayer(event), false);

                if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
                {
                    return result.getBlockPos().add(offsetX, offsetY, offsetZ);
                }
                else
                {
                    return query.getPos(event).add(offsetX, offsetY, offsetZ);
                }
            };
        }

        return (event, query) -> query.getPos(event).add(offsetX, offsetY, offsetZ);
    }

    /**
     *
     * @param itemObj
     * @return
     */
    public static List<Predicate<ItemStack>> getItems(JsonElement itemObj)
    {
        List<Predicate<ItemStack>> items = new ArrayList<>();

        if (itemObj.isJsonObject())
        {
            Predicate<ItemStack> matcher = getMatcher(itemObj.getAsJsonObject());

            if (matcher != null)
            {
                items.add(matcher);
            }
        }
        else if (itemObj.isJsonArray())
        {
            for (JsonElement element : itemObj.getAsJsonArray())
            {
                JsonObject obj = element.getAsJsonObject();
                Predicate<ItemStack> matcher = getMatcher(obj);

                if (matcher != null)
                {
                    items.add(matcher);
                }
            }
        }
        else
        {
            Log.writeDataToLogFile(2, "Item description is not valid!");
        }

        return items;
    }

    /**
     *
     * @param itemNames
     * @return
     */
    public static List<Predicate<ItemStack>> getItems(List<String> itemNames)
    {
        List<Predicate<ItemStack>> items = new ArrayList<>();

        for (String json : itemNames)
        {
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(json);

            if (element.isJsonPrimitive())
            {
                String name = element.getAsString();
                Predicate<ItemStack> matcher = getMatcher(name);

                if (matcher != null)
                {
                    items.add(matcher);
                }

            }
            else if (element.isJsonObject())
            {
                JsonObject obj = element.getAsJsonObject();
                Predicate<ItemStack> matcher = getMatcher(obj);

                if (matcher != null)
                {
                    items.add(matcher);
                }
            }
            else
            {
                Log.writeDataToLogFile(2, "Item description '" + json + "' is not valid!");
            }
        }

        return items;
    }

    /**
     *
     * @param json
     * @return
     */
    @Nullable
    public static BiPredicate<World, BlockPos> parseBlock(String json)
    {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);

        if (element.isJsonPrimitive())
        {
            String blockName = element.getAsString();

            if (blockName.startsWith("ore:"))
            {
                int oreId = OreDictionary.getOreID(blockName.substring(4));
                return (world, pos) -> isMatchingOreDict(oreId, world.getBlockState(pos).getBlock());
            }
            else
            {
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));

                Log.writeDataToLogFile(0, "Block " + blockName);

                if (block == null)
                {
                    Log.writeDataToLogFile(2, "Block '" + blockName + "' is not valid!");
                    return null;
                }

                return (world, pos) -> world.getBlockState(pos).getBlock() == block;
            }
        }
        else if (element.isJsonObject())
        {
            JsonObject obj = element.getAsJsonObject();

            BiPredicate<World, BlockPos> test;

            if (obj.has("ore"))
            {
                int oreId = OreDictionary.getOreID(obj.get("ore").getAsString());
                test = (world, pos) -> isMatchingOreDict(oreId, world.getBlockState(pos).getBlock());
            }
            else if (obj.has("block"))
            {
                String blockName = obj.get("block").getAsString();
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));

                if (block == null)
                {
                    Log.writeDataToLogFile(2, "Block '" + blockName + "' is not valid!");
                    return null;
                }

                if (obj.has("properties"))
                {
                    IBlockState blockState = block.getDefaultState();
                    JsonArray propArray = obj.get("properties").getAsJsonArray();

                    for (JsonElement el : propArray)
                    {
                        JsonObject propObj = el.getAsJsonObject();
                        String name = propObj.get("name").getAsString();
                        String value = propObj.get("value").getAsString();

                        for (IProperty<?> key : blockState.getPropertyKeys())
                        {
                            if (name.equals(key.getName()))
                            {
                                blockState = set(blockState, key, value);
                            }
                        }
                    }

                    IBlockState finalBlockState = blockState;
                    test = (world, pos) -> world.getBlockState(pos) == finalBlockState;
                }
                else
                {
                    test = (world, pos) -> world.getBlockState(pos).getBlock() == block;
                }
            }
            else
            {
                test = (world, pos) -> true;
            }

            if (obj.has("mod"))
            {
                String mod = obj.get("mod").getAsString();
                BiPredicate<World, BlockPos> finalTest = test;
                test = (world, pos) -> finalTest.test(world, pos) && mod.equals(world.getBlockState(pos).getBlock().getRegistryName().getResourceDomain());
            }

            if (obj.has("energy"))
            {
                Predicate<Integer> energy = getExpression(obj.get("energy"));

                if (energy != null)
                {
                    EnumFacing side;

                    if (obj.has("side"))
                    {
                        side = EnumFacing.byName(obj.get("side").getAsString().toLowerCase());
                    }
                    else
                    {
                        side = null;
                    }

                    BiPredicate<World, BlockPos> finalTest = test;
                    test = (world, pos) -> finalTest.test(world, pos) && energy.test(getEnergy(world, pos, side));
                }
            }

            if (obj.has("contains"))
            {
                EnumFacing side;

                if (obj.has("side"))
                {
                    side = EnumFacing.byName(obj.get("energyside").getAsString().toLowerCase());
                }
                else
                {
                    side = null;
                }

                List<Predicate<ItemStack>> items = getItems(obj.get("contains"));
                BiPredicate<World, BlockPos> finalTest = test;
                test = (world, pos) -> finalTest.test(world, pos) && contains(world, pos, side, items);
            }

            return test;
        }
        else
        {
            Log.writeDataToLogFile(2, "Block description '" + json + "' is not valid!");
        }

        return null;
    }

    /**
     *
     * @param expression
     * @return
     */
    public static Predicate<Integer> getExpression(String expression)
    {
        try
        {
            if (expression.startsWith(">="))
            {
                int amount = Integer.parseInt(expression.substring(2));

                return i -> i >= amount;
            }

            if (expression.startsWith(">"))
            {
                int amount = Integer.parseInt(expression.substring(1));

                return i -> i > amount;
            }

            if (expression.startsWith("<="))
            {
                int amount = Integer.parseInt(expression.substring(2));

                return i -> i <= amount;
            }

            if (expression.startsWith("<"))
            {
                int amount = Integer.parseInt(expression.substring(1));

                return i -> i < amount;
            }

            if (expression.startsWith("="))
            {
                int amount = Integer.parseInt(expression.substring(1));

                return i -> i == amount;
            }

            if (expression.startsWith("!=") || expression.startsWith("<>"))
            {
                int amount = Integer.parseInt(expression.substring(2));

                return i -> i != amount;
            }

            if (expression.contains("-"))
            {
                String[] split = StringUtils.split(expression, "-");

                int amount1 = Integer.parseInt(split[0]);
                int amount2 = Integer.parseInt(split[1]);

                return i -> i >= amount1 && i <= amount2;
            }

            int amount = Integer.parseInt(expression);

            return i -> i == amount;
        }
        catch (NumberFormatException exception)
        {
            Log.writeDataToLogFile(2, "Bad expression '" + expression + "'!");
            return null;
        }
    }

    /**
     *
     * @param element
     * @return
     */
    public static Predicate<Integer> getExpression(JsonElement element)
    {
        if (element.isJsonPrimitive())
        {
            if (element.getAsJsonPrimitive().isNumber())
            {
                int amount = element.getAsInt();

                return i -> i == amount;
            }
            else
            {
                return getExpression(element.getAsString());
            }
        }
        else
        {
            Log.writeDataToLogFile(2, "Bad expression!");
            throw new RuntimeException();
        }
    }

    /**
     *
     * @param name
     * @return
     */
    public static Predicate<ItemStack> getMatcher(String name)
    {
        ItemStack stack = ItemStackBuilder.parseStack(name);

        if (!stack.isEmpty())
        {
            if (name.contains("/") && name.contains("@"))
            {
                return s -> ItemStack.areItemsEqual(s, stack) && ItemStack.areItemStackTagsEqual(s, stack);
            }
            else if (name.contains("/"))
            {
                return s -> ItemStack.areItemsEqualIgnoreDurability(s, stack) && ItemStack.areItemStackTagsEqual(s, stack);
            }
            else if (name.contains("@"))
            {
                return s -> ItemStack.areItemsEqual(s, stack);
            }
            else
            {
                return s -> s.getItem() == stack.getItem();
            }
        }

        return null;
    }

    /**
     *
     * @param jsonObject
     * @return
     */
    public static Predicate<ItemStack> getMatcher(JsonObject jsonObject)
    {
        if (jsonObject.has("empty"))
        {
            boolean empty = jsonObject.get("empty").getAsBoolean();
            return s -> s.isEmpty() == empty;
        }

        String name = jsonObject.get("item").getAsString();
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));

        if (item == null)
        {
            Log.writeDataToLogFile(2, "Unknown item '" + name + "'!");
            return null;
        }

        Predicate<ItemStack> test;

        if (jsonObject.has("damage"))
        {
            Predicate<Integer> damage = getExpression(jsonObject.get("damage"));

            if (damage == null)
            {
                return null;
            }

            test = s -> s.getItem() == item && damage.test(s.getItemDamage());
        }
        else
        {
            test = s -> s.getItem() == item;
        }

        if (jsonObject.has("count"))
        {
            Predicate<Integer> count = getExpression(jsonObject.get("count"));

            if (count != null)
            {
                Predicate<ItemStack> finalTest = test;
                test = s -> finalTest.test(s) && count.test(s.getCount());
            }
        }

        if (jsonObject.has("ore"))
        {
            int oreId = OreDictionary.getOreID(jsonObject.get("ore").getAsString());
            Predicate<ItemStack> finalTest = test;
            test = s -> finalTest.test(s) && isMatchingOreId(s.isEmpty() ? new int[0] : OreDictionary.getOreIDs(s), oreId);
        }

        if (jsonObject.has("mod"))
        {
            Predicate<ItemStack> finalTest = test;
            test = s -> finalTest.test(s) && "mod".equals(s.getItem().getRegistryName().getResourceDomain());
        }

        if (jsonObject.has("nbt"))
        {
            List<Predicate<NBTTagCompound>> nbtMatchers = getNbtMatchers(jsonObject);

            if (nbtMatchers != null)
            {
                Predicate<ItemStack> finalTest = test;
                test = s -> finalTest.test(s) && nbtMatchers.stream().allMatch(p -> p.test(s.getTagCompound()));
            }
        }

        if (jsonObject.has("energy"))
        {
            Predicate<Integer> energy = getExpression(jsonObject.get("energy"));

            if (energy != null)
            {
                Predicate<ItemStack> finalTest = test;
                test = s -> finalTest.test(s) && energy.test(getEnergy(s));
            }
        }

        return test;
    }

    /**
     *
     * @param itemStack
     * @return
     */
    public static int getEnergy(ItemStack itemStack)
    {
        if (itemStack.hasCapability(CapabilityEnergy.ENERGY, null))
        {
            IEnergyStorage capability = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
            return capability.getEnergyStored();
        }

        return 0;
    }

    /**
     *
     * @param world
     * @param blockPos
     * @param side
     * @param predicateItemStack
     * @return
     */
    public static boolean contains(World world, BlockPos blockPos, @Nullable EnumFacing side, @Nonnull List<Predicate<ItemStack>> predicateItemStack)
    {
        TileEntity tileEntity = world.getTileEntity(blockPos);

        if (tileEntity != null && tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side))
        {
            IItemHandler handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);

            for (int i = 0; i < Objects.requireNonNull(handler).getSlots() ; i++)
            {
                ItemStack stack = handler.getStackInSlot(i);

                if (!stack.isEmpty())
                {
                    for (Predicate<ItemStack> matcher : predicateItemStack)
                    {
                        if (matcher.test(stack))
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     *
     * @param world
     * @param blockPos
     * @param side
     * @return
     */
    public static int getEnergy(World world, BlockPos blockPos, @Nullable EnumFacing side)
    {
        TileEntity tileEntity = world.getTileEntity(blockPos);

        if (tileEntity != null && tileEntity.hasCapability(CapabilityEnergy.ENERGY, side))
        {
            IEnergyStorage energy = tileEntity.getCapability(CapabilityEnergy.ENERGY, side);
            return energy.getEnergyStored();
        }

        return 0;
    }

    /**
     *
     * @param jsonObject
     * @return
     */
    public static List<Predicate<NBTTagCompound>> getNbtMatchers(JsonObject jsonObject)
    {
        JsonArray nbtArray = jsonObject.getAsJsonArray("nbt");

        return getNbtMatchers(nbtArray);
    }

    /**
     *
     * @param nbtArray
     * @return
     */
    public static List<Predicate<NBTTagCompound>> getNbtMatchers(JsonArray nbtArray)
    {
        List<Predicate<NBTTagCompound>> nbtMatchers = new ArrayList<>();

        for (JsonElement element : nbtArray)
        {
            JsonObject o = element.getAsJsonObject();
            String tag = o.get("tag").getAsString();

            if (o.has("contains"))
            {
                List<Predicate<NBTTagCompound>> subMatchers = getNbtMatchers(o.getAsJsonArray("contains"));

                nbtMatchers.add(tagCompound ->
                {
                    if (tagCompound != null)
                    {
                        NBTTagList list = tagCompound.getTagList(tag, Constants.NBT.TAG_COMPOUND);

                        for (NBTBase base : list)
                        {
                            for (Predicate<NBTTagCompound> matcher : Objects.requireNonNull(subMatchers))
                            {
                                if (matcher.test((NBTTagCompound) base))
                                {
                                    return true;
                                }
                            }
                        }
                    }
                    return false;
                });
            }
            else
            {
                Predicate<Integer> nbt = getExpression(o.get("value"));

                if (nbt == null)
                {
                    return null;
                }

                nbtMatchers.add(tagCompound -> nbt.test(tagCompound.getInteger(tag)));
            }
        }

        return nbtMatchers;
    }

    /**
     *
     * @param entity
     * @return
     */
    public static boolean isFakePlayer(Entity entity)
    {
        if (!(entity instanceof EntityPlayerMP))
        {
            return false;
        }

        if (entity instanceof FakePlayer)
        {
            return true;
        }

        // Если этот метод возвращает false, всё ещё возможно, что это фальшивый игрок. Попробуем найти игрока в списке онлайн-игроков
        PlayerList playerList = Objects.requireNonNull(DimensionManager.getWorld(0).getMinecraftServer()).getPlayerList();
        EntityPlayerMP playerByUUID = playerList.getPlayerByUUID(((EntityPlayerMP) entity).getGameProfile().getId());

        if (playerByUUID == null)
        {
            // Игрок не в сети. Значит, это не может быть реальный игрок
            return true;
        }

        // Игрок в списке. Но это ли тот игрок?
        return entity != playerByUUID;
    }

    /**
     *
     * @param entity
     * @return
     */
    public static boolean isRealPlayer(Entity entity)
    {
        if (!(entity instanceof EntityPlayerMP))
        {
            return false;
        }

        return !isFakePlayer(entity);
    }

    /**
     *
     * @param itemNames
     * @return
     */
    public static List<Pair<Float, ItemStack>> getItemsWeighted(List<String> itemNames)
    {
        List<Pair<Float, ItemStack>> items = new ArrayList<>();

        for (String json : itemNames)
        {
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(json);

            if (element.isJsonPrimitive())
            {
                String name = element.getAsString();
                Pair<Float, ItemStack> pair = ItemStackBuilder.parseStackWithFactor(name);

                if (pair.getValue().isEmpty())
                {
                    Log.writeDataToLogFile(2, "Unknown item '" + name + "'!");
                }
                else
                {
                    items.add(pair);
                }
            }
            else if (element.isJsonObject())
            {
                JsonObject obj = element.getAsJsonObject();
                Pair<Float, ItemStack> pair = ItemStackBuilder.parseStackWithFactor(obj);

                if (pair != null)
                {
                    items.add(pair);
                }
            }
            else
            {
                Log.writeDataToLogFile(2, "Item description '" + json + "' is not valid!");
            }
        }

        return items;
    }

    /**
     *
     * @param items
     * @param total
     * @return
     */
    public static ItemStack getRandomItem(List<Pair<Float, ItemStack>> items, float total)
    {
        float random = new Random().nextFloat() * total;

        for (Pair<Float, ItemStack> pair : items)
        {
            if (random <= pair.getLeft())
            {
                return pair.getRight().copy();
            }

            random -= pair.getLeft();
        }

        return ItemStack.EMPTY;
    }

    /**
     *
     * @param items
     * @return
     */
    public static float getTotal(List<Pair<Float, ItemStack>> items)
    {
        float total = 0.0f;

        for (Pair<Float, ItemStack> pair : items)
        {
            total += pair.getLeft();
        }

        return total;
    }
}
