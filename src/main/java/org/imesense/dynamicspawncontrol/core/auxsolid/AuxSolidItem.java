package org.imesense.dynamicspawncontrol.core.auxsolid;

import com.google.common.base.Predicate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import org.imesense.dynamicspawncontrol.core.builder.ItemStackBuilder;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.util.ArrayList;
import java.util.List;

import static org.imesense.dynamicspawncontrol.core.auxsolid.Block.*;
import static org.imesense.dynamicspawncontrol.core.auxsolid.Math.*;

/**
 *
 */
public final class AuxSolidItem
{
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
        net.minecraft.item.Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));

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
}
