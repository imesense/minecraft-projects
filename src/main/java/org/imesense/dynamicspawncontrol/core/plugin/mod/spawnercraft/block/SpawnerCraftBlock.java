package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.block;

import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.creativetabs.SpawnerCraftTabs;

public abstract class SpawnerCraftBlock extends Block
{
    /* JADX INFO: Access modifiers changed from: package-private */
    public SpawnerCraftBlock(Material material)
    {
        super(material);
        setCreativeTab(SpawnerCraftTabs.tab);
    }

    @Nonnull
    public Block setUnlocalizedName(@Nonnull String string)
    {
        return super.setUnlocalizedName("dynamicspawncontrol." + string);
    }
}

