package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.api.AbstractConceptParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.storage.GeneralMobTaskManager;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ParserEventMobTaskManager extends AbstractConceptParser
{
    public ParserEventMobTaskManager(final String NAME_FILE)
    {
        CodeGeneric.printInitClassToLog(this.getClass());
        this.nameFile = NAME_FILE;
    }

    @Override
    public void loadConfig(boolean init)
    {
        File file = getConfigFile(init, DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);

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
                    GeneralMobTaskManager.EntityHostilityToThem hostility = new GeneralMobTaskManager.EntityHostilityToThem();
                    hostility.enemies_to = parseStringArray(topLevelObject.getAsJsonArray("enemies_to"));
                    hostility.to_them = parseStringArray(topLevelObject.getAsJsonArray("to_them"));
                    taskManager.addEnemy(hostility);
                }
                else if (topLevelObject.has("enemy_id") && topLevelObject.has("to_them"))
                {
                    GeneralMobTaskManager.EntityHostilityToID hostility = new GeneralMobTaskManager.EntityHostilityToID();
                    hostility.enemy_id = topLevelObject.get("enemy_id").getAsString();
                    hostility.to_them = parseStringArray(topLevelObject.getAsJsonArray("to_them"));
                    taskManager.addEnemyByIdPrefix(hostility);
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

    private String[] parseStringArray(JsonArray jsonArray)
    {
        String[] array = new String[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++)
        {
            array[i] = jsonArray.get(i).getAsString();
        }

        return array;
    }

    @Override
    public void eraseData()
    {

    }
}
