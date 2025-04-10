package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
/* loaded from: input.jar:cad97/spawnercraft/init/SpawnerCraftRecipes.class */
public class SpawnerCraftRecipes {
    public static void registerRecipes() {
        Ingredient essence = Ingredient.func_193367_a(SpawnerCraftItems.MOB_ESSENCE);
        ForgeRegistries.RECIPES.register(new NBTPreservingShapedRecipe(SpawnerCraft.MOD_ID, 2, 2, NonNullList.func_191197_a(4, essence), new ItemStack(SpawnerCraftItems.MOB_AGGLOMERATION)).setRegistryName("craft_mob_agglomeration"));
        Ingredient agglomeration = Ingredient.func_193367_a(SpawnerCraftItems.MOB_AGGLOMERATION);
        ForgeRegistries.RECIPES.register(new NBTPreservingShapedRecipe(SpawnerCraft.MOD_ID, 2, 2, NonNullList.func_191197_a(4, agglomeration), new ItemStack(SpawnerCraftItems.MOB_SPIRIT)).setRegistryName("craft_mob_spirit"));
        if (ConfigHandler.spawnerCraftable) {
            GameRegistry.addShapedRecipe(new ResourceLocation(SpawnerCraft.MOD_ID, "craft_mob_cage"), (ResourceLocation) null, new ItemStack(SpawnerCraftBlocks.MOB_CAGE), new Object[]{"III", "I I", "III", 'I', new ItemStack(Blocks.field_150411_aY)});
        }
        GameRegistry.addShapedRecipe(new ResourceLocation(SpawnerCraft.MOD_ID, "craft_mob_rod"), (ResourceLocation) null, new ItemStack(SpawnerCraftItems.MOB_ROD), new Object[]{"F", "S", 'F', new ItemStack(Items.field_151112_aM), 'S', new ItemStack(SpawnerCraftBlocks.MOB_CAGE)});
        LogHelper.logInfo("Recipes registered.");
    }
}
