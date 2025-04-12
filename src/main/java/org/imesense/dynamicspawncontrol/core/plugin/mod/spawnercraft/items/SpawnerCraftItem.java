package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.items;

import javax.annotation.Nonnull;
import net.minecraft.item.Item;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.creativetabs.SpawnerCraftTabs;

public abstract class SpawnerCraftItem extends Item
{
    /* JADX INFO: Access modifiers changed from: package-private */
    public SpawnerCraftItem()
    {
        setCreativeTab(SpawnerCraftTabs.tab);
    }

    @Nonnull
    public Item setUnlocalizedName(@Nonnull String string)
    {
        return super.setUnlocalizedName("dynamicspawncontrol." + string);
    }
}
