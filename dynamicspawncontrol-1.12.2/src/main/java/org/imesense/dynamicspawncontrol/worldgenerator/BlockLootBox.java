package org.imesense.dynamicspawncontrol.worldgenerator;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

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
        int chestX = x + random.nextInt(16);
        int chestY = 1 + random.nextInt(255);
        int chestZ = z + random.nextInt(16);

        BlockPos pos = new BlockPos(chestX, chestY, chestZ);

        if (world.isAirBlock(pos) && world.getBlockState(pos.down()).isTopSolid())
        {
            world.setBlockState(pos, Blocks.CHEST.getDefaultState());
            TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);

            if (chest != null)
            {
                for (int i = 0; i < 5 + random.nextInt(10); i++)
                {
                    ItemStack stack = getRandomItem(random);
                    chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), stack);
                }
            }
        }
    }

    private ItemStack getRandomItem(Random random)
    {
        Item[] items = {Items.DIAMOND, Items.GOLD_INGOT, Items.IRON_INGOT, Items.APPLE, Items.BREAD};
        Item item = items[random.nextInt(items.length)];

        return new ItemStack(item, 1 + random.nextInt(64));
    }
}
