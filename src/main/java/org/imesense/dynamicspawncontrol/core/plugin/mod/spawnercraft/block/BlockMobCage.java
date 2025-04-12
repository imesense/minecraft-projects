package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.block;

import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;

public final class BlockMobCage extends SpawnerCraftBlock
{
    @Override
    @Nonnull
    public Block setUnlocalizedName(@Nonnull String string)
    {
        return super.setUnlocalizedName(string);
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

    public boolean isOpaqueCube(IBlockState iBlockState)
    {
        return false;
    }

    @Nonnull
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
}

