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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<ItemStack> parseLootItems(JsonArray itemsArray)
    {
        List<ItemStack> items = new ArrayList<>();

        for (JsonElement itemElement : itemsArray)
        {
            JsonObject itemObject = itemElement.getAsJsonObject();
            String itemName = itemObject.get("item").getAsString();
            int count = itemObject.get("count").getAsInt();
            Item item = Item.getByNameOrId(itemName);

            if (item != null)
            {
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
