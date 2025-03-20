package org.imesense.dynamicspawncontrol.worldgenerator;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.data.LootBoxGeneratorLVL;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.storage.GeneralLootBoxGeneratorLVL;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockLootBox implements IWorldGenerator
{
    public BlockLootBox()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world,
                         IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if (world.provider.getDimension() == 0)
        {
            generateSurface(world, random, chunkX * 16, chunkZ * 16);
        }
    }

    private void generateSurface(World world, Random random, int x, int z)
    {
        Map<String, LootBoxGeneratorLVL.Data> lootBoxDataMap =
                GeneralLootBoxGeneratorLVL.getInstance().lootBoxGeneratorLVLData;

        if (lootBoxDataMap.isEmpty())
        {
            return;
        }

        String[] chestLevels = lootBoxDataMap.keySet().toArray(new String[0]);
        String randomChestLevel = chestLevels[random.nextInt(chestLevels.length)];
        LootBoxGeneratorLVL.Data lootBoxData = lootBoxDataMap.get(randomChestLevel);

        if (random.nextDouble() > lootBoxData.spawnChance)
        {
            return;
        }

        int chestX = x + random.nextInt(16);
        int chestY = lootBoxData.minHeight + random.nextInt(lootBoxData.maxHeight - lootBoxData.minHeight + 1);
        int chestZ = z + random.nextInt(16);

        BlockPos pos = new BlockPos(chestX, chestY, chestZ);

        if (world.isAirBlock(pos) && world.getBlockState(pos.down()).isTopSolid())
        {
            world.setBlockState(pos, Blocks.CHEST.getDefaultState());
            TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);

            if (chest != null)
            {
                List<ItemStack> items = lootBoxData.items;

                for (ItemStack stack : items)
                {
                    chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), stack.copy());
                }
            }
        }
    }
}
