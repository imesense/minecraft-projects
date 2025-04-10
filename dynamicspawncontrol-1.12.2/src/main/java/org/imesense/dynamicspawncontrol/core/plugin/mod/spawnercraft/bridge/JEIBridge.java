package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.bridge;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
@JEIPlugin
/* loaded from: input.jar:cad97/spawnercraft/bridge/JEIBridge.class */
public class JEIBridge implements IModPlugin {
    public void registerItemSubtypes(@Nonnull ISubtypeRegistry subtypeRegistry) {
        subtypeRegistry.useNbtForSubtypes(new Item[]{SpawnerCraftItems.MOB_AGGLOMERATION, SpawnerCraftItems.MOB_ESSENCE, SpawnerCraftItems.MOB_SPIRIT});
    }

    public void register(@Nonnull IModRegistry registry) {
        registry.addRecipes((Collection) EntityList.func_180124_b().stream().flatMap(resourceLocation -> {
            return Stream.of((Object[]) new NBTPreservingShapedRecipeWrapper[]{new NBTPreservingShapedRecipeWrapper(resourceLocation, Arrays.asList(new ItemStack(SpawnerCraftItems.MOB_ESSENCE), new ItemStack(SpawnerCraftItems.MOB_ESSENCE), new ItemStack(SpawnerCraftItems.MOB_ESSENCE), new ItemStack(SpawnerCraftItems.MOB_ESSENCE)), new ItemStack(SpawnerCraftItems.MOB_AGGLOMERATION)), new NBTPreservingShapedRecipeWrapper(resourceLocation, Arrays.asList(new ItemStack(SpawnerCraftItems.MOB_AGGLOMERATION), new ItemStack(SpawnerCraftItems.MOB_AGGLOMERATION), new ItemStack(SpawnerCraftItems.MOB_AGGLOMERATION), new ItemStack(SpawnerCraftItems.MOB_AGGLOMERATION)), new ItemStack(SpawnerCraftItems.MOB_SPIRIT))});
        }).collect(Collectors.toList()), "minecraft.crafting");
    }
}
