package org.imesense.dynamicspawncontrol.core.script.storage.lootbox.storage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.enchantment.Enchantment;
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

                Log.writeDataToLogFile(0, "Created ItemStack: " + itemStack.getDisplayName() + " x" + count);

                if (itemObject.has("enchantments"))
                {
                    JsonArray enchantmentsArray = itemObject.getAsJsonArray("enchantments");
                    for (JsonElement enchantmentElement : enchantmentsArray)
                    {
                        JsonObject enchantmentObject = enchantmentElement.getAsJsonObject();
                        int enchantmentId = enchantmentObject.get("id").getAsInt();
                        double enchantmentChance = enchantmentObject.get("chance").getAsDouble();

                        if (random.nextDouble() <= enchantmentChance)
                        {
                            String[] levelRange = enchantmentObject.get("lvl").getAsString().split(":");
                            int minLevel = Integer.parseInt(levelRange[0]);
                            int maxLevel = Integer.parseInt(levelRange[1]);
                            int level = minLevel + random.nextInt(maxLevel - minLevel + 1);

                            Enchantment enchantment = Enchantment.getEnchantmentByID(enchantmentId);

                            if (enchantment != null)
                            {
                                itemStack.addEnchantment(enchantment, level);
                                Log.writeDataToLogFile(0, "Added enchantment: " + enchantment.getName() + " lvl " + level);
                            }
                        }
                    }
                }

                if (itemObject.has("nbt"))
                {
                    try
                    {
                        NBTTagCompound nbt = JsonToNBT.getTagFromJson(itemObject.get("nbt").getAsString());
                        itemStack.setTagCompound(nbt);
                        Log.writeDataToLogFile(0, "Added NBT data to item: " + itemStack.getDisplayName());
                    }
                    catch (NBTException exception)
                    {
                        Log.writeDataToLogFile(2, "Error parsing NBT data: " + exception.getMessage());
                    }
                }

                items.add(itemStack);
            }
            else
            {
                Log.writeDataToLogFile(2, "Item not found: " + itemName);
            }
        }

        return items;
    }
}
