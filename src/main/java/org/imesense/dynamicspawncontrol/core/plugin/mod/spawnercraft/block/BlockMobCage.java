package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.block;

import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
/* loaded from: input.jar:cad97/spawnercraft/block/BlockMobCage.class */
public class BlockMobCage extends SpawnerCraftBlock {
    @Override // cad97.spawnercraft.block.SpawnerCraftBlock
    @Nonnull
    public /* bridge */ /* synthetic */ Block func_149663_c(@Nonnull String str) {
        return super.func_149663_c(str);
    }

    public BlockMobCage() {
        super(Material.field_151576_e);
        this.field_149782_v = 5.0f;
        this.field_149762_H = SoundType.field_185852_e;
        func_149663_c("mob_cage");
        setRegistryName(SpawnerCraft.MOD_ID, "mob_cage");
        setHarvestLevel("pickaxe", Item.ToolMaterial.STONE.func_77996_d());
    }

    public boolean func_149662_c(IBlockState state) {
        return false;
    }

    @Nonnull
    public BlockRenderLayer func_180664_k() {
        return BlockRenderLayer.CUTOUT;
    }
}

