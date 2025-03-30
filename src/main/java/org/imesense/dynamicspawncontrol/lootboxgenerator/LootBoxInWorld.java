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
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.script.parser.ParserEventLootBoxInWorld;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.data.LootBox;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.storage.GeneralLootBox;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.List;
import java.util.Map;
import java.util.Random;

@InitLog
public final class LootBoxInWorld implements IWorldGenerator
{
    private final String[] TIERS = {"common", "rare", "legendary"};

    public LootBoxInWorld()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

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

    private void addLootToChest(TileEntityChest tileEntityChest, Random random)
    {
        Map<String, List<LootBox.Data>> lootTable = GeneralLootBox.getInstance().lootTable;

        if (lootTable == null || lootTable.isEmpty())
        {
            return;
        }

        String selectedTier = TIERS[random.nextInt(TIERS.length)];
        List<LootBox.Data> entries = lootTable.get(selectedTier);

        if (entries == null || entries.isEmpty())
        {
            return;
        }

        for (LootBox.Data entry : entries)
        {
            if (random.nextFloat() > entry.getChance())
            {
                continue;
            }

            Item item = Item.getByNameOrId(entry.getItem());

            if (item == null)
            {
                continue;
            }

            int count = entry.getMinCount() +
                    random.nextInt(entry.getMaxCount() - entry.getMinCount() + 1);

            ItemStack stack = new ItemStack(item, count);

            int slot = random.nextInt(tileEntityChest.getSizeInventory());
            tileEntityChest.setInventorySlotContents(slot, stack);
        }
    }
}