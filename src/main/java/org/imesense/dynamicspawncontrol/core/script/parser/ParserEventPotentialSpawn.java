package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
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
    //-' todo переделать потом все нахуй
    private List<Biome.SpawnListEntry> spawnEntries = new ArrayList<>();

    public ParserEventPotentialSpawn(final String NAME_FILE)
    {
        CodeGeneric.printInitClassToLog(this.getClass());
        this.nameFile = NAME_FILE;
    }

    @Override
    public void reloadConfig()
    {
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
            JsonArray mobsArray = jsonObject.getAsJsonArray("mobs");

            Log.writeDataToLogFile(0, "Loaded mobs: " + mobsArray.size());

            for (JsonElement mobElement : mobsArray)
            {
                JsonObject mobMap = mobElement.getAsJsonObject();
                String id = mobMap.get("mob").getAsString();

                EntityEntry ee = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(id));

                if (ee == null)
                {
                    Log.writeDataToLogFile(0, "Mob not found: " + id);
                    continue;
                }

                Class<? extends Entity> clazz = ee.getEntityClass();
                if (clazz == null)
                {
                    Log.writeDataToLogFile(0, "Entity class not found for mob: " + id);
                    continue;
                }

                int weight = mobMap.has("weight") ? mobMap.get("weight").getAsInt() : 1;
                int groupCountMin = mobMap.has("groupcountmin") ? mobMap.get("groupcountmin").getAsInt() : 1;
                int groupCountMax = mobMap.has("groupcountmax") ? mobMap.get("groupcountmax").getAsInt() : Math.max(groupCountMin, 1);

                Biome.SpawnListEntry entry = new Biome.SpawnListEntry((Class<? extends EntityLiving>) clazz,
                        weight, groupCountMin, groupCountMax);

                spawnEntries.add(entry);
            }

            GeneralPotentialSpawnStorage.getInstance().setSpawnEntries(spawnEntries);
        }
        catch (IOException | JsonSyntaxException exception)
        {
            Log.writeDataToLogFile(0, "Error loading config file: " + exception.getMessage());
        }
    }

    //-' todo эту хуйню вообще нахуй отсюда в абстракт убрать. пиздец
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
                    emptyJson.add("mobs", new JsonArray());
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
