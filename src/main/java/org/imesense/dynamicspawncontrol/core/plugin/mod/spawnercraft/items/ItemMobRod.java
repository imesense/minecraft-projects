package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.items;

import javax.annotation.Nonnull;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.creativetabs.SpawnerCraftTabs;

public final class ItemMobRod extends ItemSword
{
    public ItemMobRod()
    {
        super(Item.ToolMaterial.IRON);
        setCreativeTab(SpawnerCraftTabs.tab);
        setUnlocalizedName("mob_rod");
        setRegistryName(DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID, "mob_rod");
    }

    @Nonnull
    public Item setUnlocalizedName(@Nonnull String string)
    {
        return super.setUnlocalizedName("spawnercraft." + string);
    }
}

