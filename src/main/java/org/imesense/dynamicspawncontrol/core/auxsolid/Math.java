package org.imesense.dynamicspawncontrol.core.auxsolid;

import com.google.common.base.Predicate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.plexus.util.StringUtils;
import org.imesense.dynamicspawncontrol.core.builder.ItemStackBuilder;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public final class Math
{
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
     * @param items
     * @param total
     * @return
     */
    public static ItemStack getRandomItem(List<Pair<Float, ItemStack>> items, float total)
    {
        float random = UniqueField.RANDOM.nextFloat() * total;

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
     * @param jsonObject
     * @return
     */
    public static List<Predicate<NBTTagCompound>> getNbtMatchers(JsonObject jsonObject)
    {
        JsonArray jsonArray = jsonObject.getAsJsonArray("nbt");

        return getNbtMatchers(jsonArray);
    }
}
