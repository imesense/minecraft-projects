package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.creativetabs;

import javax.annotation.Nonnull;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class SpawnerCraftTabs
{
    public SpawnerCraftTabs()
    {

    }

    public static final CreativeTabs tab = new CreativeTabs("dynamicspawncontrol.tab")
    {
        @Nonnull
        public ItemStack getTabIconItem()
        {
            return new ItemStack(Item.getItemFromBlock(Blocks.MOB_SPAWNER));
        }
    };
}
