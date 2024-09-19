package org.imesense.dynamicspawncontrol.technical.customlibrary;

/**
 *
 */
public class ItemStackBuilder
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
            catch (NumberFormatException e)
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
            catch (NBTException e)
            {
                Log.writeDataToLogFile(Log._typeLog[2], "Error parsing NBT in '" + name + "'!");
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
            Log.writeDataToLogFile(Log._typeLog[2], "Unknown item '" + name + "'!");
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
            catch (NBTException e)
            {
                Log.writeDataToLogFile(Log._typeLog[2], "Error parsing json '" + nbt + "'!");
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
            catch (NumberFormatException e)
            {
                Log.writeDataToLogFile(Log._typeLog[2], "Unknown item '" + name + "'!");
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
