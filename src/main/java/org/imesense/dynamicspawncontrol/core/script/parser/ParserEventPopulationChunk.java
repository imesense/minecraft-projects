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
import org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.data.SecondaryParameters1;
import org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.storage.GeneralPopulationChunkSpawn;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.data.SecondaryParameters;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.storage.GeneralPotentialSpawnStorage;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserEventPopulationChunk extends AbstractConceptParser
{
    public ParserEventPopulationChunk(final String NAME_FILE)
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

            List<Biome.SpawnListEntry> newSpawnEntries = new ArrayList<>();
            List<SecondaryParameters1.Data> newSecondaryParameters = new ArrayList<>();

            for (JsonElement topLevelElement : jsonArray)
            {
                JsonObject topLevelObject = topLevelElement.getAsJsonObject();

                if (topLevelObject.has("mobs"))
                {
                    JsonArray mobsArray = topLevelObject.getAsJsonArray("mobs");

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

                        Integer weight = mobMap.has("weight") ? mobMap.get("weight").getAsInt() : 1;
                        Integer groupCountMin = mobMap.has("groupcountmin") ? mobMap.get("groupcountmin").getAsInt() : 1;
                        Integer groupCountMax = mobMap.has("groupcountmax") ? mobMap.get("groupcountmax").getAsInt() : Math.max(groupCountMin, 1);

                        Biome.SpawnListEntry entry = new Biome.SpawnListEntry((Class<? extends EntityLiving>) clazz,
                                weight, groupCountMin, groupCountMax);

                        SecondaryParameters1.Data data = new SecondaryParameters1.Data();

                        data.spawnChance = mobMap.has("spawn_chance") ? mobMap.get("spawn_chance").getAsFloat() : 0.01f;
                        data.minHeight = mobMap.has("min_height") ? mobMap.get("min_height").getAsFloat() : 1.0f;
                        data.maxHeight = mobMap.has("max_height") ? mobMap.get("max_height").getAsFloat() : 255.0f;

                        newSpawnEntries.add(entry);
                        newSecondaryParameters.add(data);

                        Log.writeDataToLogFile(0, String.format(
                                "Entity [%s:%s] has been added to the spawn list. " +
                                        "Data -> SpawnChance [%f], " +
                                        "Weight [%d], " +
                                        "Group min [%d], " +
                                        "Group max [%d], " +
                                        "Max Height [%f] " +
                                        "Min Height [%f]",
                                entry, id, data.spawnChance, weight, groupCountMin, groupCountMax, data.maxHeight, data.minHeight));
                    }
                }
            }

            GeneralPopulationChunkSpawn.getInstance().spawnEntries = newSpawnEntries;
            GeneralPopulationChunkSpawn.getInstance().secondaryParameters1 = newSecondaryParameters;
        }
        catch (IOException | JsonSyntaxException exception)
        {
            Log.writeDataToLogFile(0, "Error loading config file: " + exception.getMessage());
        }
    }

    @Override
    public void eraseData()
    {
        GeneralPopulationChunkSpawn.getInstance().spawnEntries.clear();
        GeneralPopulationChunkSpawn.getInstance().secondaryParameters1.clear();
    }
}
