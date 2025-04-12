package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.init;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.block.BlockMobCage;

public final class SpawnerCraftBlocks
{
    public static final Block MOB_CAGE = new BlockMobCage();

    public SpawnerCraftBlocks()
    {

    }

    public static void registerBlocks()
    {
        ForgeRegistries.BLOCKS.register(MOB_CAGE);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels()
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MOB_CAGE),
                0, new ModelResourceLocation(MOB_CAGE.getRegistryName(), "inventory"));
    }
}

