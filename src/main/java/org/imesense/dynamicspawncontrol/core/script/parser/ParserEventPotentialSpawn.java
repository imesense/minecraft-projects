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
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.data.PotentialSpawnStruct;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.storage.GeneralPotentialSpawnStorage;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@InitLog
public final class ParserEventPotentialSpawn extends BaseParser
{
    private static final boolean DEBUG_AND_CHECK_SYNTAX = true;

    public ParserEventPotentialSpawn(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;

        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "ParserEventPotentialSpawn initialized with config file: " + NAME_FILE);
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
                Log.write(0, "Reading and parsing potential spawn configuration");
            }

            JsonArray jsonArray = JsonParser.parseReader(fileReader).getAsJsonArray();

            List<Biome.SpawnListEntry> newSpawnEntries = new ArrayList<>();
            List<PotentialSpawnStruct.Data> newSecondaryParameters = new ArrayList<>();

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "Found " + jsonArray.size() + " top-level configuration entries");
            }

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

                                Integer weight = mobMap.has("weight") ? mobMap.get("weight").getAsInt() : 1;
                                Integer groupCountMin = mobMap.has("groupcountmin") ? mobMap.get("groupcountmin").getAsInt() : 1;
                                Integer groupCountMax = mobMap.has("groupcountmax") ? mobMap.get("groupcountmax").getAsInt() : Math.max(groupCountMin, 1);

                                if (DEBUG_AND_CHECK_SYNTAX)
                                {
                                    Log.write(0, String.format(
                                            "Mob %s settings: weight=%d, groupCount=%d-%d",
                                            id, weight, groupCountMin, groupCountMax
                                    ));
                                }

                                PotentialSpawnStruct.Data data = new PotentialSpawnStruct.Data();
                                data.spawnChance = mobMap.has("spawn_chance") ? mobMap.get("spawn_chance").getAsFloat() : 0.01f;
                                data.minHeight = mobMap.has("min_height") ? mobMap.get("min_height").getAsFloat() : 1.0f;
                                data.maxHeight = mobMap.has("max_height") ? mobMap.get("max_height").getAsFloat() : 255.0f;

                                if (DEBUG_AND_CHECK_SYNTAX)
                                {
                                    Log.write(0, String.format(
                                            "Spawn parameters: chance=%.2f, height=%.1f-%.1f",
                                            data.spawnChance, data.minHeight, data.maxHeight
                                    ));
                                }

                                Biome.SpawnListEntry entry = new Biome.SpawnListEntry(
                                        (Class<? extends EntityLiving>) _class,
                                        weight, groupCountMin, groupCountMax
                                );

                                newSpawnEntries.add(entry);
                                newSecondaryParameters.add(data);

                                if (DEBUG_AND_CHECK_SYNTAX)
                                {
                                    Log.write(0, "Successfully added mob to spawn lists: " + id);
                                }

                                Log.write(0, String.format(
                                        "Entity [%s:%s] has been added to the spawn list. " +
                                                "Data -> SpawnChance [%.2f], " +
                                                "Weight [%d], " +
                                                "Group min [%d], " +
                                                "Group max [%d], " +
                                                "Height [%.1f-%.1f]",
                                        entry, id, data.spawnChance, weight,
                                        groupCountMin, groupCountMax,
                                        data.minHeight, data.maxHeight));
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

            GeneralPotentialSpawnStorage.getInstance().spawnEntries = newSpawnEntries;
            GeneralPotentialSpawnStorage.getInstance().potentialSpawnStruct = newSecondaryParameters;

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, String.format(
                        "Config parsing completed. Added %d spawn entries and %d parameter sets",
                        newSpawnEntries.size(), newSecondaryParameters.size()
                ));
            }
        }
        catch (FileNotFoundException exception)
        {
            Log.write(2, "Config file not found: " + exception.getMessage());

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                exception.printStackTrace();
            }
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
            int spawnEntriesCount = GeneralPotentialSpawnStorage.getInstance().spawnEntries.size();
            int paramSetsCount = GeneralPotentialSpawnStorage.getInstance().potentialSpawnStruct.size();

            Log.write(0, String.format(
                    "Clearing potential spawn data (%d spawn entries, %d parameter sets)",
                    spawnEntriesCount, paramSetsCount
            ));
        }

        GeneralPotentialSpawnStorage.getInstance().spawnEntries.clear();
        GeneralPotentialSpawnStorage.getInstance().potentialSpawnStruct.clear();

        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "Potential spawn data cleared successfully");
        }
    }
}