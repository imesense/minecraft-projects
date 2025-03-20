package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.data.LootBoxGeneratorLVL;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.storage.GeneralLootBoxGeneratorLVL;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ParserEventLootBoxGeneratorLVL extends BaseParser
{
    public ParserEventLootBoxGeneratorLVL(final String NAME_FILE)
    {
        CodeGeneric.printInitClassToLog(this.getClass());
        this.nameFile = NAME_FILE;
    }

    @Override
    public void loadConfig(boolean init)
    {
        Log.writeDataToLogFile(0, "Reading the config for the first time: " + init + " " + "file: " + this.nameFile);

        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);

        if (!file.exists())
        {
            Log.writeDataToLogFile(0, "Config file not found, creating new: " + file);
            this.createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(fileReader, JsonArray.class);

            for (JsonElement jsonElement : jsonArray)
            {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                String chestLevel = jsonObject.keySet().iterator().next();
                JsonObject chestData = jsonObject.getAsJsonObject(chestLevel);

                double spawnChance = chestData.get("spawn_chance").getAsDouble();
                int maxHeight = chestData.get("max_height_spawn").getAsInt();
                int minHeight = chestData.get("min_height_spawn").getAsInt();

                JsonArray itemsArray = chestData.getAsJsonArray("loot");
                List<ItemStack> items = parseItems(itemsArray);

                LootBoxGeneratorLVL.Data lootBoxData = new LootBoxGeneratorLVL.Data(spawnChance, maxHeight, minHeight, items);
                GeneralLootBoxGeneratorLVL.getInstance().lootBoxGeneratorLVLData.put(chestLevel, lootBoxData);
            }
        }
        catch (JsonSyntaxException | IOException exception)
        {
            Log.writeDataToLogFile(2, "Error reading config file: " + exception.getMessage());
        }
        catch (RuntimeException exception)
        {
            Log.writeDataToLogFile(2, "Runtime error: " + exception.getMessage());
        }
    }

    private List<ItemStack> parseItems(JsonArray itemsArray)
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

    @Override
    public void eraseData()
    {
        GeneralLootBoxGeneratorLVL.getInstance().lootBoxGeneratorLVLData.clear();
    }
}
