package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.data.LootBox;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.storage.GeneralLootBox;

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
        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_WORLD_SCRIPTS, this.nameFile);

        if (!file.exists())
        {
            this.createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<LootBox.Data>>>() {}.getType();

            Map<String, List<LootBox.Data>> lootTable = gson.fromJson(fileReader, type);
            GeneralLootBox.getInstance().lootTable = lootTable;
        }
        catch (JsonSyntaxException exception)
        {

        }
        catch (JsonParseException exception)
        {

        }
        catch (IOException exception)
        {

        }
        catch (Exception exception)
        {

        }
    }

    @Override
    public void eraseData()
    {
        if (GeneralLootBox.getInstance().lootTable != null)
        {
            GeneralLootBox.getInstance().lootTable.clear();
        }
        else
        {

        }
    }
}
