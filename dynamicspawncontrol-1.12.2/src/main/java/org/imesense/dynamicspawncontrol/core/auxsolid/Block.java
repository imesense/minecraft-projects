package org.imesense.dynamicspawncontrol.core.auxsolid;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import org.imesense.dynamicspawncontrol.core.api.SignalDataAccessor;
import org.imesense.dynamicspawncontrol.core.collection.PositionEnum;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.raytrace.RayTrace;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import static org.imesense.dynamicspawncontrol.core.auxsolid.Math.*;
import static org.imesense.dynamicspawncontrol.core.auxsolid.AuxSolidItem.*;

/**
 *
 */
public final class Block
{
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
    public static boolean isMatchingOreDict(int oreId, net.minecraft.block.Block block)
    {
        ItemStack itemStack = new ItemStack(block);

        int[] oreIDs = itemStack.isEmpty() ? new int[0] :
                OreDictionary.getOreIDs(itemStack);

        return isMatchingOreId(oreIDs, oreId);
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
                net.minecraft.block.Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));

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
                net.minecraft.block.Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));

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

        if (jsonObject.has(PositionEnum.BlockProperties.OFFSET.getValue()))
        {
            JsonObject jsonObject1 =
                    jsonObject.getAsJsonObject(PositionEnum.BlockProperties.OFFSET.getValue());

            offsetX = jsonObject1.has(PositionEnum.Coordinates.X.getValue()) ?
                    jsonObject1.get(PositionEnum.Coordinates.X.getValue()).getAsInt() : 0;

            offsetY = jsonObject1.has(PositionEnum.Coordinates.Y.getValue()) ?
                    jsonObject1.get(PositionEnum.Coordinates.Y.getValue()).getAsInt() : 0;

            offsetZ = jsonObject1.has(PositionEnum.Coordinates.Z.getValue()) ?
                    jsonObject1.get(PositionEnum.Coordinates.Z.getValue()).getAsInt() : 0;
        }
        else
        {
            offsetX = 0;
            offsetY = 0;
            offsetZ = 0;
        }

        if (jsonObject.has(PositionEnum.BlockProperties.STEP.getValue()))
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

        if (jsonObject.has(PositionEnum.BlockProperties.LOOK.getValue()))
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
}
