package org.imesense.dynamicspawncontrol.lootboxgenerator;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.imesense.dynamicspawncontrol.core.script.parser.ParserEventLootBoxInWorld;

import java.util.List;
import java.util.Random;

public final class LootBoxInWorld implements IWorldGenerator
{
    private final String[] TIERS = {"common", "rare", "legendary"};

    @Override
    public void generate(Random random, int chunkX, int chunkZ,
                         World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if (world.provider.getDimension() != 0)
        {
            return;
        }

        if (random.nextFloat() < 0.1)
        {
            int x = chunkX * 16 + random.nextInt(16);
            int z = chunkZ * 16 + random.nextInt(16);
            int y = world.getHeight(x, z);

            if (world.isAirBlock(new BlockPos(x, y - 1, z)))
            {
                return;
            }

            world.setBlockState(new BlockPos(x, y, z), Blocks.CHEST.getDefaultState(), 2);

            TileEntityChest chest = (TileEntityChest) world.getTileEntity(new BlockPos(x, y, z));

            if (chest != null)
            {
                addLootToChest(chest, random);
            }
        }
    }

    private void addLootToChest(TileEntityChest chest, Random random)
    {
        if (ParserEventLootBoxInWorld.instance.getLootTable() == null ||
                ParserEventLootBoxInWorld.instance.getLootTable().isEmpty())
        {
            return;
        }

        String selectedTier = TIERS[random.nextInt(TIERS.length)];
        List<String> items = ParserEventLootBoxInWorld.instance.getLootTable().get(selectedTier);

        if (items == null || items.isEmpty())
        {
            return;
        }

        int itemCount = random.nextInt(5) + 1;

        for (int i = 0; i < itemCount; i++)
        {
            String itemName = items.get(random.nextInt(items.size()));
            Item item = Item.getByNameOrId(itemName);

            if (item == null)
            {
                continue;
            }

            ItemStack stack = new ItemStack(item, 1);

            int slot = random.nextInt(chest.getSizeInventory());
            chest.setInventorySlotContents(slot, stack);
        }
    }
}