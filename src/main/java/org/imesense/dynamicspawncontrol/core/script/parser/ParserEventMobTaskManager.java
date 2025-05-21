package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddEnemy;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddPanicToId;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddEnemyId;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddEnemyToIdThemToId;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.storage.GeneralMobTaskManager;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@InitLog
public final class ParserEventMobTaskManager extends BaseParser
{
    private static final boolean DEBUG_AND_CHECK_SYNTAX = true;

    public ParserEventMobTaskManager(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;

        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "ParserEventMobTaskManager initialized with config file: " + NAME_FILE);
        }
    }

    private String[] getStringArray(JsonObject jsonObject, String key)
    {
        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "Getting string array for key: " + key);
        }

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        String[] array = new String[jsonArray.size()];

        for (Integer i = 0; i < jsonArray.size(); i++)
        {
            array[i] = jsonArray.get(i).getAsString();

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "Array element [" + i + "]: " + array[i]);
            }
        }

        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "Returning array with " + array.length + " elements");
        }

        return array;
    }

    @Override
    public void loadConfig(boolean init)
    {
        Log.write(0, "Reading the config for the first time: " + init + " " + "file: " + this.nameFile);

        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);

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
                Log.write(0, "Reading and parsing mob task configuration");
            }

            JsonArray jsonArray = JsonParser.parseReader(fileReader).getAsJsonArray();
            GeneralMobTaskManager taskManager = GeneralMobTaskManager.getInstance();

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "Found " + jsonArray.size() + " top-level configuration entries");
            }

            for (JsonElement topLevelElement : jsonArray)
            {
                try
                {
                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        Log.write(0, "Processing new configuration entry");
                    }

                    JsonObject topLevelObject = topLevelElement.getAsJsonObject();

                    if (topLevelObject.has("enemies_to") && topLevelObject.has("to_them"))
                    {
                        if (DEBUG_AND_CHECK_SYNTAX)
                        {
                            Log.write(0, "Found AddEnemy.Data configuration");
                        }

                        AddEnemy.Data data = new AddEnemy.Data();
                        data.enemies_to = getStringArray(topLevelObject, "enemies_to");
                        data.to_them = getStringArray(topLevelObject, "to_them");

                        Log.write(0, "Parsed AddEnemy.Data:");
                        Log.write(0, "enemies_to: " + Arrays.toString(data.enemies_to));
                        Log.write(0, "to_them: " + Arrays.toString(data.to_them));

                        taskManager.addEnemyData.add(data);

                        if (DEBUG_AND_CHECK_SYNTAX)
                        {
                            Log.write(0, "AddEnemy.Data successfully added (total: " +
                                    taskManager.addEnemyData.size() + ")");
                        }
                    }
                    else if (topLevelObject.has("enemies_to") && topLevelObject.has("enemy_id"))
                    {
                        if (DEBUG_AND_CHECK_SYNTAX)
                        {
                            Log.write(0, "Found AddEnemyId.Data configuration");
                        }

                        AddEnemyId.Data data = new AddEnemyId.Data();
                        data.enemies_to = getStringArray(topLevelObject, "enemies_to");
                        data.enemy_id = getStringArray(topLevelObject, "enemy_id");

                        Log.write(0, "Parsed AddEnemyId.Data:");
                        Log.write(0, "enemies_to: " + Arrays.toString(data.enemies_to));
                        Log.write(0, "enemy_id: " + Arrays.toString(data.enemy_id));

                        taskManager.addEnemyIdData.add(data);

                        if (DEBUG_AND_CHECK_SYNTAX)
                        {
                            Log.write(0, "AddEnemyId.Data successfully added (total: " +
                                    taskManager.addEnemyIdData.size() + ")");
                        }
                    }
                    else if (topLevelObject.has("panic_to") && topLevelObject.has("panic_id"))
                    {
                        if (DEBUG_AND_CHECK_SYNTAX)
                        {
                            Log.write(0, "Found AddPanicToId.Data configuration");
                        }

                        AddPanicToId.Data data = new AddPanicToId.Data();
                        data.panic_to = getStringArray(topLevelObject, "panic_to");
                        data.panic_id = getStringArray(topLevelObject, "panic_id");

                        Log.write(0, "Parsed AddPanicToId.Data:");
                        Log.write(0, "panic_to: " + Arrays.toString(data.panic_to));
                        Log.write(0, "panic_id: " + Arrays.toString(data.panic_id));

                        taskManager.addPanicToIdData.add(data);

                        if (DEBUG_AND_CHECK_SYNTAX)
                        {
                            Log.write(0, "AddPanicToId.Data successfully added (total: " +
                                    taskManager.addPanicToIdData.size() + ")");
                        }
                    }
                    else if (topLevelObject.has("enemy_id") && topLevelObject.has("them_id"))
                    {
                        if (DEBUG_AND_CHECK_SYNTAX)
                        {
                            Log.write(0, "Found AddEnemyToIdThemToId.Data configuration");
                        }

                        AddEnemyToIdThemToId.Data data = new AddEnemyToIdThemToId.Data();
                        data.enemy_id = getStringArray(topLevelObject, "enemy_id");
                        data.them_id = getStringArray(topLevelObject, "them_id");

                        Log.write(0, "Parsed AddEnemyToIdThemToId.Data:");
                        Log.write(0, "enemy_id: " + Arrays.toString(data.enemy_id));
                        Log.write(0, "them_id: " + Arrays.toString(data.them_id));

                        taskManager.addEnemyToIdThemToIdData.add(data);

                        if (DEBUG_AND_CHECK_SYNTAX)
                        {
                            Log.write(0, "AddEnemyToIdThemToId.Data successfully added (total: " +
                                    taskManager.addEnemyToIdThemToIdData.size() + ")");
                        }
                    }
                    else
                    {
                        Log.write(2, "Unknown configuration format in JSON object: " + topLevelObject);
                    }
                }
                catch (Exception exception)
                {
                    Log.write(2, "Error processing configuration entry: " + exception.getMessage());
                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        exception.printStackTrace();
                    }
                }
            }
        }
        catch (FileNotFoundException exception)
        {
            Log.write(2, "Config file not found: " + exception.getMessage());
            throw new RuntimeException(exception);
        }
        catch (IOException exception)
        {
            Log.write(2, "IO error reading config: " + exception.getMessage());
            throw new RuntimeException(exception);
        }
        catch (JsonParseException exception)
        {
            Log.write(2, "JSON parse error: " + exception.getMessage());
            throw new RuntimeException(exception);
        }
        catch (Exception exception)
        {
            Log.write(2, "Unexpected error: " + exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void eraseData()
    {
        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "Clearing all mob task data");
        }

        GeneralMobTaskManager taskManager = GeneralMobTaskManager.getInstance();

        int enemyDataCount = taskManager.addEnemyData.size();
        int enemyIdDataCount = taskManager.addEnemyIdData.size();
        int panicDataCount = taskManager.addPanicToIdData.size();
        int enemyThemDataCount = taskManager.addEnemyToIdThemToIdData.size();

        taskManager.addEnemyData.clear();
        taskManager.addEnemyIdData.clear();
        taskManager.addPanicToIdData.clear();
        taskManager.addEnemyToIdThemToIdData.clear();

        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, String.format(
                    "Cleared mob task data: %d AddEnemy, %d AddEnemyId, %d AddPanicToId, %d AddEnemyToIdThemToId",
                    enemyDataCount, enemyIdDataCount, panicDataCount, enemyThemDataCount
            ));
        }

        Log.write(0, "Mob task data cleared successfully");
    }
}
