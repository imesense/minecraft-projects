package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
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
import java.util.List;

public final class ParserEventMobTaskManager extends BaseParser
{
    public ParserEventMobTaskManager(final String NAME_FILE)
    {
        CodeGeneric.printInitClassToLog(this.getClass());
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
        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);

        if (!file.exists())
        {
            Log.writeDataToLogFile(0, "Config file not found, creating new: " + file);
            createNewConfigFile(file);
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
                    taskManager.addEnemyData.add(data);
                }
                else if (topLevelObject.has("enemies_to") && topLevelObject.has("enemy_id"))
                {
                    AddEnemyId.Data data = new AddEnemyId.Data();
                    data.enemies_to = getStringArray(topLevelObject, "enemies_to");
                    data.enemy_id = getStringArray(topLevelObject, "enemy_id");
                    taskManager.addEnemyIdData.add(data);
                }
                else if (topLevelObject.has("panic_to") && topLevelObject.has("panic_id"))
                {
                    AddPanicToId.Data data = new AddPanicToId.Data();
                    data.panic_to = getStringArray(topLevelObject, "panic_to");
                    data.panic_id = getStringArray(topLevelObject, "panic_id");
                    taskManager.addPanicToIdData.add(data);
                }
                else if (topLevelObject.has("enemy_id") && topLevelObject.has("them_id"))
                {
                    AddEnemyToIdThemToId.Data data = new AddEnemyToIdThemToId.Data();
                    data.enemy_id = getStringArray(topLevelObject, "enemy_id");
                    data.them_id = getStringArray(topLevelObject, "them_id");
                    taskManager.addEnemyToIdThemToIdData.add(data);
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
