package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.bridge;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: JEIBridge.java */
/* loaded from: input.jar:cad97/spawnercraft/bridge/NBTPreservingShapedRecipeWrapper.class */
public class NBTPreservingShapedRecipeWrapper extends BlankRecipeWrapper {
    private final List<ItemStack> input;
    private final ItemStack output;

    /* JADX INFO: Access modifiers changed from: package-private */
    public NBTPreservingShapedRecipeWrapper(ResourceLocation variant, List<ItemStack> input, ItemStack output) {
        this.input = (List) input.stream().map(itemStack -> {
            ItemStack copy = itemStack.func_77946_l();
            ItemMobSoul.applyEntityIdToItemStack(copy, variant);
            return copy;
        }).collect(Collectors.toList());
        this.output = output.func_77946_l();
        ItemMobSoul.applyEntityIdToItemStack(this.output, variant);
    }

    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, this.input);
        ingredients.setOutput(ItemStack.class, this.output);
    }
}
