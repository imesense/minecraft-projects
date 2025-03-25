package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.data.PopulationChunkStruct;
import org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.storage.GeneralPopulationChunkSpawn;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@InitLog
public final class ParserEventPopulationChunk extends BaseParser
{
    public ParserEventPopulationChunk(final String NAME_FILE)
    {
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
            JsonArray jsonArray = JsonParser.parseReader(fileReader).getAsJsonArray();

            List<Biome.SpawnListEntry> newSpawnEntries = new ArrayList<>();
            List<PopulationChunkStruct.Data> populationList = new ArrayList<>();

            Log.writeDataToLogFile(0, "Starting to parse JSON config.");

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

                        Log.writeDataToLogFile(0, "Processing mob: " + id);

                        EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(id));

                        if (entityEntry == null)
                        {
                            Log.writeDataToLogFile(0, "Mob not found: " + id);
                            continue;
                        }

                        Class<? extends Entity> _class = entityEntry.getEntityClass();

                        if (_class == null)
                        {
                            Log.writeDataToLogFile(0, "Entity class not found for mob: " + id);
                            continue;
                        }

                        Integer weight = mobMap.has("weight") ?
                                mobMap.get("weight").getAsInt() : 1;

                        Integer groupCountMin = mobMap.has("groupcountmin") ?
                                mobMap.get("groupcountmin").getAsInt() : 1;

                        Integer groupCountMax = mobMap.has("groupcountmax") ?
                                mobMap.get("groupcountmax").getAsInt() : Math.max(groupCountMin, 1);

                        String spawnChancePriority =
                                mobMap.has("spawn_chance_priority") ?
                                        mobMap.get("spawn_chance_priority").getAsString() : "medium";

                        Integer maxEntitiesPerChunk =
                                mobMap.has("max_entities_per_chunk") ?
                                        mobMap.get("max_entities_per_chunk").getAsInt() : 1;

                        List<String> biomes = new ArrayList<>();

                        if (mobMap.has("biome"))
                        {
                            String biomeString = mobMap.get("biome").getAsString();
                            Log.writeDataToLogFile(0, "Biomes for mob " + id + ": " + biomeString);

                            if (biomeString.contains(","))
                            {
                                String[] biomeArray = biomeString.split(",");

                                for (String biome : biomeArray)
                                {
                                    biomes.add(biome.trim());
                                }
                            }
                            else
                            {
                                biomes.add(biomeString.trim());
                            }
                        }

                        boolean isWater = mobMap.has("isWater") ? mobMap.get("isWater").getAsBoolean() : false;

                        PopulationChunkStruct.Data data = new PopulationChunkStruct.Data();
                        data.entity = new ResourceLocation(id);
                        data.weight = weight;
                        data.groupCountMin = groupCountMin;
                        data.groupCountMax = groupCountMax;
                        data.biomes = biomes;
                        data.isWater = isWater;
                        data.spawnChancePriority = spawnChancePriority;
                        data.maxEntitiesPerChunk = maxEntitiesPerChunk;
                        populationList.add(data);

                        Log.writeDataToLogFile(0, "Added mob to population list: " + id);

                        Biome.SpawnListEntry entry = new Biome.SpawnListEntry((Class<? extends EntityLiving>) _class,
                                weight, groupCountMin, groupCountMax);

                        newSpawnEntries.add(entry);
                    }
                }
            }

            GeneralPopulationChunkSpawn.getInstance().populationChunkStruct = populationList;
            Log.writeDataToLogFile(0, "Config parsing completed successfully.");
        }
        catch (IOException | JsonSyntaxException exception)
        {
            Log.writeDataToLogFile(0, "Error loading config file: " + exception.getMessage());
        }
    }

    @Override
    public void eraseData()
    {
        GeneralPopulationChunkSpawn.getInstance().populationChunkStruct.clear();
    }
}