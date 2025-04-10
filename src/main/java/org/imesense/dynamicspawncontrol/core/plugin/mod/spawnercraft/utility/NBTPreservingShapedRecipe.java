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
public class NBTPreservingShapedRecipe extends ShapedRecipes {
    private NBTTagCompound matchingCompound;

    static {
        RecipeSorter.register("spawnercraft:nbtshaped", NBTPreservingShapedRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
    }

    public NBTPreservingShapedRecipe(String group, int width, int height, NonNullList<Ingredient> items, ItemStack output) {
        super(group, width, height, items, output);
        this.matchingCompound = null;
    }

    @Nonnull
    public ItemStack func_77571_b() {
        ItemStack output = super.func_77571_b();
        output.func_77982_d(this.matchingCompound);
        return output;
    }

    public boolean func_77569_a(@Nonnull InventoryCrafting inv, World worldIn) {
        this.matchingCompound = null;
        int i = 0;
        while (true) {
            if (i >= inv.func_70302_i_()) {
                break;
            }
            ItemStack itemStack = inv.func_70301_a(i);
            if (itemStack.func_190926_b()) {
                i++;
            } else {
                this.matchingCompound = itemStack.func_77978_p();
                break;
            }
        }
        for (int i2 = 0; i2 < inv.func_70302_i_(); i2++) {
            ItemStack itemStack2 = inv.func_70301_a(i2);
            if (!itemStack2.func_190926_b() && !Objects.equal(itemStack2.func_77978_p(), this.matchingCompound)) {
                return false;
            }
        }
        return super.func_77569_a(inv, worldIn);
    }
}
