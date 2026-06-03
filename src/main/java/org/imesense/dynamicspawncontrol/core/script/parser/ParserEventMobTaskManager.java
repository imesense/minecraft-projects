package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.base.BaseParser;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.*;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.storage.GeneralMobTaskManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@InitLog
public final class ParserEventMobTaskManager extends BaseParser
{
    public ParserEventMobTaskManager(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;
    }

    private String[] getStringArray(JsonObject jsonObject, String key)
    {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        String[] array = new String[jsonArray.size()];

        for (Integer i = 0; i < jsonArray.size(); i++)
        {
            array[i] = jsonArray.get(i).getAsString();
        }

        return array;
    }

    private Integer parseIdDimension(JsonObject jsonObject, String dataType)
    {
        if (jsonObject.has("id_dimension"))
        {
            Integer idDim = jsonObject.get("id_dimension").getAsInt();

            return idDim;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void loadConfig(boolean init)
    {
        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);

        if (!file.exists())
        {
            this.createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            JsonArray jsonArray = new JsonParser().parse(fileReader).getAsJsonArray();
            GeneralMobTaskManager taskManager = GeneralMobTaskManager.getInstance();

            for (JsonElement topLevelElement : jsonArray)
            {
                try
                {
                    JsonObject topLevelObject = topLevelElement.getAsJsonObject();

                    if (topLevelObject.has("enemies_to") && topLevelObject.has("to_them"))
                    {
                        AddEnemy.Data data = new AddEnemy.Data();
                        data.enemies_to = getStringArray(topLevelObject, "enemies_to");
                        data.to_them = getStringArray(topLevelObject, "to_them");

                        data.idDimension = parseIdDimension(topLevelObject, "AddEnemy.Data");

                        taskManager.addEnemyData.add(data);
                    }
                    else if (topLevelObject.has("enemies_to") && topLevelObject.has("enemy_id"))
                    {
                        AddEnemyId.Data data = new AddEnemyId.Data();
                        data.enemies_to = getStringArray(topLevelObject, "enemies_to");
                        data.enemy_id = getStringArray(topLevelObject, "enemy_id");

                        data.idDimension = parseIdDimension(topLevelObject, "AddEnemyId.Data");

                        taskManager.addEnemyIdData.add(data);
                    }
                    else if (topLevelObject.has("panic_to") && topLevelObject.has("panic_id"))
                    {
                        AddPanicToId.Data data = new AddPanicToId.Data();
                        data.panic_to = getStringArray(topLevelObject, "panic_to");
                        data.panic_id = getStringArray(topLevelObject, "panic_id");

                        data.idDimension = parseIdDimension(topLevelObject, "AddPanicToId.Data");

                        taskManager.addPanicToIdData.add(data);
                    }
                    else if (topLevelObject.has("enemy_id") && topLevelObject.has("them_id"))
                    {
                        AddEnemyToIdThemToId.Data data = new AddEnemyToIdThemToId.Data();
                        data.enemy_id = getStringArray(topLevelObject, "enemy_id");
                        data.them_id = getStringArray(topLevelObject, "them_id");

                        data.idDimension = parseIdDimension(topLevelObject, "AddEnemyToIdThemToId.Data");

                        taskManager.addEnemyToIdThemToIdData.add(data);
                    }
                }
                catch (Exception exception)
                {

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
        catch (JsonParseException exception)
        {
            throw new RuntimeException(exception);
        }
        catch (Exception exception)
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
