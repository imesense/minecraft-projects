package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.data.LootBox;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.storage.GeneralLootBox;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@InitLog
public final class ParserEventLootBoxInWorld extends BaseParser
{
    public ParserEventLootBoxInWorld(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;
    }

    @Override
    public void loadConfig(boolean init)
    {
        Log.writeDataToLogFile(0, "Reading the config for the first time: " + init + " " + "file: " + this.nameFile);

        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_WORLD_SCRIPTS, this.nameFile);

        if (!file.exists())
        {
            Log.writeDataToLogFile(0, "Config file not found, creating new: " + file);
            this.createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            Gson gson = new Gson();

            Type type = new TypeToken<Map<String, List<LootBox.Data>>>() {}.getType();
            GeneralLootBox.getInstance().lootTable = gson.fromJson(fileReader, type);

            Log.writeDataToLogFile(0, "Loot table loaded: " + GeneralLootBox.getInstance().lootTable);
        }
        catch (Exception exception)
        {
            Log.writeDataToLogFile(2, "Error reading config: " + exception.getMessage());
        }
    }

    @Override
    public void eraseData()
    {
        if (GeneralLootBox.getInstance().lootTable != null)
        {
            GeneralLootBox.getInstance().lootTable.clear();
            Log.writeDataToLogFile(0, "Loot table cleared successfully.");
        }
        else
        {
            Log.writeDataToLogFile(2, "Loot table is already null. Nothing to clear.");
        }
    }
}
