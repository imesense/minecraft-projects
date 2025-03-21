package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import net.minecraft.item.ItemStack;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.data.LootBoxGeneratorLVL;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.storage.GeneralLootBoxGeneratorLVL;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * [
 *     {
 *         "data":
 *         {
 *             "levels": "loot_box_1, loot_box_2, loot_box_3",
 *             "world_for_spawn": "(0: loot_box_1, loot_box_2, loot_box_3, 1: loot_box_3)"
 *         },
 *         "lootboxex":
 *         [
 *             "loot_box_1":
 *             {
 *                 "items": "(0.4, minecraft:apple, 0-5)"
 *             },
 *             "loot_box_2":
 *             {
 *                 "items": "(0.7, minecraft:deamond, 0-3)"
 *             },
 *             "loot_box_3":
 *             {
 *                 "items": "(0.7, minecraft:emerald, 0-4)"
 *             }
 *         ]
 *     }
 * ]
 */
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

    @Override
    public void eraseData()
    {

    }
}