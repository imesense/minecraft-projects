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

public final class ParserEventMobTaskManager extends BaseParser
{
    public ParserEventMobTaskManager(final String NAME_FILE)
    {
        CodeGeneric.printInitClassToLog(this.getClass());
        this.nameFile = NAME_FILE;
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
                    AddEnemy.Data object = new AddEnemy.Data();
                    //
                }
                else if (topLevelObject.has("enemies_to") && topLevelObject.has("enemy_id"))
                {
                    AddEnemyId.Data object = new AddEnemyId.Data();
                    //
                }
                else if (topLevelObject.has("panic_to") && topLevelObject.has("panic_id"))
                {
                    AddPanicToId.Data object = new AddPanicToId.Data();
                    //
                }
                else if (topLevelObject.has("enemy_id") && topLevelObject.has("them_id"))
                {
                    AddEnemyToIdThemToId.Data object = new AddEnemyToIdThemToId.Data();
                    //
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
        GeneralMobTaskManager.getInstance().listEnemiesToToThemData.clear();
        GeneralMobTaskManager.getInstance().listEnemiesToEnemyIdData.clear();;
        GeneralMobTaskManager.getInstance().listPanicToPanicIdData.clear();;
        GeneralMobTaskManager.getInstance().listEnemyIdThemIdData.clear();;
    }
}
