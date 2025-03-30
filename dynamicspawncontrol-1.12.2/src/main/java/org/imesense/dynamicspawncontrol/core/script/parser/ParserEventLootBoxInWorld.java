package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
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
    @Getter
    private Map<String, List<LootEntry>> lootTable;

    public static ParserEventLootBoxInWorld instance;

    public final static class LootEntry
    {
        private String item;
        private int minCount = 1;
        private int maxCount = 1;
        private float chance = 1.0f;

        public String getItem() { return item; }
        public int getMinCount() { return minCount; }
        public int getMaxCount() { return maxCount; }
        public float getChance() { return chance; }
    }

    public ParserEventLootBoxInWorld(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;
        instance = this;
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
            Type type = new TypeToken<Map<String, List<LootEntry>>>() {}.getType();
            lootTable = gson.fromJson(fileReader, type);
            Log.writeDataToLogFile(0, "Loot table loaded: " + lootTable);
        }
        catch (Exception exception)
        {
            Log.writeDataToLogFile(2, "Error reading config: " + exception.getMessage());
        }
    }

    @Override
    public void eraseData()
    {
        if (lootTable != null)
        {
            lootTable.clear();
            Log.writeDataToLogFile(0, "Loot table cleared successfully.");
        }
        else
        {
            Log.writeDataToLogFile(2, "Loot table is already null. Nothing to clear.");
        }
    }
}
