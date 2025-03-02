package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.api.AbstractConceptParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.storage.GeneralPotentialSpawnStorage;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ParserEventPotentialSpawn extends AbstractConceptParser
{
    public ParserEventPotentialSpawn(final String NAME_FILE)
    {
        CodeGeneric.printInitClassToLog(this.getClass());
        this.nameFile = NAME_FILE;
    }

    @Override
    public void reloadConfig()
    {
        GeneralPotentialSpawnStorage.getInstance().spawnParametersList.clear();

        this.loadConfig(false);
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
        }

        try (FileReader fileReader = new FileReader(file))
        {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fileReader).getAsJsonObject();

            if (jsonObject == null)
            {
                jsonObject = new JsonObject();
                jsonObject.add("configs", new JsonArray());
            }

            JsonArray configsArray = jsonObject.has("configs") ? jsonObject.getAsJsonArray("configs") : new JsonArray();

            List<GeneralPotentialSpawnStorage.SpawnParameters> spawnParametersList = new ArrayList<>();

            for (JsonElement element : configsArray)
            {
                JsonObject config = element.getAsJsonObject();
                JsonObject structure = config.getAsJsonObject("structure");

                GeneralPotentialSpawnStorage.SpawnParameters params = new GeneralPotentialSpawnStorage.SpawnParameters();

                params.entityType = structure.get("entityType").getAsString();
                params.frequency = structure.get("frequency").getAsInt();
                params.groupCountMin = structure.get("groupCountMin").getAsInt();
                params.groupCountMax = structure.get("groupCountMax").getAsInt();
                params.spawnChance = structure.get("spawnChance").getAsFloat();
                params.maxHeight = structure.get("maxHeight").getAsInt();
                params.minHeight = structure.get("minHeight").getAsInt();

                spawnParametersList.add(params);
            }

            GeneralPotentialSpawnStorage.getInstance().spawnParametersList = spawnParametersList;
        }
        catch (IOException | JsonSyntaxException exception)
        {
            Log.writeDataToLogFile(0, "Error loading config file: " + exception.getMessage());
        }
    }

    private void createNewConfigFile(File file)
    {
        try
        {
            File parentDir = file.getParentFile();
            if (!parentDir.exists() && !parentDir.mkdirs())
            {
                Log.writeDataToLogFile(0, "Failed to create directory: " + parentDir.getAbsolutePath());
                return;
            }

            if (file.createNewFile())
            {
                try (FileWriter writer = new FileWriter(file))
                {
                    JsonObject emptyJson = new JsonObject();
                    emptyJson.add("configs", new JsonArray());
                    writer.write(emptyJson.toString());
                }
                Log.writeDataToLogFile(0, "Created new config file with empty JSON object: " + file.getAbsolutePath());
            }
        }
        catch (IOException exception)
        {
            Log.writeDataToLogFile(0, "Error creating config file: " + exception.getMessage());
        }
    }
}
