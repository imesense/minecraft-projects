package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.block;

import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;

/* loaded from: input.jar:cad97/spawnercraft/block/BlockMobCage.class */
public class BlockMobCage extends SpawnerCraftBlock
{
    @Override // cad97.spawnercraft.block.SpawnerCraftBlock
    @Nonnull
    public /* bridge */ /* synthetic */ Block setUnlocalizedName(@Nonnull String str)
    {
        return super.setUnlocalizedName(str);
    }

    public BlockMobCage()
    {
        super(Material.ROCK);
        this.blockHardness = 5.0f;
        this.blockSoundType = SoundType.METAL;
        setUnlocalizedName("mob_cage");
        setRegistryName(DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID, "mob_cage");
        setHarvestLevel("pickaxe", Item.ToolMaterial.STONE.getHarvestLevel());
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Nonnull
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
}

