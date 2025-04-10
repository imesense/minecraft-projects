package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.block;

import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
/* loaded from: input.jar:cad97/spawnercraft/block/SpawnerCraftBlock.class */
abstract class SpawnerCraftBlock extends Block {
    /* JADX INFO: Access modifiers changed from: package-private */
    public SpawnerCraftBlock(Material material) {
        super(material);
        func_149647_a(SpawnerCraftTabs.tab);
    }

    @Nonnull
    public Block func_149663_c(@Nonnull String name) {
        return super.func_149663_c("spawnercraft." + name);
    }
}

