package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.items;

import javax.annotation.Nonnull;
import net.minecraft.item.Item;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.creativetabs.SpawnerCraftTabs;

/* loaded from: input.jar:cad97/spawnercraft/items/SpawnerCraftItem.class */
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
        return super.setUnlocalizedName("spawnercraft." + string);
    }
}
