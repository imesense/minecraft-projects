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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@InitLog
public final class ParserEventPotentialSpawn extends BaseParser
{
    private File baseFile;

    public ParserEventPotentialSpawn(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;
        Log.write(0, "ParserEventPotentialSpawn initialized with config file: " + NAME_FILE);
    }

    private JsonObject loadIncludedFile(String includePath, File currentFile) throws IOException
    {
        Log.write(0, "Loading included file: " + includePath);
        Path includeFilePath = resolveIncludePath(includePath, currentFile);
        Log.write(0, "  Resolved path: " + includeFilePath);

        if (!Files.exists(includeFilePath))
        {
            Log.write(0, "  ERROR: Included file not found");
            throw new IOException("Included file not found: " + includeFilePath);
        }

        Log.write(0, "  File exists, parsing...");
        try (FileReader reader = new FileReader(includeFilePath.toFile()))
        {
            Gson gson = new Gson();
            JsonElement jsonElement = gson.fromJson(reader, JsonElement.class);

            JsonObject includedJson;
            if (jsonElement.isJsonArray())
            {
                Log.write(0, "  JSON is an array, extracting first element");
                JsonArray jsonArray = jsonElement.getAsJsonArray();

                if (jsonArray.size() != 1)
                {
                    throw new IOException("Included file must contain exactly one JSON object in array");
                }

                includedJson = jsonArray.get(0).getAsJsonObject();

            }
            else if (jsonElement.isJsonObject())
            {
                Log.write(0, "  JSON is an object (legacy format)");
                includedJson = jsonElement.getAsJsonObject();
            }
            else
            {
                throw new IOException("Invalid JSON format in included file");
            }

            Log.write(0, "  File parsed successfully");
            return processIncludes(includedJson, includeFilePath.toFile());
        }
    }

    private Path resolveIncludePath(String includePath, File currentFile)
    {
        Log.write(0, "Resolving include path: " + includePath);

        if (includePath.startsWith("./") || includePath.startsWith("../"))
        {
            Path currentDir = currentFile.toPath().getParent();
            Path resolved = currentDir.resolve(includePath).normalize();
            Log.write(0, "  Relative path, resolved to: " + resolved);
            return resolved;
        }
        else if (!includePath.contains("/") || includePath.startsWith("includes/"))
        {
            File scriptsDir = getConfigFile(true,
                    DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, "");
            Path includesDir = scriptsDir.toPath().resolve("includes");
            Path resolved = includesDir.resolve(includePath).normalize();
            Log.write(0, "  Includes directory path, resolved to: " + resolved);
            return resolved;
        }
        else
        {
            File scriptsDir = getConfigFile(true,
                    DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, "");
            Path resolved = scriptsDir.toPath().resolve(includePath).normalize();
            Log.write(0, "  Absolute/root path, resolved to: " + resolved);
            return resolved;
        }
    }

    private JsonObject processIncludes(JsonObject jsonObject, File currentFile) throws IOException
    {
        Log.write(0, "Processing includes...");
        JsonObject result = new JsonObject();

        if (jsonObject.has("#include"))
        {
            JsonElement includeElement = jsonObject.get("#include");
            Log.write(0, "  Found top-level #include directive");

            if (includeElement.isJsonPrimitive())
            {
                String includeFile = includeElement.getAsString();
                Log.write(0, "  Single file include: " + includeFile);
                JsonObject included = loadIncludedFile(includeFile, currentFile);
                mergeJsonObjects(result, included);
            }
            else if (includeElement.isJsonArray())
            {
                JsonArray includeArray = includeElement.getAsJsonArray();
                Log.write(0, "  Multiple files include, count: " + includeArray.size());

                for (JsonElement element : includeArray)
                {
                    String includeFile = element.getAsString();
                    Log.write(0, "    Including file: " + includeFile);
                    JsonObject included = loadIncludedFile(includeFile, currentFile);
                    mergeJsonObjects(result, included);
                }
            }
            Log.write(0, "  Includes processed, result size: " + result.size());
            return result;
        }

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet())
        {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            Log.write(0, "  Processing key: " + key);

            if (key.equals("#include"))
            {
                continue;
            }

            if (value.isJsonObject() && value.getAsJsonObject().has("#include"))
            {
                Log.write(0, "    Found nested #include");
                String includeFile = value.getAsJsonObject().get("#include").getAsString();
                JsonObject included = loadIncludedFile(includeFile, currentFile);
                result.add(key, included);
                Log.write(0, "    Nested include processed");
            }
            else if (value.isJsonObject())
            {
                Log.write(0, "    Recursively processing object");
                result.add(key, processIncludes(value.getAsJsonObject(), currentFile));
            }
            else if (value.isJsonArray())
            {
                JsonArray array = value.getAsJsonArray();
                JsonArray resultArray = new JsonArray();
                Log.write(0, "    Processing array, size: " + array.size());

                for (JsonElement element : array)
                {
                    if (element.isJsonObject() && element.getAsJsonObject().has("#include"))
                    {
                        String includeFile = element.getAsJsonObject().get("#include").getAsString();
                        Log.write(0, "      Array element has #include: " + includeFile);
                        JsonObject included = loadIncludedFile(includeFile, currentFile);
                        resultArray.add(included);
                    }
                    else
                    {
                        resultArray.add(element);
                    }
                }

                result.add(key, resultArray);
                Log.write(0, "    Array processed, result size: " + resultArray.size());
            }
            else
            {
                result.add(key, value);
            }
        }

        Log.write(0, "All includes processed");
        return result;
    }

    private void mergeJsonObjects(JsonObject target, JsonObject source)
    {
        Log.write(0, "Merging JSON objects");
        Log.write(0, "  Target keys: " + getJsonKeys(target));
        Log.write(0, "  Source keys: " + getJsonKeys(source));

        for (Map.Entry<String, JsonElement> entry : source.entrySet())
        {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            Log.write(0, "  Processing key: " + key);

            if (target.has(key))
            {
                Log.write(0, "    Key already exists in target");
                JsonElement existing = target.get(key);

                if (existing.isJsonArray() && value.isJsonArray())
                {
                    JsonArray targetArray = existing.getAsJsonArray();
                    JsonArray sourceArray = value.getAsJsonArray();
                    Log.write(0, "    Merging arrays, target size: " + targetArray.size() +
                            ", source size: " + sourceArray.size());

                    for (JsonElement elem : sourceArray)
                    {
                        targetArray.add(elem);
                    }
                    Log.write(0, "    Merged array size: " + targetArray.size());
                }
                else if (existing.isJsonObject() && value.isJsonObject())
                {
                    Log.write(0, "    Recursively merging objects");
                    JsonObject mergedObject = existing.getAsJsonObject();
                    mergeJsonObjects(mergedObject, value.getAsJsonObject());
                    target.add(key, mergedObject);
                }
                else
                {
                    Log.write(0, "    Replacing value");
                    target.add(key, value);
                }
            }
            else
            {
                Log.write(0, "    Adding new key");
                target.add(key, value);
            }
        }
    }

    private Set<String> getJsonKeys(JsonObject jsonObject)
    {
        Set<String> keys = new HashSet<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet())
        {
            keys.add(entry.getKey());
        }
        return keys;
    }

    private void removeIncludeDirectives(JsonObject jsonObject)
    {
        Log.write(0, "Removing #include directives...");

        if (jsonObject.has("#include"))
        {
            jsonObject.remove("#include");
            Log.write(0, "  Removed top-level #include");
        }

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet())
        {
            if (entry.getValue().isJsonObject())
            {
                removeIncludeDirectives(entry.getValue().getAsJsonObject());
            }
            else if (entry.getValue().isJsonArray())
            {
                JsonArray array = entry.getValue().getAsJsonArray();
                for (JsonElement element : array)
                {
                    if (element.isJsonObject())
                    {
                        removeIncludeDirectives(element.getAsJsonObject());
                    }
                }
            }
        }
    }

    @Override
    public void loadConfig(boolean init)
    {
        Log.write(0, "================================================");
        Log.write(0, "Loading config for PotentialSpawn parser");
        Log.write(0, "File: " + this.nameFile);
        Log.write(0, "First time: " + init);
        Log.write(0, "================================================");

        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);
        this.baseFile = file;

        Log.write(0, "Config file path: " + file.getAbsolutePath());

        if (!file.exists())
        {
            Log.write(0, "Config file not found, creating new: " + file);
            this.createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            Log.write(0, "Reading and parsing config file...");
            long startTime = System.currentTimeMillis();

            Gson gson = new Gson();
            JsonElement jsonElement = gson.fromJson(fileReader, JsonElement.class);

            JsonObject jsonObject;
            if (jsonElement.isJsonArray())
            {
                Log.write(0, "JSON is an array, extracting object...");
                JsonArray jsonArray = jsonElement.getAsJsonArray();

                if (jsonArray.size() != 1)
                {
                    throw new RuntimeException("Expected exactly one JSON object in array, found " + jsonArray.size() + " elements");
                }

                jsonObject = jsonArray.get(0).getAsJsonObject();
            }
            else if (jsonElement.isJsonObject())
            {
                Log.write(0, "JSON is an object (legacy format), processing directly...");
                jsonObject = jsonElement.getAsJsonObject();
            }
            else
            {
                throw new RuntimeException("Invalid JSON format: expected object or array");
            }

            Log.write(0, "Processing includes...");
            JsonObject processedJson = processIncludes(jsonObject, file);

            removeIncludeDirectives(processedJson);
            Log.write(0, "Include directives removed");

            Log.write(0, "Processing JSON structure...");
            processJsonObject(processedJson);

            long endTime = System.currentTimeMillis();
            Log.write(0, "Config loaded successfully in " + (endTime - startTime) + "ms");
            Log.write(0, "================================================");
            Log.write(0, "");
        }
        catch (IOException exception)
        {
            Log.write(2, "IO error reading config: " + exception.getMessage());
            exception.printStackTrace();
        }
        catch (JsonSyntaxException exception)
        {
            Log.write(2, "JSON syntax error in config: " + exception.getMessage());
            exception.printStackTrace();
        }
        catch (RuntimeException exception)
        {
            Log.write(2, "Error loading config: " + exception.getMessage());
            exception.printStackTrace();
        }
        catch (Exception exception)
        {
            Log.write(2, "Unexpected error loading config: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void processJsonObject(JsonObject jsonObject) throws RuntimeException
    {
        Log.write(0, "Processing JSON object structure...");
        Log.write(0, "JSON keys: " + getJsonKeys(jsonObject));
        Log.write(0, "========================================");

        List<Biome.SpawnListEntry> newSpawnEntries = new ArrayList<>();
        List<PotentialSpawnStruct.Data> newSecondaryParameters = new ArrayList<>();

        if (jsonObject.has("mobs"))
        {
            Log.write(0, "Found 'mobs' section");
            processMobsSection(jsonObject.getAsJsonArray("mobs"), newSpawnEntries, newSecondaryParameters);
        }
        else
        {
            Log.write(0, "No 'mobs' section found in JSON");
        }

        GeneralPotentialSpawnStorage.getInstance().spawnEntries = newSpawnEntries;
        GeneralPotentialSpawnStorage.getInstance().potentialSpawnStruct = newSecondaryParameters;

        Log.write(0, String.format(
                "Processing completed. Added %d spawn entries and %d parameter sets",
                newSpawnEntries.size(), newSecondaryParameters.size()
        ));
        Log.write(0, "========================================");
        Log.write(0, "");
    }

    private void processMobsSection(JsonArray mobsArray,
                                    List<Biome.SpawnListEntry> spawnEntries,
                                    List<PotentialSpawnStruct.Data> secondaryParams)
    {
        Log.write(0, "Processing mobs array...");
        Log.write(0, "Number of mob entries: " + mobsArray.size());
        Log.write(0, "========================================");
        Log.write(0, "");

        for (int i = 0; i < mobsArray.size(); i++)
        {
            Log.write(0, "Processing mob entry " + (i + 1) + " of " + mobsArray.size());
            Log.write(0, "----------------------------------------");

            try
            {
                JsonObject mobMap = mobsArray.get(i).getAsJsonObject();
                String id = mobMap.get("mob").getAsString();
                Log.write(0, "  Mob ID: " + id);

                ResourceLocation mobResource = new ResourceLocation(id);
                EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(mobResource);

                if (entityEntry == null)
                {
                    Log.write(2, "  ERROR: Mob not found in registry: " + id);
                    continue;
                }

                Class<? extends Entity> _class = entityEntry.getEntityClass();
                if (_class == null)
                {
                    Log.write(2, "  ERROR: Entity class not found for mob: " + id);
                    continue;
                }

                Integer weight = mobMap.has("weight") ? mobMap.get("weight").getAsInt() : 1;
                Integer idDimension = mobMap.has("id_dimension") ? mobMap.get("id_dimension").getAsInt() : null;
                Integer groupCountMin = mobMap.has("groupcountmin") ? mobMap.get("groupcountmin").getAsInt() : 1;
                Integer groupCountMax = mobMap.has("groupcountmax") ?
                        mobMap.get("groupcountmax").getAsInt() : Math.max(groupCountMin, 1);

                Log.write(0, String.format(
                        "  Settings: weight = %d, groupCount = %d - %d, dimension = %s",
                        weight, groupCountMin, groupCountMax,
                        idDimension != null ? idDimension.toString() : "any"
                ));

                PotentialSpawnStruct.Data data = new PotentialSpawnStruct.Data();
                data.minHeight = mobMap.has("min_height") ? mobMap.get("min_height").getAsFloat() : 1.0f;
                data.maxHeight = mobMap.has("max_height") ? mobMap.get("max_height").getAsFloat() : 255.0f;
                data.spawnChance = mobMap.has("spawn_chance") ? mobMap.get("spawn_chance").getAsFloat() : 0.01f;
                data.idDimension = idDimension;

                Log.write(0, String.format(
                        "  Spawn params: chance = %.2f, height = %.1f - %.1f",
                        data.spawnChance, data.minHeight, data.maxHeight
                ));

                Biome.SpawnListEntry entry = new Biome.SpawnListEntry(
                        (Class<? extends EntityLiving>) _class,
                        weight, groupCountMin, groupCountMax
                );

                spawnEntries.add(entry);
                secondaryParams.add(data);

                Log.write(0, String.format(
                        "  Entity [%s] added to spawn list successfully",
                        entityEntry.getName()
                ));

                Log.write(0, "----------------------------------------");
                Log.write(0, "Mob entry " + (i + 1) + " processed");
                Log.write(0, "");
            }
            catch (Exception exception)
            {
                Log.write(2, "  ERROR processing mob entry " + (i + 1) + ": " + exception.getMessage());
                Log.write(0, "----------------------------------------");
                Log.write(0, "");
            }
        }

        Log.write(0, "Mobs array processing completed");
        Log.write(0, "========================================");
        Log.write(0, "");
    }

    @Override
    public void eraseData()
    {
        Log.write(0, "================================================");
        Log.write(0, "Clearing all potential spawn data...");

        int spawnEntriesCount = GeneralPotentialSpawnStorage.getInstance().spawnEntries.size();
        int paramSetsCount = GeneralPotentialSpawnStorage.getInstance().potentialSpawnStruct.size();

        Log.write(0, "  Spawn entries: " + spawnEntriesCount);
        Log.write(0, "  Parameter sets: " + paramSetsCount);

        GeneralPotentialSpawnStorage.getInstance().spawnEntries.clear();
        GeneralPotentialSpawnStorage.getInstance().potentialSpawnStruct.clear();

        Log.write(0, "Potential spawn data cleared successfully");
        Log.write(0, "================================================");
        Log.write(0, "");
    }
}