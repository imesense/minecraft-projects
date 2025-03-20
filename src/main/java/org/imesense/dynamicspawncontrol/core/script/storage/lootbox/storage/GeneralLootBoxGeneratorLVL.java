package org.imesense.dynamicspawncontrol.core.script.storage.lootbox.storage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.data.LootBoxGeneratorLVL;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.*;

public final class GeneralLootBoxGeneratorLVL
{
    private static volatile GeneralLootBoxGeneratorLVL _INSTANCE;

    public static GeneralLootBoxGeneratorLVL getInstance()
    {
        return CodeGeneric.getInstance(GeneralLootBoxGeneratorLVL.class);
    }

    public GeneralLootBoxGeneratorLVL()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
        this.lootBoxGeneratorLVLData = new HashMap<>();
    }

    public Map<String, LootBoxGeneratorLVL.Data> lootBoxGeneratorLVLData;

    public List<ItemStack> parseLootItems(JsonArray itemsArray, Random random)
    {
        List<ItemStack> items = new ArrayList<>();

        for (JsonElement itemElement : itemsArray)
        {
            JsonObject itemObject = itemElement.getAsJsonObject();
            String itemName = itemObject.get("item").getAsString();
            Item item = Item.getByNameOrId(itemName);

            if (item != null)
            {
                double chanceToSpawn = itemObject.has("chance_to_spawn")
                        ? itemObject.get("chance_to_spawn").getAsDouble()
                        : 1.0;

                if (random.nextDouble() > chanceToSpawn)
                {
                    continue;
                }

                int minCount = 0;
                int maxCount = 1;

                if (itemObject.has("count_range"))
                {
                    String[] countRange = itemObject.get("count_range").getAsString().split("-");
                    minCount = Integer.parseInt(countRange[0]);
                    maxCount = Integer.parseInt(countRange[1]);
                }
                else if (itemObject.has("count"))
                {
                    minCount = maxCount = itemObject.get("count").getAsInt();
                }

                int count = minCount + random.nextInt(maxCount - minCount + 1);

                ItemStack itemStack = new ItemStack(item, count);

                if (itemObject.has("nbt"))
                {
                    try
                    {
                        NBTTagCompound nbt = JsonToNBT.getTagFromJson(itemObject.get("nbt").getAsString());
                        itemStack.setTagCompound(nbt);
                    }
                    catch (NBTException exception)
                    {
                        Log.writeDataToLogFile(2, "Error parsing NBT data: " + exception.getMessage());
                    }
                }

                items.add(itemStack);
            }
        }

        return items;
    }
}
