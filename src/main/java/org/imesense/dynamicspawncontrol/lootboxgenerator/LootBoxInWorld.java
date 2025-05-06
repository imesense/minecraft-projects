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
import org.imesense.dynamicspawncontrol.core.config.debug.DebugConfig;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.parser.ParserEventLootBoxInWorld;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.data.LootBox;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.storage.GeneralLootBox;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.*;

@InitLog
public final class LootBoxInWorld implements IWorldGenerator
{
    private static final double OVERALL_CHANCE_PER_CHUNK = 0.05;

    private static final double[] TIER_WEIGHTS = {60.0, 30.0, 10.0};
    private static final String[] TIERS = {"common", "rare", "legendary"};

    private static final boolean DEBUG =
            DebugConfig.getInstance(DebugConfig.class).isShowLoggingInEventLootBoxInWorld();

    public LootBoxInWorld()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ,
                         World world, IChunkGenerator iChunkGenerator, IChunkProvider iChunkProvider)
    {
        if (world.provider.getDimension() != 0)
        {
            if (DEBUG)
            {
                Log.write(1, "Skipping non-overworld dimension");
            }

            return;
        }

        if (random.nextDouble() >= OVERALL_CHANCE_PER_CHUNK)
        {
            if (DEBUG)
            {
                Log.write(2, "No chest spawned in this chunk (failed overall chance)");
            }

            return;
        }

        int tierIndex = selectTierIndex(random);
        trySpawnChestForTier(world, random, chunkX, chunkZ, tierIndex);
    }

    private int selectTierIndex(Random random)
    {
        double totalWeight = 0;

        for (double weight : TIER_WEIGHTS)
        {
            totalWeight += weight;
        }

        double randomValue = random.nextDouble() * totalWeight;
        double cumulativeWeight = 0;

        for (int i = 0; i < TIER_WEIGHTS.length; i++)
        {
            cumulativeWeight += TIER_WEIGHTS[i];

            if (randomValue <= cumulativeWeight)
            {
                return i;
            }
        }

        return 0;
    }

    private void trySpawnChestForTier(World world, Random random, int chunkX, int chunkZ, int tierIndex)
    {
        for (int attempt = 0; attempt < 5; attempt++)
        {
            int x = chunkX * 16 + random.nextInt(16);
            int z = chunkZ * 16 + random.nextInt(16);
            int y = tierIndex == 0 ? world.getHeight(x, z) : findCaveY(world, x, z, random);

            if (y == -1)
            {
                if (DEBUG)
                {
                    Log.write(2, "No suitable Y found for " + TIERS[tierIndex] + " chest");
                }

                continue;
            }

            if (DEBUG)
            {
                Log.write(3, String.format("Trying to spawn %s chest at [%d, %d, %d]",
                        TIERS[tierIndex], x, y, z));
            }

            if (!isValidSpawnLocation(world, x, y, z))
            {
                continue;
            }

            world.setBlockState(new BlockPos(x, y, z), Blocks.CHEST.getDefaultState(), 2);

            if (DEBUG)
            {
                Log.write(0, String.format("Spawned %s chest at [%d, %d, %d]",
                        TIERS[tierIndex], x, y, z));
            }

            TileEntityChest chest = (TileEntityChest) world.getTileEntity(new BlockPos(x, y, z));

            if (chest != null)
            {
                if (DEBUG)
                {
                    Log.write(3, "Adding loot to chest...");
                }

                addLootToChest(chest, random, TIERS[tierIndex]);
            }

            break;
        }
    }

    private int findCaveY(World world, int x, int z, Random random)
    {
        for (int i = 0; i < 10; i++)
        {
            int y = 10 + random.nextInt(50);

            if (world.isAirBlock(new BlockPos(x, y, z)) &&
                    !world.isAirBlock(new BlockPos(x, y-1, z)) &&
                    !world.getBlockState(new BlockPos(x, y-1, z)).getMaterial().isLiquid())
            {
                return y;
            }
        }

        return -1;
    }

    private boolean isValidSpawnLocation(World world, int x, int y, int z)
    {
        BlockPos belowPos = new BlockPos(x, y-1, z);

        if (world.isAirBlock(belowPos) || world.getBlockState(belowPos).getMaterial().isLiquid())
        {
            if (DEBUG)
            {
                Log.write(2, "Invalid block below at [" + x + ", " + (y - 1) + ", " + z + "]");
            }

            return false;
        }

        if (!world.isAirBlock(new BlockPos(x, y, z)))
        {
            if (DEBUG)
            {
                Log.write(2, "Target block is not air at [" + x + ", " + y + ", " + z + "]");
            }

            return false;
        }

        for (int dx = 0; dx <= 1; dx++)
        {
            for (int dz = 0; dz <= 1; dz++)
            {
                BlockPos pos = new BlockPos(x + dx, y, z + dz);

                if (!world.isAirBlock(pos) && !world.getBlockState(pos).getBlock().isReplaceable(world, pos))
                {
                    if (DEBUG)
                    {
                        Log.write(2, "Block at [" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() +
                                "] is not air or replaceable: " + world.getBlockState(pos).getBlock().getRegistryName());
                    }

                    return false;
                }
            }
        }

        return true;
    }

    private void addLootToChest(TileEntityChest tileEntityChest, Random random, String tier)
    {
        Map<String, List<LootBox.Data>> lootTable = GeneralLootBox.getInstance().lootTable;

        if (lootTable == null || lootTable.isEmpty())
        {
            if (DEBUG)
            {
                Log.write(1, "Loot table is null or empty for tier: " + tier);
            }

            return;
        }

        List<LootBox.Data> entries = lootTable.get(tier);

        if (entries == null || entries.isEmpty())
        {
            if (DEBUG)
            {
                Log.write(1, "No entries found in loot table for tier: " + tier);
            }

            return;
        }

        if (DEBUG)
        {
            Log.write(3, "Adding " + entries.size() + " possible items to chest");
        }

        int itemsAdded = 0;

        for (LootBox.Data entry : entries)
        {
            if (random.nextFloat() > entry.getChance())
            {
                if (DEBUG)
                {
                    Log.write(4, "Item " + entry.getItem() + " failed chance check");
                }

                continue;
            }

            Item item = Item.getByNameOrId(entry.getItem());

            if (item == null)
            {
                if (DEBUG)
                {
                    Log.write(1, "Item not found: " + entry.getItem());
                }

                continue;
            }

            int count = entry.getMinCount() +
                    random.nextInt(entry.getMaxCount() - entry.getMinCount() + 1);

            ItemStack stack = new ItemStack(item, count);

            int slot = random.nextInt(tileEntityChest.getSizeInventory());
            tileEntityChest.setInventorySlotContents(slot, stack);

            itemsAdded++;

            if (DEBUG)
            {
                Log.write(3, "Added " + count + "x " + entry.getItem() + " to slot " + slot);
            }
        }

        if (DEBUG)
        {
            Log.write(2, "Added " + itemsAdded + " items to chest of tier " + tier);
        }
    }
}