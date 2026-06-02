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
    }

    private JsonObject loadIncludedFile(String includePath, File currentFile) throws IOException
    {
        Path includeFilePath = resolveIncludePath(includePath, currentFile);

        if (!Files.exists(includeFilePath))
        {
            throw new IOException("Included file not found: " + includeFilePath);
        }

        try (FileReader reader = new FileReader(includeFilePath.toFile()))
        {
            Gson gson = new Gson();
            JsonElement jsonElement = gson.fromJson(reader, JsonElement.class);

            JsonObject includedJson;
            if (jsonElement.isJsonArray())
            {
                JsonArray jsonArray = jsonElement.getAsJsonArray();

                if (jsonArray.size() != 1)
                {
                    throw new IOException("Included file must contain exactly one JSON object in array");
                }

                includedJson = jsonArray.get(0).getAsJsonObject();

            }
            else if (jsonElement.isJsonObject())
            {
                includedJson = jsonElement.getAsJsonObject();
            }
            else
            {
                throw new IOException("Invalid JSON format in included file");
            }

            return processIncludes(includedJson, includeFilePath.toFile());
        }
    }

    private Path resolveIncludePath(String includePath, File currentFile)
    {
        if (includePath.startsWith("./") || includePath.startsWith("../"))
        {
            Path currentDir = currentFile.toPath().getParent();
            Path resolved = currentDir.resolve(includePath).normalize();

            return resolved;
        }
        else if (!includePath.contains("/") || includePath.startsWith("includes/"))
        {
            File scriptsDir = getConfigFile(true,
                    DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, "");

            Path includesDir = scriptsDir.toPath().resolve("includes");
            Path resolved = includesDir.resolve(includePath).normalize();

            return resolved;
        }
        else
        {
            File scriptsDir = getConfigFile(true,
                    DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, "");

            Path resolved = scriptsDir.toPath().resolve(includePath).normalize();

            return resolved;
        }
    }

    private JsonObject processIncludes(JsonObject jsonObject, File currentFile) throws IOException
    {
        JsonObject result = new JsonObject();

        if (jsonObject.has("#include"))
        {
            JsonElement includeElement = jsonObject.get("#include");

            if (includeElement.isJsonPrimitive())
            {
                String includeFile = includeElement.getAsString();
                JsonObject included = loadIncludedFile(includeFile, currentFile);
                mergeJsonObjects(result, included);
            }
            else if (includeElement.isJsonArray())
            {
                JsonArray includeArray = includeElement.getAsJsonArray();

                for (JsonElement element : includeArray)
                {
                    String includeFile = element.getAsString();
                    JsonObject included = loadIncludedFile(includeFile, currentFile);
                    mergeJsonObjects(result, included);
                }
            }

            return result;
        }

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet())
        {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            if (key.equals("#include"))
            {
                continue;
            }

            if (value.isJsonObject() && value.getAsJsonObject().has("#include"))
            {
                String includeFile = value.getAsJsonObject().get("#include").getAsString();
                JsonObject included = loadIncludedFile(includeFile, currentFile);
                result.add(key, included);
            }
            else if (value.isJsonObject())
            {
                result.add(key, processIncludes(value.getAsJsonObject(), currentFile));
            }
            else if (value.isJsonArray())
            {
                JsonArray array = value.getAsJsonArray();
                JsonArray resultArray = new JsonArray();

                for (JsonElement element : array)
                {
                    if (element.isJsonObject() && element.getAsJsonObject().has("#include"))
                    {
                        String includeFile = element.getAsJsonObject().get("#include").getAsString();
                        JsonObject included = loadIncludedFile(includeFile, currentFile);
                        resultArray.add(included);
                    }
                    else
                    {
                        resultArray.add(element);
                    }
                }

                result.add(key, resultArray);
            }
            else
            {
                result.add(key, value);
            }
        }

        return result;
    }

    private void mergeJsonObjects(JsonObject target, JsonObject source)
    {
        for (Map.Entry<String, JsonElement> entry : source.entrySet())
        {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            if (target.has(key))
            {
                JsonElement existing = target.get(key);

                if (existing.isJsonArray() && value.isJsonArray())
                {
                    JsonArray targetArray = existing.getAsJsonArray();
                    JsonArray sourceArray = value.getAsJsonArray();

                    for (JsonElement elem : sourceArray)
                    {
                        targetArray.add(elem);
                    }
                }
                else if (existing.isJsonObject() && value.isJsonObject())
                {
                    JsonObject mergedObject = existing.getAsJsonObject();
                    mergeJsonObjects(mergedObject, value.getAsJsonObject());
                    target.add(key, mergedObject);
                }
                else
                {
                    target.add(key, value);
                }
            }
            else
            {
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
        if (jsonObject.has("#include"))
        {
            jsonObject.remove("#include");
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
        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);
        this.baseFile = file;

        if (!file.exists())
        {
            this.createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            Gson gson = new Gson();
            JsonElement jsonElement = gson.fromJson(fileReader, JsonElement.class);

            JsonObject jsonObject;

            if (jsonElement.isJsonArray())
            {
                JsonArray jsonArray = jsonElement.getAsJsonArray();

                if (jsonArray.size() != 1)
                {
                    throw new RuntimeException("Expected exactly one JSON object in array, found " + jsonArray.size() + " elements");
                }

                jsonObject = jsonArray.get(0).getAsJsonObject();
            }
            else if (jsonElement.isJsonObject())
            {
                jsonObject = jsonElement.getAsJsonObject();
            }
            else
            {
                throw new RuntimeException("Invalid JSON format: expected object or array");
            }

            JsonObject processedJson = processIncludes(jsonObject, file);

            removeIncludeDirectives(processedJson);

            processJsonObject(processedJson);
        }
        catch (IOException exception)
        {

        }
        catch (JsonSyntaxException exception)
        {

        }
        catch (RuntimeException exception)
        {
            exception.printStackTrace();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void processJsonObject(JsonObject jsonObject) throws RuntimeException
    {
        List<Biome.SpawnListEntry> newSpawnEntries = new ArrayList<>();
        List<PotentialSpawnStruct.Data> newSecondaryParameters = new ArrayList<>();

        if (jsonObject.has("mobs"))
        {
            processMobsSection(jsonObject.getAsJsonArray("mobs"), newSpawnEntries, newSecondaryParameters);
        }

        GeneralPotentialSpawnStorage.getInstance().spawnEntries = newSpawnEntries;
        GeneralPotentialSpawnStorage.getInstance().potentialSpawnStruct = newSecondaryParameters;
    }

    private void processMobsSection(JsonArray mobsArray,
                                    List<Biome.SpawnListEntry> spawnEntries,
                                    List<PotentialSpawnStruct.Data> secondaryParams)
    {
        for (int i = 0; i < mobsArray.size(); i++)
        {
            try
            {
                JsonObject mobMap = mobsArray.get(i).getAsJsonObject();
                String id = mobMap.get("mob").getAsString();

                ResourceLocation mobResource = new ResourceLocation(id);
                EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(mobResource);

                if (entityEntry == null)
                {
                    continue;
                }

                Class<? extends Entity> _class = entityEntry.getEntityClass();

                if (_class == null)
                {
                    continue;
                }

                Integer weight = mobMap.has("weight") ? mobMap.get("weight").getAsInt() : 1;
                Integer idDimension = mobMap.has("id_dimension") ? mobMap.get("id_dimension").getAsInt() : null;
                Integer groupCountMin = mobMap.has("groupcountmin") ? mobMap.get("groupcountmin").getAsInt() : 1;
                Integer groupCountMax = mobMap.has("groupcountmax") ?
                        mobMap.get("groupcountmax").getAsInt() : Math.max(groupCountMin, 1);

                PotentialSpawnStruct.Data data = new PotentialSpawnStruct.Data();
                data.minHeight = mobMap.has("min_height") ? mobMap.get("min_height").getAsFloat() : 1.0f;
                data.maxHeight = mobMap.has("max_height") ? mobMap.get("max_height").getAsFloat() : 255.0f;
                data.spawnChance = mobMap.has("spawn_chance") ? mobMap.get("spawn_chance").getAsFloat() : 0.01f;
                data.idDimension = idDimension;

                Biome.SpawnListEntry entry = new Biome.SpawnListEntry(
                        (Class<? extends EntityLiving>) _class,
                        weight, groupCountMin, groupCountMax
                );

                spawnEntries.add(entry);
                secondaryParams.add(data);
            }
            catch (Exception exception)
            {

            }
        }
    }

    @Override
    public void eraseData()
    {
        GeneralPotentialSpawnStorage.getInstance().spawnEntries.clear();
        GeneralPotentialSpawnStorage.getInstance().potentialSpawnStruct.clear();
    }
}
