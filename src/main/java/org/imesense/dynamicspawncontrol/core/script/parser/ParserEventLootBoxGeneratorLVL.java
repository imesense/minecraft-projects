package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import net.minecraft.item.ItemStack;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventCheckSpawn;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.data.LootBoxGeneratorLVL;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.storage.GeneralLootBoxGeneratorLVL;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class ParserEventLootBoxGeneratorLVL extends BaseParser
{
    @Getter
    public static Map<String, List<String>> lootTable;

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
            Type type = new TypeToken<Map<String, List<String>>>() {}.getType();
            lootTable = gson.fromJson(fileReader, type);
            Log.writeDataToLogFile(0, "Loot table loaded successfully: " + lootTable);
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
        lootTable = null;
    }
}