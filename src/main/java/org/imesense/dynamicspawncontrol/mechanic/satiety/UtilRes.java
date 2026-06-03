package org.imesense.dynamicspawncontrol.mechanic.satiety;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;

import java.util.Locale;

public class UtilRes
{
    public static final String RESOURCE = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID.toLowerCase(Locale.US);

    public static String resource(String res) {
        return String.format("%s:%s", RESOURCE, res);
    }

    public static ResourceLocation getResource(String res)
    {
        return new ResourceLocation(RESOURCE, res);
    }

    public static String prefix(String name)
    {
        return String.format("%s.%s", RESOURCE, name.toLowerCase(Locale.US));
    }

    public static ItemStack copyStackWithAmount(ItemStack stack, int amount)
    {
        if (stack.isEmpty())
        {
            return ItemStack.EMPTY;
        }

        ItemStack s2 = stack.copy();
        s2.setCount(amount);

        return s2;
    }
}
