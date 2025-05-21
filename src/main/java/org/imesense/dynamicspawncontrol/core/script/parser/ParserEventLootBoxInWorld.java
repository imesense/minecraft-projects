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
    private static final boolean DEBUG_AND_CHECK_SYNTAX = true;

    public ParserEventLootBoxInWorld(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;

        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "ParserEventLootBoxInWorld initialized with config file: " + NAME_FILE);
        }
    }

    @Override
    public void loadConfig(boolean init)
    {
        Log.write(0, "Reading the config for the first time: " + init + " " + "file: " + this.nameFile);

        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_WORLD_SCRIPTS, this.nameFile);

        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "Config file path: " + file.getAbsolutePath());
        }

        if (!file.exists())
        {
            Log.write(0, "Config file not found, creating new: " + file);
            this.createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "Reading and parsing loot box configuration file");
            }

            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<LootBox.Data>>>() {}.getType();

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "Deserializing JSON to loot table map");
            }

            Map<String, List<LootBox.Data>> lootTable = gson.fromJson(fileReader, type);
            GeneralLootBox.getInstance().lootTable = lootTable;

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                if (lootTable != null)
                {
                    Log.write(0, "Loot table contains " + lootTable.size() + " entries");

                    for (Map.Entry<String, List<LootBox.Data>> entry : lootTable.entrySet())
                    {
                        Log.write(0, "Loot box '" + entry.getKey() + "' has " + entry.getValue().size() + " items");
                    }
                }
                else
                {
                    Log.write(0, "Loaded loot table is null");
                }
            }

            Log.write(0, "Loot table loaded successfully");
        }
        catch (JsonSyntaxException exception)
        {
            Log.write(2, "JSON syntax error in loot box config: " + exception.getMessage());

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "Stack trace: " + exception);
            }
        }
        catch (JsonParseException exception)
        {
            Log.write(2, "JSON parse error in loot box config: " + exception.getMessage());

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "Stack trace: " + exception);
            }
        }
        catch (IOException exception)
        {
            Log.write(2, "IO error reading loot box config: " + exception.getMessage());

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "Stack trace: " + exception);
            }
        }
        catch (Exception exception)
        {
            Log.write(2, "Unexpected error loading loot box config: " + exception.getMessage());

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "Stack trace: " + exception);
            }
        }
    }

    @Override
    public void eraseData()
    {
        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "Attempting to clear loot table");
        }

        if (GeneralLootBox.getInstance().lootTable != null)
        {
            int sizeBefore = GeneralLootBox.getInstance().lootTable.size();
            GeneralLootBox.getInstance().lootTable.clear();

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "Cleared " + sizeBefore + " entries from loot table");
            }

            Log.write(0, "Loot table cleared successfully.");
        }
        else
        {
            Log.write(2, "Loot table is already null. Nothing to clear.");
        }
    }
}
