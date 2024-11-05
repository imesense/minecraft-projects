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
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.technical.customlibrary.enumeration.EnumGameProperty;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataAccessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 *
 */
public class AuxFunction
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
    public static final Map<String, DamageSource> DAMAGE_MAP = new HashMap<>();

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
            DAMAGE_MAP.put(source.getDamageType(), source);
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
        ItemStack itemStack = new ItemStack(block);

        int[] oreIDs = itemStack.isEmpty() ? new int[0] :
                OreDictionary.getOreIDs(itemStack);

        return isMatchingOreId(oreIDs, oreId);
    }

    /**
     *
     * @param iBlockState
     * @param iProperty
     * @param value
     * @return
     * @param <T>
     */
    public static <T extends Comparable<T>> IBlockState set(IBlockState iBlockState, IProperty<T> iProperty, String value)
    {
        Optional<T> optional = iProperty.parseValue(value);

        if (optional.isPresent())
        {
            return iBlockState.withProperty(iProperty, optional.get());
        }
        else
        {
            return iBlockState;
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

        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        if (jsonObject.has(EnumGameProperty.BlockProperties.OFFSET.getValue()))
        {
            JsonObject jsonObject1 =
                    jsonObject.getAsJsonObject(EnumGameProperty.BlockProperties.OFFSET.getValue());

            offsetX = jsonObject1.has(EnumGameProperty.Coordinates.X.getValue()) ?
                    jsonObject1.get(EnumGameProperty.Coordinates.X.getValue()).getAsInt() : 0;

            offsetY = jsonObject1.has(EnumGameProperty.Coordinates.Y.getValue()) ?
                    jsonObject1.get(EnumGameProperty.Coordinates.Y.getValue()).getAsInt() : 0;

            offsetZ = jsonObject1.has(EnumGameProperty.Coordinates.Z.getValue()) ?
                    jsonObject1.get(EnumGameProperty.Coordinates.Z.getValue()).getAsInt() : 0;
        }
        else
        {
            offsetX = 0;
            offsetY = 0;
            offsetZ = 0;
        }

        if (jsonObject.has(EnumGameProperty.BlockProperties.STEP.getValue()))
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

        if (jsonObject.has(EnumGameProperty.BlockProperties.LOOK.getValue()))
        {
            return (event, query) ->
            {
                RayTraceResult rayTraceResult =
                        RayTrace.getMovingObjectPositionFromPlayer
                                (query.getWorld(event), query.getPlayer(event), false);

                if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK)
                {
                    return rayTraceResult.getBlockPos().add(offsetX, offsetY, offsetZ);
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
            Predicate<ItemStack> itemStackPredicate = getMatcher(itemObj.getAsJsonObject());

            if (itemStackPredicate != null)
            {
                items.add(itemStackPredicate);
            }
        }
        else if (itemObj.isJsonArray())
        {
            for (JsonElement jsonElement : itemObj.getAsJsonArray())
            {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Predicate<ItemStack> itemStackPredicate = getMatcher(jsonObject);

                if (itemStackPredicate != null)
                {
                    items.add(itemStackPredicate);
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
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(json);

            if (jsonElement.isJsonPrimitive())
            {
                String name = jsonElement.getAsString();
                Predicate<ItemStack> itemStackPredicate = getMatcher(name);

                if (itemStackPredicate != null)
                {
                    items.add(itemStackPredicate);
                }

            }
            else if (jsonElement.isJsonObject())
            {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Predicate<ItemStack> itemStackPredicate = getMatcher(jsonObject);

                if (itemStackPredicate != null)
                {
                    items.add(itemStackPredicate);
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
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);

        if (jsonElement.isJsonPrimitive())
        {
            String blockName = jsonElement.getAsString();

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
        else if (jsonElement.isJsonObject())
        {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            BiPredicate<World, BlockPos> blockPosBiPredicate;

            if (jsonObject.has("ore"))
            {
                int oreId = OreDictionary.getOreID(jsonObject.get("ore").getAsString());
                blockPosBiPredicate = (world, pos) -> isMatchingOreDict(oreId, world.getBlockState(pos).getBlock());
            }
            else if (jsonObject.has("block"))
            {
                String blockName = jsonObject.get("block").getAsString();
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));

                if (block == null)
                {
                    Log.writeDataToLogFile(2, "Block '" + blockName + "' is not valid!");
                    return null;
                }

                if (jsonObject.has("properties"))
                {
                    IBlockState iBlockState = block.getDefaultState();
                    JsonArray jsonArray = jsonObject.get("properties").getAsJsonArray();

                    for (JsonElement jsonElement1 : jsonArray)
                    {
                        JsonObject jsonObject1 = jsonElement1.getAsJsonObject();

                        String name = jsonObject1.get("name").getAsString();
                        String value = jsonObject1.get("value").getAsString();

                        for (IProperty<?> iProperty : iBlockState.getPropertyKeys())
                        {
                            if (name.equals(iProperty.getName()))
                            {
                                iBlockState = set(iBlockState, iProperty, value);
                            }
                        }
                    }

                    IBlockState iBlockState1 = iBlockState;
                    blockPosBiPredicate = (world, pos) -> world.getBlockState(pos) == iBlockState1;
                }
                else
                {
                    blockPosBiPredicate = (world, pos) -> world.getBlockState(pos).getBlock() == block;
                }
            }
            else
            {
                blockPosBiPredicate = (world, pos) -> true;
            }

            if (jsonObject.has("mod"))
            {
                String mod = jsonObject.get("mod").getAsString();
                BiPredicate<World, BlockPos> blockPosBiPredicate1 = blockPosBiPredicate;

                blockPosBiPredicate = (world, pos) -> blockPosBiPredicate1.test(world, pos) &&
                        mod.equals(world.getBlockState(pos).getBlock().getRegistryName().getResourceDomain());
            }

            if (jsonObject.has("energy"))
            {
                Predicate<Integer> integerPredicate = getExpression(jsonObject.get("energy"));

                if (integerPredicate != null)
                {
                    EnumFacing enumFacing;

                    if (jsonObject.has("side"))
                    {
                        enumFacing =
                                EnumFacing.byName(jsonObject.get("side").getAsString().toLowerCase());
                    }
                    else
                    {
                        enumFacing = null;
                    }

                    BiPredicate<World, BlockPos> blockPosBiPredicate1 = blockPosBiPredicate;

                    blockPosBiPredicate = (world, pos) -> blockPosBiPredicate1.test(world, pos) &&
                            integerPredicate.test(getEnergy(world, pos, enumFacing));
                }
            }

            if (jsonObject.has("contains"))
            {
                EnumFacing enumFacing;

                if (jsonObject.has("side"))
                {
                    enumFacing =
                            EnumFacing.byName(jsonObject.get("energyside").getAsString().toLowerCase());
                }
                else
                {
                    enumFacing = null;
                }

                List<Predicate<ItemStack>> items = getItems(jsonObject.get("contains"));
                BiPredicate<World, BlockPos> blockPosBiPredicate1 = blockPosBiPredicate;

                blockPosBiPredicate = (world, pos) -> blockPosBiPredicate1.test(world, pos) && contains(world, pos, enumFacing, items);
            }

            return blockPosBiPredicate;
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
     * @param jsonElement
     * @return
     */
    public static Predicate<Integer> getExpression(JsonElement jsonElement)
    {
        if (jsonElement.isJsonPrimitive())
        {
            if (jsonElement.getAsJsonPrimitive().isNumber())
            {
                int amount = jsonElement.getAsInt();

                return i -> i == amount;
            }
            else
            {
                return getExpression(jsonElement.getAsString());
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
        ItemStack itemStack = ItemStackBuilder.parseStack(name);

        if (!itemStack.isEmpty())
        {
            if (name.contains("/") && name.contains("@"))
            {
                return s -> ItemStack.areItemsEqual(s, itemStack) && ItemStack.areItemStackTagsEqual(s, itemStack);
            }
            else if (name.contains("/"))
            {
                return s -> ItemStack.areItemsEqualIgnoreDurability(s, itemStack) && ItemStack.areItemStackTagsEqual(s, itemStack);
            }
            else if (name.contains("@"))
            {
                return s -> ItemStack.areItemsEqual(s, itemStack);
            }
            else
            {
                return s -> s.getItem() == itemStack.getItem();
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

        Predicate<ItemStack> predicate;

        if (jsonObject.has("damage"))
        {
            Predicate<Integer> damage = getExpression(jsonObject.get("damage"));

            if (damage == null)
            {
                return null;
            }

            predicate = s -> s.getItem() == item && damage.test(s.getItemDamage());
        }
        else
        {
            predicate = s -> s.getItem() == item;
        }

        if (jsonObject.has("count"))
        {
            Predicate<Integer> count = getExpression(jsonObject.get("count"));

            if (count != null)
            {
                Predicate<ItemStack> predicate1 = predicate;
                predicate = s -> predicate1.test(s) && count.test(s.getCount());
            }
        }

        if (jsonObject.has("ore"))
        {
            int oreId = OreDictionary.getOreID(jsonObject.get("ore").getAsString());
            Predicate<ItemStack> predicate1 = predicate;

            predicate = s -> predicate1.test(s) && isMatchingOreId(s.isEmpty() ? new int[0] :
                    OreDictionary.getOreIDs(s), oreId);
        }

        if (jsonObject.has("mod"))
        {
            Predicate<ItemStack> predicate1 = predicate;
            predicate = s -> predicate1.test(s) && "mod".equals(s.getItem().getRegistryName().getResourceDomain());
        }

        if (jsonObject.has("nbt"))
        {
            List<Predicate<NBTTagCompound>> nbtMatchers = getNbtMatchers(jsonObject);

            if (nbtMatchers != null)
            {
                Predicate<ItemStack> predicate1 = predicate;
                predicate = s -> predicate1.test(s) && nbtMatchers.stream().allMatch(p -> p.test(s.getTagCompound()));
            }
        }

        if (jsonObject.has("energy"))
        {
            Predicate<Integer> energy = getExpression(jsonObject.get("energy"));

            if (energy != null)
            {
                Predicate<ItemStack> predicate1 = predicate;
                predicate = s -> predicate1.test(s) && energy.test(getEnergy(s));
            }
        }

        return predicate;
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
            IEnergyStorage iEnergyStorage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
            return iEnergyStorage.getEnergyStored();
        }

        return 0;
    }

    /**
     *
     * @param world
     * @param blockPos
     * @param enumFacing
     * @param predicateItemStack
     * @return
     */
    public static boolean contains(World world, BlockPos blockPos,
                                   @Nullable EnumFacing enumFacing, @Nonnull List<Predicate<ItemStack>> predicateItemStack)
    {
        TileEntity tileEntity = world.getTileEntity(blockPos);

        if (tileEntity != null && tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, enumFacing))
        {
            IItemHandler iItemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, enumFacing);

            for (int i = 0; i < Objects.requireNonNull(iItemHandler).getSlots() ; i++)
            {
                ItemStack itemStack = iItemHandler.getStackInSlot(i);

                if (!itemStack.isEmpty())
                {
                    for (Predicate<ItemStack> itemStackPredicate : predicateItemStack)
                    {
                        if (itemStackPredicate.test(itemStack))
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
     * @param enumFacing
     * @return
     */
    public static int getEnergy(World world, BlockPos blockPos, @Nullable EnumFacing enumFacing)
    {
        TileEntity tileEntity = world.getTileEntity(blockPos);

        if (tileEntity != null && tileEntity.hasCapability(CapabilityEnergy.ENERGY, enumFacing))
        {
            IEnergyStorage iEnergyStorage = tileEntity.getCapability(CapabilityEnergy.ENERGY, enumFacing);
            return iEnergyStorage.getEnergyStored();
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
        JsonArray jsonArray = jsonObject.getAsJsonArray("nbt");

        return getNbtMatchers(jsonArray);
    }

    /**
     *
     * @param jsonArray
     * @return
     */
    public static List<Predicate<NBTTagCompound>> getNbtMatchers(JsonArray jsonArray)
    {
        List<Predicate<NBTTagCompound>> nbtMatchers = new ArrayList<>();

        for (JsonElement element : jsonArray)
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
                        NBTTagList nbtTagList = tagCompound.getTagList(tag, Constants.NBT.TAG_COMPOUND);

                        for (NBTBase nbtBase : nbtTagList)
                        {
                            for (Predicate<NBTTagCompound> matcher : Objects.requireNonNull(subMatchers))
                            {
                                if (matcher.test((NBTTagCompound) nbtBase))
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
                Predicate<Integer> integerPredicate = getExpression(o.get("value"));

                if (integerPredicate == null)
                {
                    return null;
                }

                nbtMatchers.add(tagCompound -> integerPredicate.test(tagCompound.getInteger(tag)));
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
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(json);

            if (jsonElement.isJsonPrimitive())
            {
                String name = jsonElement.getAsString();
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
            else if (jsonElement.isJsonObject())
            {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Pair<Float, ItemStack> pair = ItemStackBuilder.parseStackWithFactor(jsonObject);

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
