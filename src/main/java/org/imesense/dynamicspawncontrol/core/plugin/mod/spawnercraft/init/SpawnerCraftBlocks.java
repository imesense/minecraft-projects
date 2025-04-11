package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.init;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.block.BlockMobCage;

/* loaded from: input.jar:cad97/spawnercraft/init/SpawnerCraftBlocks.class */
public class SpawnerCraftBlocks
{
    public static final Block MOB_CAGE = new BlockMobCage();

    public static void registerBlocks()
    {
        ForgeRegistries.BLOCKS.register(MOB_CAGE);
        Log.writeDataToLogFile(0, "Blocks initialized.");
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels()
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MOB_CAGE),
                0, new ModelResourceLocation(MOB_CAGE.getRegistryName(), "inventory"));

        Log.writeDataToLogFile(0, "Block models initialized.");
    }
}

