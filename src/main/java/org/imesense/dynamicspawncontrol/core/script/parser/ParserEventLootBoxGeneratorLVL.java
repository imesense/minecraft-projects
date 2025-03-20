package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.storage.GeneralLootBoxGeneratorLVL;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public final class ParserEventLootBoxGeneratorLVL extends BaseParser
{
    public ParserEventLootBoxGeneratorLVL(final String NAME_FILE)
    {
        CodeGeneric.printInitClassToLog(this.getClass());
        this.nameFile = NAME_FILE;
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
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(fileReader, JsonArray.class);

            for (JsonElement jsonElement : jsonArray)
            {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
            }
        }
        catch (JsonSyntaxException | IOException exception)
        {

        }
        catch (RuntimeException exception)
        {

        }
    }

    @Override
    public void eraseData()
    {
        GeneralLootBoxGeneratorLVL.getInstance().lootBoxGeneratorLVLData.clear();
    }
}
