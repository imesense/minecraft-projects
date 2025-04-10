package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.items;

import javax.annotation.Nonnull;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
/* loaded from: input.jar:cad97/spawnercraft/items/ItemMobRod.class */
public class ItemMobRod extends ItemSword {
    public ItemMobRod() {
        super(Item.ToolMaterial.IRON);
        func_77637_a(SpawnerCraftTabs.tab);
        func_77655_b("mob_rod");
        setRegistryName(SpawnerCraft.MOD_ID, "mob_rod");
    }

    @Nonnull
    public Item func_77655_b(@Nonnull String name) {
        return super.func_77655_b("spawnercraft." + name);
    }
}

