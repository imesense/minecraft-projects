package org.imesense.dynamicspawncontrol.technical.customlibrary;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public final class ItemStackBuilder
{
    /**
     *
     * @param name
     * @return
     */
    public static Pair<Float, ItemStack> parseStackWithFactor(String name)
    {
        int i = 0;

        while (i < name.length() && (Character.isDigit(name.charAt(i)) || name.charAt(i) == '.'))
        {
            i++;
        }

        if (i < name.length() && name.charAt(i) == '=')
        {
            String f = name.substring(0, i);
            float v;

            try
            {
                v = Float.parseFloat(f);
            }
            catch (NumberFormatException exception)
            {
                v = 1.0f;
            }

            return Pair.of(v, parseStack(name.substring(i+1)));
        }

        return Pair.of(1.0f, parseStack(name));
    }

    /**
     *
     * @param jsonObject
     * @return
     */
    public static Pair<Float, ItemStack> parseStackWithFactor(JsonObject jsonObject)
    {
        float factor = 1.0f;

        if (jsonObject.has("factor"))
        {
            factor = jsonObject.get("factor").getAsFloat();
        }

        ItemStack stack = parseStack(jsonObject);

        if (stack == null)
        {
            return null;
        }

        return Pair.of(factor, stack);
    }

    /**
     *
     * @param name
     * @return
     */
    @Nonnull
    public static ItemStack parseStack(String name)
    {
        if (name.contains("/"))
        {
            String[] split = StringUtils.split(name, "/");
            ItemStack stack = parseStackNoNBT(split[0]);

            if (stack.isEmpty())
            {
                return stack;
            }

            NBTTagCompound nbt;

            try
            {
                nbt = JsonToNBT.getTagFromJson(split[1]);
            }
            catch (NBTException exception)
            {
                Log.writeDataToLogFile(2, "Error parsing NBT in '" + name + "'!");
                return ItemStack.EMPTY;
            }

            stack.setTagCompound(nbt);
            return stack;
        }
        else
        {
            return parseStackNoNBT(name);
        }
    }

    /**
     *
     * @param jsonObject
     * @return
     */
    @Nullable
    public static ItemStack parseStack(JsonObject jsonObject)
    {
        if (jsonObject.has("empty"))
        {
            return ItemStack.EMPTY;
        }

        String name = jsonObject.get("item").getAsString();
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));

        if (item == null)
        {
            Log.writeDataToLogFile(2, "Unknown item '" + name + "'!");
            return null;
        }

        ItemStack stack = new ItemStack(item);

        if (jsonObject.has("damage"))
        {
            stack.setItemDamage(jsonObject.get("damage").getAsInt());
        }

        if (jsonObject.has("count"))
        {
            stack.setCount(jsonObject.get("count").getAsInt());
        }

        if (jsonObject.has("nbt"))
        {
            String nbt = jsonObject.get("nbt").toString();
            NBTTagCompound tag;

            try
            {
                tag = JsonToNBT.getTagFromJson(nbt);
            }
            catch (NBTException exception)
            {
                Log.writeDataToLogFile(2, "Error parsing json '" + nbt + "'!");
                return ItemStack.EMPTY;
            }

            stack.setTagCompound(tag);
        }

        return stack;
    }

    /**
     *
     * @param name
     * @return
     */
    private static ItemStack parseStackNoNBT(String name)
    {
        if (name.contains("@"))
        {
            String[] split = StringUtils.split(name, "@");
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(split[0]));

            if (item == null)
            {
                return ItemStack.EMPTY;
            }

            int meta;

            try
            {
                meta = Integer.parseInt(split[1]);
            }
            catch (NumberFormatException exception)
            {
                Log.writeDataToLogFile(2, "Unknown item '" + name + "'!");
                return ItemStack.EMPTY;
            }

            return new ItemStack(item, 1, meta);
        }
        else
        {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));

            if (item == null)
            {
                return ItemStack.EMPTY;
            }

            return new ItemStack(item);
        }
    }
}
