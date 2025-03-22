package org.imesense.dynamicspawncontrol.lootboxgenerator;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.parser.ParserEventGeneratorLootBoxInWorld;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class GeneratorLootBoxInWorld
{
    private static Map<String, List<String>> lootTable;
    private static final String[] TIERS = {"common", "rare", "legendary"};

    public static void loadLootConfig()
    {
        Log.writeDataToLogFile(0, "Loading loot configuration...");
        lootTable = ParserEventGeneratorLootBoxInWorld.getLootTable();

        if (lootTable == null || lootTable.isEmpty())
        {
            Log.writeDataToLogFile(2, "Loot table is empty or not loaded!");
        }
        else
        {
            Log.writeDataToLogFile(0, "Loot table loaded successfully. Tiers available: " + String.join(", ", lootTable.keySet()));
        }
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event)
    {
        Log.writeDataToLogFile(0, "Chunk load event triggered.");
        World world = event.getWorld();

        if (!world.isRemote)
        {
            Log.writeDataToLogFile(0, "Processing chunk load on server side.");
            Random random = new Random();

            // TODO: для дебага, сундуки всегда спавнятся в огромном количестве
            if (random.nextFloat() < 1.0)
            {
                Log.writeDataToLogFile(0, "Attempting to spawn a chest in the chunk.");

                int x = (event.getChunk().x * 16) + random.nextInt(16);
                int z = (event.getChunk().z * 16) + random.nextInt(16);
                int y = world.getHeight(x, z);

                Log.writeDataToLogFile(0, "Calculated spawn position: X=" + x + ", Y=" + y + ", Z=" + z);
                spawnChest(world, new BlockPos(x, y, z), random);
            }
            else
            {
                Log.writeDataToLogFile(0, "Chest spawn chance check failed. No chest will be spawned.");
            }
        }
        else
        {
            Log.writeDataToLogFile(0, "Chunk load event ignored on client side.");
        }
    }

    private static void spawnChest(World world, BlockPos pos, Random random)
    {
        Log.writeDataToLogFile(0, "Attempting to spawn a chest at position: " + pos);
        world.setBlockState(pos, Blocks.CHEST.getDefaultState(), 2);

        TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);
        if (chest != null)
        {
            Log.writeDataToLogFile(0, "Chest successfully spawned. Adding loot...");
            addLootToChest(chest, random);
        }
        else
        {
            Log.writeDataToLogFile(2, "Failed to create TileEntityChest at position: " + pos);
        }
    }

    private static void addLootToChest(TileEntityChest chest, Random random)
    {
        if (lootTable == null || lootTable.isEmpty())
        {
            Log.writeDataToLogFile(2, "Loot table is empty or not loaded!");
            return;
        }

        String selectedTier = TIERS[random.nextInt(TIERS.length)];
        Log.writeDataToLogFile(0, "Selected loot tier: " + selectedTier);

        List<String> items = lootTable.get(selectedTier);
        if (items == null || items.isEmpty())
        {
            Log.writeDataToLogFile(2, "No items found for tier: " + selectedTier);
            return;
        }

        int itemCount = random.nextInt(5) + 1;
        Log.writeDataToLogFile(0, "Adding " + itemCount + " items to the chest.");

        for (int i = 0; i < itemCount; i++)
        {
            String itemName = items.get(random.nextInt(items.size()));
            Log.writeDataToLogFile(0, "Selected item: " + itemName);

            Item item = Item.getByNameOrId(itemName);

            if (item == null)
            {
                Log.writeDataToLogFile(2, "Item not found: " + itemName);
                continue;
            }

            ItemStack stack = new ItemStack(item, 1);
            int slot = random.nextInt(chest.getSizeInventory());
            Log.writeDataToLogFile(0, "Adding item to slot " + slot + ": " + itemName);
            chest.setInventorySlotContents(slot, stack);
        }

        Log.writeDataToLogFile(0, "Chest loot generation completed.");
    }
}