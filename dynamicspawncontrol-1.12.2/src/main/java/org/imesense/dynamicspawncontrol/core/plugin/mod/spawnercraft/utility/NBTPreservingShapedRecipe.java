package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.utility;

import com.google.common.base.Objects;
import javax.annotation.Nonnull;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.RecipeSorter;

/* loaded from: input.jar:cad97/spawnercraft/utility/NBTPreservingShapedRecipe.class */
public class NBTPreservingShapedRecipe extends ShapedRecipes
{
    private NBTTagCompound matchingCompound;

    static
    {
        RecipeSorter.register("spawnercraft:nbtshaped",
                NBTPreservingShapedRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
    }

    public NBTPreservingShapedRecipe(String group, int width, int height, NonNullList<Ingredient> items, ItemStack output)
    {
        super(group, width, height, items, output);
        this.matchingCompound = null;
    }

    @Nonnull
    public ItemStack getRecipeOutput()
    {
        ItemStack output = super.getRecipeOutput();
        output.setTagCompound(this.matchingCompound);
        return output;
    }

    public boolean matches(@Nonnull InventoryCrafting inv, World worldIn)
    {
        this.matchingCompound = null;
        int i = 0;
        while (true)
        {
            if (i >= inv.getSizeInventory())
            {
                break;
            }

            ItemStack itemStack = inv.getStackInSlot(i);

            if (itemStack.isEmpty())
            {
                i++;
            }
            else
            {
                this.matchingCompound = itemStack.getTagCompound();
                break;
            }
        }

        for (int i2 = 0; i2 < inv.getSizeInventory(); i2++)
        {
            ItemStack itemStack2 = inv.getStackInSlot(i2);

            if (!itemStack2.isEmpty() && !Objects.equal(itemStack2.getTagCompound(), this.matchingCompound))
            {
                return false;
            }
        }

        return super.matches(inv, worldIn);
    }
}
