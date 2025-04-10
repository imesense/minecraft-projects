package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.items;

import javax.annotation.Nonnull;
import net.minecraft.item.Item;
/* loaded from: input.jar:cad97/spawnercraft/items/SpawnerCraftItem.class */
abstract class SpawnerCraftItem extends Item {
    /* JADX INFO: Access modifiers changed from: package-private */
    public SpawnerCraftItem() {
        func_77637_a(SpawnerCraftTabs.tab);
    }

    @Nonnull
    public Item func_77655_b(@Nonnull String name) {
        return super.func_77655_b("spawnercraft." + name);
    }
}
