package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.creativetabs;

import javax.annotation.Nonnull;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
/* loaded from: input.jar:cad97/spawnercraft/creativetabs/SpawnerCraftTabs.class */
public class SpawnerCraftTabs {
    public static final CreativeTabs tab = new CreativeTabs("spawnercraft.tab") { // from class: cad97.spawnercraft.creativetabs.SpawnerCraftTabs.1
        @Nonnull
        public ItemStack func_78016_d() {
            return new ItemStack(Item.func_150898_a(Blocks.field_150474_ac));
        }
    };
}
