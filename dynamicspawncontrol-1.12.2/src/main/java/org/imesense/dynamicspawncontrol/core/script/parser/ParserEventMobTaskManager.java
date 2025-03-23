package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    public ParserEventMobTaskManager(final String NAME_FILE)
    {
        super();

        this.nameFile = NAME_FILE;
    }

    private String[] getStringArray(JsonObject jsonObject, String key)
    {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);

        String[] array = new String[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++)
        {
            array[i] = jsonArray.get(i).getAsString();
        }
        
        return array;
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
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(fileReader).getAsJsonArray();

            GeneralMobTaskManager taskManager = GeneralMobTaskManager.getInstance();

            for (JsonElement topLevelElement : jsonArray)
            {
                JsonObject topLevelObject = topLevelElement.getAsJsonObject();

                if (topLevelObject.has("enemies_to") && topLevelObject.has("to_them"))
                {
                    AddEnemy.Data data = new AddEnemy.Data();
                    data.enemies_to = getStringArray(topLevelObject, "enemies_to");
                    data.to_them = getStringArray(topLevelObject, "to_them");

                    Log.writeDataToLogFile(0, "Parsed AddEnemy.Data:");
                    Log.writeDataToLogFile(0, "enemies_to: " + Arrays.toString(data.enemies_to));
                    Log.writeDataToLogFile(0, "to_them: " + Arrays.toString(data.to_them));

                    taskManager.addEnemyData.add(data);

                    Log.writeDataToLogFile(0, "AddEnemy.Data successfully added to addEnemyData.");
                }
                else if (topLevelObject.has("enemies_to") && topLevelObject.has("enemy_id"))
                {
                    AddEnemyId.Data data = new AddEnemyId.Data();
                    data.enemies_to = getStringArray(topLevelObject, "enemies_to");
                    data.enemy_id = getStringArray(topLevelObject, "enemy_id");

                    Log.writeDataToLogFile(0, "Parsed AddEnemyId.Data:");
                    Log.writeDataToLogFile(0, "enemies_to: " + Arrays.toString(data.enemies_to));
                    Log.writeDataToLogFile(0, "enemy_id: " + Arrays.toString(data.enemy_id));

                    taskManager.addEnemyIdData.add(data);

                    Log.writeDataToLogFile(0, "AddEnemyId.Data successfully added to addEnemyIdData.");
                }
                else if (topLevelObject.has("panic_to") && topLevelObject.has("panic_id"))
                {
                    AddPanicToId.Data data = new AddPanicToId.Data();
                    data.panic_to = getStringArray(topLevelObject, "panic_to");
                    data.panic_id = getStringArray(topLevelObject, "panic_id");

                    Log.writeDataToLogFile(0, "Parsed AddPanicToId.Data:");
                    Log.writeDataToLogFile(0, "panic_to: " + Arrays.toString(data.panic_to));
                    Log.writeDataToLogFile(0, "panic_id: " + Arrays.toString(data.panic_id));

                    taskManager.addPanicToIdData.add(data);

                    Log.writeDataToLogFile(0, "AddPanicToId.Data successfully added to addPanicToIdData.");
                }
                else if (topLevelObject.has("enemy_id") && topLevelObject.has("them_id"))
                {
                    AddEnemyToIdThemToId.Data data = new AddEnemyToIdThemToId.Data();
                    data.enemy_id = getStringArray(topLevelObject, "enemy_id");
                    data.them_id = getStringArray(topLevelObject, "them_id");

                    Log.writeDataToLogFile(0, "Parsed AddEnemyToIdThemToId.Data:");
                    Log.writeDataToLogFile(0, "enemy_id: " + Arrays.toString(data.enemy_id));
                    Log.writeDataToLogFile(0, "them_id: " + Arrays.toString(data.them_id));

                    taskManager.addEnemyToIdThemToIdData.add(data);

                    Log.writeDataToLogFile(0, "AddEnemyToIdThemToId.Data successfully added to addEnemyToIdThemToIdData.");
                }
            }
        }
        catch (FileNotFoundException exception)
        {
            throw new RuntimeException(exception);
        }
        catch (IOException exception)
        {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void eraseData()
    {
        GeneralMobTaskManager.getInstance().addEnemyData.clear();
        GeneralMobTaskManager.getInstance().addEnemyIdData.clear();
        GeneralMobTaskManager.getInstance().addPanicToIdData.clear();
        GeneralMobTaskManager.getInstance().addEnemyToIdThemToIdData.clear();
    }
}
