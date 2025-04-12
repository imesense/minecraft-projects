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

public final class NBTPreservingShapedRecipe extends ShapedRecipes
{
    private NBTTagCompound matchingCompound;

    static
    {
        RecipeSorter.register("dynamicspawncontrol:nbtshaped",
                NBTPreservingShapedRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
    }

    public NBTPreservingShapedRecipe(String group, int width, int height,
                                     NonNullList<Ingredient> ingredients, ItemStack itemStack)
    {
        super(group, width, height, ingredients, itemStack);
        this.matchingCompound = null;
    }

    @Nonnull
    public ItemStack getRecipeOutput()
    {
        ItemStack output = super.getRecipeOutput();
        output.setTagCompound(this.matchingCompound);

        return output;
    }

    public boolean matches(@Nonnull InventoryCrafting inventoryCrafting, World world)
    {
        int i = 0;
        this.matchingCompound = null;

        while (true)
        {
            if (i >= inventoryCrafting.getSizeInventory())
            {
                break;
            }

            ItemStack itemStack = inventoryCrafting.getStackInSlot(i);

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

        for (int i2 = 0; i2 < inventoryCrafting.getSizeInventory(); i2++)
        {
            ItemStack itemStack2 = inventoryCrafting.getStackInSlot(i2);

            if (!itemStack2.isEmpty() && !Objects.equal(itemStack2.getTagCompound(), this.matchingCompound))
            {
                return false;
            }
        }

        return super.matches(inventoryCrafting, world);
    }
}
