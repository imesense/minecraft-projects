package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.items;

import javax.annotation.Nonnull;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.creativetabs.SpawnerCraftTabs;

/* loaded from: input.jar:cad97/spawnercraft/items/ItemMobRod.class */
public class ItemMobRod extends ItemSword {
    public ItemMobRod() {
        super(Item.ToolMaterial.IRON);
        setCreativeTab(SpawnerCraftTabs.tab);
        setUnlocalizedName("mob_rod");
        setRegistryName(SpawnerCraft.MOD_ID, "mob_rod");
    }

    @Nonnull
    public Item setUnlocalizedName(@Nonnull String name) {
        return super.setUnlocalizedName("spawnercraft." + name);
    }
}

