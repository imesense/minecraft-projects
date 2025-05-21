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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@InitLog
public final class ParserEventPopulationChunk extends BaseParser
{
    private static final boolean DEBUG_AND_CHECK_SYNTAX = true;

    public ParserEventPopulationChunk(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;

        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "ParserEventPopulationChunk initialized with config file: " + NAME_FILE);
        }
    }

    @Override
    public void loadConfig(boolean init)
    {
        Log.write(0, "Reading the config for the first time: " + init + " " + "file: " + this.nameFile);

        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);

        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "Config file path: " + file.getAbsolutePath());
        }

        if (!file.exists())
        {
            Log.write(0, "Config file not found, creating new: " + file);
            this.createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "Reading and parsing population chunk configuration");
            }

            JsonArray jsonArray = JsonParser.parseReader(fileReader).getAsJsonArray();

            List<Biome.SpawnListEntry> newSpawnEntries = new ArrayList<>();
            List<PopulationChunkStruct.Data> populationList = new ArrayList<>();

            Log.write(0, "Starting to parse JSON config. Found " + jsonArray.size() + " top-level entries.");

            for (JsonElement topLevelElement : jsonArray)
            {
                try
                {
                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        Log.write(0, "Processing new top-level configuration entry");
                    }

                    JsonObject topLevelObject = topLevelElement.getAsJsonObject();

                    if (topLevelObject.has("mobs"))
                    {
                        JsonArray mobsArray = topLevelObject.getAsJsonArray("mobs");

                        if (DEBUG_AND_CHECK_SYNTAX)
                        {
                            Log.write(0, "Found mobs array with " + mobsArray.size() + " entries");
                        }

                        for (JsonElement mobElement : mobsArray)
                        {
                            try
                            {
                                JsonObject mobMap = mobElement.getAsJsonObject();
                                String id = mobMap.get("mob").getAsString();

                                if (DEBUG_AND_CHECK_SYNTAX)
                                {
                                    Log.write(0, "Processing mob: " + id);
                                }

                                ResourceLocation mobResource = new ResourceLocation(id);
                                EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(mobResource);

                                if (entityEntry == null)
                                {
                                    Log.write(2, "Mob not found in registry: " + id);
                                    continue;
                                }

                                Class<? extends Entity> _class = entityEntry.getEntityClass();

                                if (_class == null)
                                {
                                    Log.write(2, "Entity class not found for mob: " + id);
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

                                if (DEBUG_AND_CHECK_SYNTAX)
                                {
                                    Log.write(0, String.format(
                                            "Mob %s settings: weight=%d, groupCount=%d-%d, priority=%s, maxPerChunk=%d",
                                            id, weight, groupCountMin, groupCountMax, spawnChancePriority, maxEntitiesPerChunk
                                    ));
                                }

                                List<String> biomes = new ArrayList<>();

                                if (mobMap.has("biome"))
                                {
                                    String biomeString = mobMap.get("biome").getAsString();

                                    if (DEBUG_AND_CHECK_SYNTAX)
                                    {
                                        Log.write(0, "Processing biomes for mob " + id + ": " + biomeString);
                                    }

                                    if (biomeString.contains(","))
                                    {
                                        String[] biomeArray = biomeString.split(",");

                                        for (String biome : biomeArray)
                                        {
                                            String trimmedBiome = biome.trim();
                                            biomes.add(trimmedBiome);

                                            if (DEBUG_AND_CHECK_SYNTAX)
                                            {
                                                Log.write(0, "Added biome: " + trimmedBiome);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        String trimmedBiome = biomeString.trim();
                                        biomes.add(trimmedBiome);

                                        if (DEBUG_AND_CHECK_SYNTAX)
                                        {
                                            Log.write(0, "Added single biome: " + trimmedBiome);
                                        }
                                    }
                                }

                                boolean isWater = mobMap.has("isWater") ? mobMap.get("isWater").getAsBoolean() : false;

                                PopulationChunkStruct.Data data = new PopulationChunkStruct.Data();
                                data.entity = mobResource;
                                data.weight = weight;
                                data.groupCountMin = groupCountMin;
                                data.groupCountMax = groupCountMax;
                                data.biomes = biomes;
                                data.isWater = isWater;
                                data.spawnChancePriority = spawnChancePriority;
                                data.maxEntitiesPerChunk = maxEntitiesPerChunk;
                                populationList.add(data);

                                if (DEBUG_AND_CHECK_SYNTAX)
                                {
                                    Log.write(0, "Added mob data to population list: " + id);
                                }

                                Biome.SpawnListEntry entry = new Biome.SpawnListEntry(
                                        (Class<? extends EntityLiving>) _class,
                                        weight, groupCountMin, groupCountMax
                                );

                                newSpawnEntries.add(entry);

                                if (DEBUG_AND_CHECK_SYNTAX)
                                {
                                    Log.write(0, "Added spawn list entry for mob: " + id);
                                }
                            }
                            catch (Exception exception)
                            {
                                Log.write(2, "Error processing mob entry: " + exception.getMessage());

                                if (DEBUG_AND_CHECK_SYNTAX)
                                {
                                    exception.printStackTrace();
                                }
                            }
                        }
                    }
                }
                catch (Exception exception)
                {
                    Log.write(2, "Error processing top-level entry: " + exception.getMessage());

                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        exception.printStackTrace();
                    }
                }
            }

            GeneralPopulationChunkSpawn.getInstance().populationChunkStruct = populationList;

            Log.write(0, "Config parsing completed successfully. Added " +
                    populationList.size() + " mob entries to population chunk struct.");
        }
        catch (IOException exception)
        {
            Log.write(2, "IO error reading config: " + exception.getMessage());

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                exception.printStackTrace();
            }
        }
        catch (JsonSyntaxException exception)
        {
            Log.write(2, "JSON syntax error in config: " + exception.getMessage());

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                exception.printStackTrace();
            }
        }
        catch (Exception exception)
        {
            Log.write(2, "Unexpected error loading config: " + exception.getMessage());

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void eraseData()
    {
        if (DEBUG_AND_CHECK_SYNTAX)
        {
            int count = GeneralPopulationChunkSpawn.getInstance().populationChunkStruct.size();
            Log.write(0, "Clearing population chunk data (" + count + " entries)");
        }

        GeneralPopulationChunkSpawn.getInstance().populationChunkStruct.clear();

        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "Population chunk data cleared successfully");
        }
    }
}