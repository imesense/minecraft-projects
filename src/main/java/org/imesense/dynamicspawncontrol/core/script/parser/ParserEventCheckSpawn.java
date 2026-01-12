package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.script.actioncollector.Equipment;
import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.data.*;
import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.datasupport.AdditionalChecks;
import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.storage.GeneralCheckSpawnStorage;
import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.storagesupport.SupportCheckSpawnStorage;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.imesense.dynamicspawncontrol.core.script.auxscript.Util.*;

@InitLog
@TODO(value = "Merge 'TemplateWithChance' in storage for scripts.", showOnce = false, priority = TODO.TodoPriority.HIGH)
public final class ParserEventCheckSpawn extends BaseParser
{
    private File baseFile;

    private static final String[] SECTION_ORDER = {
            "templates",
            "configs",
            "data_support"
    };

    public ParserEventCheckSpawn(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;
        Log.write(0, "ParserEventCheckSpawn initialized with config file: " + NAME_FILE);
    }

    private static class TemplateWithChance
    {
        JsonElement resolved;
        double chance;

        TemplateWithChance(JsonElement resolved, double chance)
        {
            this.resolved = resolved;
            this.chance = chance;
        }
    }

    private TemplateWithChance resolveTemplateWithChance(JsonElement element, JsonObject templates)
    {
        Log.write(0, "Resolving template with chance...");

        if (element.isJsonPrimitive())
        {
            Log.write(0, "  Type: JsonPrimitive (single template)");
            JsonElement resolved = resolveTemplate(element, templates);
            Log.write(0, "  Chance: 1.0 (100%)");
            return new TemplateWithChance(resolved, 1.0);
        }

        if (element.isJsonArray())
        {
            JsonArray arr = element.getAsJsonArray();
            Log.write(0, "  Type: JsonArray, size: " + arr.size());

            if (arr.size() != 2)
                throw new RuntimeException("Template array must be [template, chance]");

            JsonElement template = resolveTemplate(arr.get(0), templates);
            double chance = arr.get(1).getAsDouble();

            Log.write(0, "  Chance: " + chance + " (" + (chance * 100) + "%)");
            return new TemplateWithChance(template, Math.max(0.0, Math.min(1.0, chance)));
        }

        throw new RuntimeException("Invalid template format: " + element);
    }

    private JsonObject processOrderedIncludes(JsonObject jsonObject, File currentFile) throws IOException
    {
        Log.write(0, "Processing ordered includes...");
        JsonObject result = new JsonObject();

        for (String section : SECTION_ORDER)
        {
            if (jsonObject.has(section))
            {
                Log.write(0, "  Processing section: " + section);
                JsonElement sectionElement = jsonObject.get(section);
                JsonElement processedSection = processSection(section, sectionElement, currentFile);

                if (processedSection != null)
                {
                    result.add(section, processedSection);
                    Log.write(0, "  Section added: " + section);
                }
            }
        }

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet())
        {
            String key = entry.getKey();
            if (!Arrays.asList(SECTION_ORDER).contains(key) && !result.has(key))
            {
                result.add(key, entry.getValue());
                Log.write(0, "  Additional section added: " + key);
            }
        }

        Log.write(0, "Ordered includes processed, total sections: " + result.size());
        return result;
    }

    private JsonElement processSection(String sectionName, JsonElement sectionElement, File currentFile) throws IOException
    {
        Log.write(0, "Processing section: " + sectionName);

        if (sectionElement.isJsonObject())
        {
            JsonObject sectionObj = sectionElement.getAsJsonObject();
            Log.write(0, "  Section is JsonObject");

            if (sectionObj.has("#include"))
            {
                Log.write(0, "  Found #include directive");
                return processIncludeDirective(sectionObj.get("#include"), currentFile, sectionName);
            }
            else
            {
                Log.write(0, "  No #include, processing recursively");
                return processOrderedIncludes(sectionObj, currentFile);
            }
        }
        else if (sectionElement.isJsonArray())
        {
            Log.write(0, "  Section is JsonArray");
            JsonArray sectionArray = sectionElement.getAsJsonArray();
            JsonArray resultArray = new JsonArray();

            for (JsonElement element : sectionArray)
            {
                if (element.isJsonObject() && element.getAsJsonObject().has("#include"))
                {
                    Log.write(0, "  Found #include in array element");
                    JsonObject included = loadIncludedFile(element.getAsJsonObject().get("#include").getAsString(), currentFile);
                    resultArray.add(included);
                }
                else
                {
                    resultArray.add(element);
                }
            }

            Log.write(0, "  Array processed, size: " + resultArray.size());
            return resultArray;
        }

        Log.write(0, "  Section is primitive value");
        return sectionElement;
    }

    private JsonElement processIncludeDirective(JsonElement includeElement, File currentFile, String sectionName) throws IOException
    {
        Log.write(0, "Processing include directive for section: " + sectionName);

        if (includeElement.isJsonPrimitive())
        {
            String includeFile = includeElement.getAsString();
            Log.write(0, "  Single file include: " + includeFile);
            return loadIncludedFileForSection(includeFile, currentFile, sectionName);
        }
        else if (includeElement.isJsonArray())
        {
            JsonArray includeArray = includeElement.getAsJsonArray();
            Log.write(0, "  Multiple files include, count: " + includeArray.size());

            if (sectionName.equals("configs") || sectionName.equals("data_support"))
            {
                Log.write(0, "  Merging arrays for section: " + sectionName);
                JsonArray mergedArray = new JsonArray();

                for (JsonElement element : includeArray)
                {
                    String includeFile = element.getAsString();
                    Log.write(0, "    Including file: " + includeFile);
                    JsonObject included = loadIncludedFile(includeFile, currentFile);

                    if (included.has(sectionName))
                    {
                        JsonElement sectionContent = included.get(sectionName);
                        if (sectionContent.isJsonArray())
                        {
                            int count = sectionContent.getAsJsonArray().size();
                            Log.write(0, "      Adding " + count + " items from section");
                            for (JsonElement item : sectionContent.getAsJsonArray())
                            {
                                mergedArray.add(item);
                            }
                        }
                    }
                    else
                    {
                        Log.write(0, "      Adding entire file as array element");
                        mergedArray.add(included);
                    }
                }

                Log.write(0, "  Total items after merge: " + mergedArray.size());
                return mergedArray;
            }
            else
            {
                Log.write(0, "  Merging objects for section: " + sectionName);
                JsonObject mergedObject = new JsonObject();

                for (JsonElement element : includeArray)
                {
                    String includeFile = element.getAsString();
                    Log.write(0, "    Including file: " + includeFile);
                    JsonObject included = loadIncludedFile(includeFile, currentFile);

                    if (included.has(sectionName))
                    {
                        mergeJsonObjects(mergedObject, included.get(sectionName).getAsJsonObject());
                        Log.write(0, "      Merged section content");
                    }
                    else
                    {
                        mergeJsonObjects(mergedObject, included);
                        Log.write(0, "      Merged entire file");
                    }
                }

                return mergedObject;
            }
        }

        Log.write(0, "  Include directive is empty");
        return JsonNull.INSTANCE;
    }

    private JsonElement loadIncludedFileForSection(String includePath, File currentFile, String sectionName) throws IOException
    {
        Log.write(0, "Loading included file for section: " + sectionName);
        Log.write(0, "  Path: " + includePath);

        JsonObject included = loadIncludedFile(includePath, currentFile);

        if (included.has(sectionName))
        {
            Log.write(0, "  Found section in included file");
            return included.get(sectionName);
        }

        Log.write(0, "  Section not found, returning entire file");
        return included;
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
            JsonObject includedJson = gson.fromJson(reader, JsonObject.class);
            Log.write(0, "  File parsed successfully, processing nested includes");

            return processOrderedIncludes(includedJson, includeFilePath.toFile());
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

    private Set<String> getJsonKeys(JsonObject jsonObject) {
        Set<String> keys = new HashSet<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            keys.add(entry.getKey());
        }
        return keys;
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

    private JsonObject loadTemplates(JsonObject jsonObject, File currentFile) throws IOException
    {
        Log.write(0, "Loading templates...");
        JsonObject templates = new JsonObject();

        if (jsonObject.has("templates"))
        {
            Log.write(0, "  Found templates section");
            JsonElement templatesElement = jsonObject.get("templates");

            if (templatesElement.isJsonObject())
            {
                templates = templatesElement.getAsJsonObject();
                Log.write(0, "  Templates is JsonObject, size: " + templates.size());

                if (templates.has("#include"))
                {
                    Log.write(0, "  Templates has #include directive");
                    templates = processIncludeDirective(templates.get("#include"), currentFile, "templates").getAsJsonObject();
                    Log.write(0, "  Templates after include processing, size: " + templates.size());
                }
            }
            else
            {
                Log.write(0, "  Templates is not JsonObject, type: " + templatesElement.getClass().getSimpleName());
            }
        }
        else
        {
            Log.write(0, "  No templates section found");
        }

        Log.write(0, "Templates loaded, total templates: " + templates.size());
        return templates;
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

    private List<TemplateWithChance> resolveMultipleTemplatesWithChance(JsonElement element, JsonObject templates)
    {
        Log.write(0, "Resolving multiple templates with chance...");
        List<TemplateWithChance> result = new ArrayList<>();

        if (element.isJsonPrimitive())
        {
            Log.write(0, "  Type: JsonPrimitive");
            String templateString = element.getAsString();
            String[] templateNames = templateString.split("\\s*,\\s*");
            Log.write(0, "  Found " + templateNames.length + " template(s) in string");

            for (String templateName : templateNames)
            {
                templateName = templateName.trim();
                if (!templateName.isEmpty())
                {
                    Log.write(0, "    Processing template: " + templateName);
                    result.add(new TemplateWithChance(
                            resolveTemplate(new JsonPrimitive(templateName), templates),
                            1.0
                    ));
                }
            }
        }
        else if (element.isJsonArray())
        {
            JsonArray arr = element.getAsJsonArray();
            Log.write(0, "  Type: JsonArray, size: " + arr.size());

            if (arr.size() == 2 && arr.get(0).isJsonPrimitive())
            {
                Log.write(0, "  Format: [\"template1, template2\", chance]");
                String templateString = arr.get(0).getAsString();
                double commonChance = arr.get(1).getAsDouble();
                Log.write(0, "  Common chance: " + commonChance);

                String[] templateNames = templateString.split("\\s*,\\s*");
                Log.write(0, "  Found " + templateNames.length + " template(s)");

                for (String templateName : templateNames)
                {
                    templateName = templateName.trim();
                    if (!templateName.isEmpty())
                    {
                        Log.write(0, "    Processing template: " + templateName);
                        result.add(new TemplateWithChance(
                                resolveTemplate(new JsonPrimitive(templateName), templates),
                                commonChance
                        ));
                    }
                }
            }
            else if (arr.size() > 0 && arr.get(0).isJsonArray())
            {
                Log.write(0, "  Format: [[template1, chance1], [template2, chance2]]");
                Log.write(0, "  Found " + arr.size() + " template entries");

                for (int i = 0; i < arr.size(); i++)
                {
                    JsonElement subElement = arr.get(i);
                    if (subElement.isJsonArray())
                    {
                        JsonArray subArr = subElement.getAsJsonArray();
                        if (subArr.size() == 2)
                        {
                            JsonElement templateElement = subArr.get(0);
                            double chance = subArr.get(1).getAsDouble();
                            Log.write(0, "    Entry " + (i+1) + ": chance = " + chance);

                            result.add(new TemplateWithChance(
                                    resolveTemplate(templateElement, templates),
                                    chance
                            ));
                        }
                    }
                }
            }
            else
            {
                if (arr.size() == 2)
                {
                    Log.write(0, "  Warning: Using old [template, chance] format");
                    Log.write(0, "  Consider using new format for multiple templates");

                    JsonElement templateElement = arr.get(0);
                    double chance = arr.get(1).getAsDouble();
                    Log.write(0, "  Chance: " + chance);

                    result.add(new TemplateWithChance(
                            resolveTemplate(templateElement, templates),
                            chance
                    ));
                }
            }
        }

        Log.write(0, "Total templates resolved: " + result.size());
        return result;
    }

    @Override
    public void loadConfig(boolean init)
    {
        Log.write(0, "================================================");
        Log.write(0, "Loading config for CheckSpawn parser");
        Log.write(0, "File: " + this.nameFile);
        Log.write(0, "First time: " + init);
        Log.write(0, "================================================");

        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);
        this.baseFile = file;
        Log.write(0, "Config file path: " + file.getAbsolutePath());

        if (!file.exists())
        {
            Log.write(0, "Config file not found, creating new file");
            this.createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            Log.write(0, "Reading and parsing config file...");
            long startTime = System.currentTimeMillis();

            Gson gson = new Gson();
            JsonElement jsonElement = gson.fromJson(fileReader, JsonElement.class);

            if (jsonElement.isJsonArray())
            {
                Log.write(0, "JSON is an array, processing as array...");
                JsonArray jsonArray = jsonElement.getAsJsonArray();

                if (jsonArray.size() == 1)
                {
                    JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
                    processJsonFile(jsonObject, file);
                }
                else
                {
                    throw new RuntimeException("Expected single JSON object in array, found " + jsonArray.size() + " elements");
                }
            }
            else if (jsonElement.isJsonObject())
            {
                Log.write(0, "JSON is an object, processing as object...");
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                processJsonFile(jsonObject, file);
            }
            else
            {
                throw new RuntimeException("Invalid JSON format: expected object or array");
            }

            long endTime = System.currentTimeMillis();
            Log.write(0, "Config loaded successfully in " + (endTime - startTime) + "ms");
            Log.write(0, "================================================");
            Log.write(0, "");
        }
        catch (JsonSyntaxException | IOException exception)
        {
            handleLoadError("Error loading script file: " + exception.getMessage(), exception);
        }
        catch (RuntimeException exception)
        {
            handleLoadError(exception.getMessage(), exception);
        }
    }

    private void processJsonFile(JsonObject jsonObject, File file) throws IOException
    {
        Log.write(0, "JSON parsed successfully");

        Log.write(0, "Loading templates...");
        JsonObject templates = loadTemplates(jsonObject, file);

        Log.write(0, "Processing ordered includes...");
        JsonObject processedJson = processOrderedIncludes(jsonObject, file);

        processedJson.add("templates", templates);
        Log.write(0, "Templates added to processed JSON");

        removeIncludeDirectives(processedJson);
        Log.write(0, "Include directives removed");

        Log.write(0, "Processing JSON object...");
        processJsonObject(processedJson);
    }

    private void processJsonObject(JsonObject jsonObject) throws RuntimeException
    {
        Log.write(0, "Processing JSON object structure...");
        Log.write(0, "========================================");
        Log.write(0, "");

        JsonObject templates = jsonObject.has("templates") ? jsonObject.getAsJsonObject("templates") : new JsonObject();
        Log.write(0, "Templates available: " + templates.size());

        if (jsonObject.has("configs"))
        {
            Log.write(0, "Found configs section");
            processConfigs(jsonObject.getAsJsonArray("configs"), templates);
        }
        else
        {
            Log.write(0, "No configs section found");
        }

        if (jsonObject.has("data_support"))
        {
            Log.write(0, "Found data_support section");
            processDataSupport(jsonObject.getAsJsonArray("data_support"));
        }
        else
        {
            Log.write(0, "No data_support section found");
        }

        Log.write(0, "JSON object processing completed");
        Log.write(0, "========================================");
        Log.write(0, "");
    }

    private void processConfigs(JsonArray configs, JsonObject templates) throws RuntimeException
    {
        Log.write(0, "Processing configs...");
        Log.write(0, "Number of configs: " + configs.size());
        Log.write(0, "========================================");
        Log.write(0, "");

        for (int i = 0; i < configs.size(); i++)
        {
            Log.write(0, "Processing config " + (i + 1) + " of " + configs.size());
            Log.write(0, "----------------------------------------");

            JsonObject dataObject = configs.get(i).getAsJsonObject().getAsJsonObject("data");

            if (dataObject == null)
            {
                Log.write(0, "ERROR: Key 'data' not found in config " + (i + 1));
                throw new RuntimeException("Key 'data' not found in JSON file.");
            }

            validateRequiredFields(dataObject);
            processDataObject(dataObject, templates);

            Log.write(0, "----------------------------------------");
            Log.write(0, "Config " + (i + 1) + " processed");
            Log.write(0, "");
        }

        Log.write(0, "========================================");
        Log.write(0, "Configs processing completed");
    }

    private void validateRequiredFields(JsonObject dataObject) throws RuntimeException
    {
        Log.write(0, "Validating required fields...");

        if (!dataObject.has("profile") || !dataObject.has("description"))
        {
            Log.write(0, "ERROR: Missing required fields 'profile' or 'description'");
            throw new RuntimeException("Fields 'profile' and 'description' are required in the 'data' section.");
        }

        if (dataObject.get("profile").getAsString().isEmpty() || dataObject.get("description").getAsString().isEmpty())
        {
            Log.write(0, "ERROR: Fields 'profile' and 'description' must not be empty");
            throw new RuntimeException("Fields 'profile' and 'description' must not be empty in the 'data' section.");
        }

        Log.write(0, "Required fields validation passed");
    }

    private void processDataObject(JsonObject dataObject, JsonObject templates)
    {
        String profileName = dataObject.get("profile").getAsString();
        Log.write(0, "Processing data object: " + profileName);
        Log.write(0, "----------------------------------------");
        Log.write(0, "");

        EntityEquipment.Data entityEquipmentData = new EntityEquipment.Data();
        EntityDescription.Data entityDescriptionData = new EntityDescription.Data();
        ProfilePriority.Data profilePriorityData = new ProfilePriority.Data();
        GameWorld.Data gameWorldData = new GameWorld.Data();
        EntityAttributes.Data entityAttributesData = new EntityAttributes.Data();

        if (dataObject.has("potion"))
        {
            Log.write(0, "  Processing potion templates...");
            List<TemplateWithChance> potionTemplates = resolveMultipleTemplatesWithChance(
                    dataObject.get("potion"), templates
            );

            JsonArray mergedPotions = new JsonArray();
            double maxChance = 0.0;

            for (TemplateWithChance template : potionTemplates)
            {
                if (template.resolved.isJsonArray())
                {
                    JsonArray potionArray = template.resolved.getAsJsonArray();
                    Log.write(0, "    Adding " + potionArray.size() + " potion effects from template");
                    for (JsonElement potion : potionArray)
                    {
                        mergedPotions.add(potion);
                    }
                }
                else
                {
                    Log.write(0, "    Adding single potion effect from template");
                    mergedPotions.add(template.resolved);
                }

                maxChance = Math.max(maxChance, template.chance);
            }

            dataObject.add("potion", mergedPotions);
            entityAttributesData.potionChance = maxChance;
            Log.write(0, "  Total potion effects: " + mergedPotions.size() + ", chance: " + maxChance);
        }

        if (dataObject.has("command_nbt"))
        {
            Log.write(0, "  Processing command_nbt...");
            TemplateWithChance nbt = resolveTemplateWithChance(
                    dataObject.get("command_nbt"), templates
            );

            dataObject.add("command_nbt", nbt.resolved);
            entityAttributesData.commandNbtChance = nbt.chance;
            Log.write(0, "  NBT chance: " + nbt.chance);
        }

        entityDescriptionData.profile = profileName;
        entityDescriptionData.description = dataObject.get("description").getAsString();
        Log.write(0, "  Description: " + entityDescriptionData.description);

        String entityTypeString = dataObject.get("entity_type").getAsString();
        Log.write(0, "  Entity type: " + entityTypeString);

        ResourceLocation entityType = new ResourceLocation(entityTypeString);
        EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(entityType);

        if (entityEntry == null)
        {
            Log.write(0, "  ERROR: Mob not found in registry: " + entityTypeString);
            return;
        }

        entityDescriptionData.entityType = entityType;
        Log.write(0, "  Entity validated: " + entityEntry.getName());

        profilePriorityData.priority = dataObject.has("priority") ? dataObject.get("priority").getAsInt() : 0;
        Log.write(0, "  Priority: " + profilePriorityData.priority);

        entityDescriptionData.isArcher = dataObject.has("is_archer") && dataObject.get("is_archer").getAsBoolean();
        Log.write(0, "  Is archer: " + entityDescriptionData.isArcher);

        gameWorldData.seeSky = dataObject.has("see_sky") ? dataObject.get("see_sky").getAsBoolean() : null;
        Log.write(0, "  See sky: " + gameWorldData.seeSky);

        gameWorldData.idDimension = dataObject.has("id_dimension") ? dataObject.get("id_dimension").getAsInt() : null;
        Log.write(0, "  Dimension ID: " + gameWorldData.idDimension);

        entityAttributesData.commandNbt = dataObject.has("command_nbt") ? dataObject.get("command_nbt").toString() : null;

        gameWorldData.maxHeight = dataObject.has("max_height") ? dataObject.get("max_height").getAsInt() : null;
        Log.write(0, "  Max height: " + gameWorldData.maxHeight);

        gameWorldData.minHeight = dataObject.has("min_height") ? dataObject.get("min_height").getAsInt() : null;
        Log.write(0, "  Min height: " + gameWorldData.minHeight);

        entityDescriptionData.name = dataObject.has("name") ? dataObject.get("name").getAsString() : null;
        Log.write(0, "  Custom name: " + entityDescriptionData.name);

        if (dataObject.has("equipment"))
        {
            Log.write(0, "  Processing equipment...");
            processEquipment(dataObject.getAsJsonObject("equipment"), entityEquipmentData);
        }

        if (dataObject.has("potion"))
        {
            Log.write(0, "  Processing potion effects list...");
            processPotionEffects(dataObject.getAsJsonArray("potion"), entityAttributesData);
        }

        GeneralCheckSpawnStorage.getInstance().entityEquipmentList.add(entityEquipmentData);
        GeneralCheckSpawnStorage.getInstance().profilePriorityList.add(profilePriorityData);
        GeneralCheckSpawnStorage.getInstance().gameWorldList.add(gameWorldData);
        GeneralCheckSpawnStorage.getInstance().entityDescriptionsList.add(entityDescriptionData);
        GeneralCheckSpawnStorage.getInstance().entityAttributesList.add(entityAttributesData);

        Log.write(0, "Data object added to storage successfully");
        Log.write(0, "----------------------------------------");
        Log.write(0, "");
    }

    private void processEquipment(JsonObject equipmentObject, EntityEquipment.Data entityEquipmentData)
    {
        Log.write(0, "    Processing equipment details...");
        Log.write(0, "    --------------------------------");

        entityEquipmentData.heldItem = equipmentObject.has("held_item")
                ? Equipment.getInstance().parseItemList(equipmentObject.get("held_item"))
                : null;
        Log.write(0, "    Held item: " + (entityEquipmentData.heldItem != null ? "set" : "null"));

        entityEquipmentData.helmet = equipmentObject.has("armor_helmet")
                ? Equipment.getInstance().parseItemList(equipmentObject.get("armor_helmet"))
                : null;
        Log.write(0, "    Helmet: " + (entityEquipmentData.helmet != null ? "set" : "null"));

        entityEquipmentData.chestPlate = equipmentObject.has("armor_chest")
                ? Equipment.getInstance().parseItemList(equipmentObject.get("armor_chest"))
                : null;
        Log.write(0, "    Chestplate: " + (entityEquipmentData.chestPlate != null ? "set" : "null"));

        entityEquipmentData.legging = equipmentObject.has("armor_legs")
                ? Equipment.getInstance().parseItemList(equipmentObject.get("armor_legs"))
                : null;
        Log.write(0, "    Leggings: " + (entityEquipmentData.legging != null ? "set" : "null"));

        entityEquipmentData.boots = equipmentObject.has("armor_boots")
                ? Equipment.getInstance().parseItemList(equipmentObject.get("armor_boots"))
                : null;
        Log.write(0, "    Boots: " + (entityEquipmentData.boots != null ? "set" : "null"));

        if (equipmentObject.has("has_shield"))
        {
            JsonElement shieldElement = equipmentObject.get("has_shield");

            if (shieldElement.isJsonPrimitive())
            {
                JsonPrimitive primitive = shieldElement.getAsJsonPrimitive();

                if (primitive.isBoolean())
                {
                    entityEquipmentData.hasShield = primitive.getAsBoolean();
                    entityEquipmentData.shieldChance = primitive.getAsBoolean() ? 1.0 : 0.0;
                    Log.write(0, "    Shield (boolean): " + entityEquipmentData.hasShield);
                }
                else if (primitive.isNumber())
                {
                    double chance = primitive.getAsDouble();
                    entityEquipmentData.hasShield = chance > 0.0;
                    entityEquipmentData.shieldChance = Math.max(0.0, Math.min(1.0, chance));
                    Log.write(0, "    Shield chance: " + entityEquipmentData.shieldChance);
                }
            }
            else
            {
                entityEquipmentData.hasShield = false;
                entityEquipmentData.shieldChance = 0.0;
                Log.write(0, "    Shield: false");
            }
        }
        else
        {
            entityEquipmentData.hasShield = false;
            entityEquipmentData.shieldChance = 0.0;
            Log.write(0, "    Shield: not specified");
        }

        Log.write(0, "    Equipment processing completed");
        Log.write(0, "    --------------------------------");
        Log.write(0, "");
    }

    private void processPotionEffects(JsonArray potionArray, EntityAttributes.Data entityAttributesData)
    {
        Log.write(0, "    Processing " + potionArray.size() + " potion effects...");
        Log.write(0, "    --------------------------------");
        Log.write(0, "");

        entityAttributesData.potion = new ArrayList<>();

        for (int i = 0; i < potionArray.size(); i++)
        {
            String potionString = potionArray.get(i).getAsString();
            Log.write(0, "      Effect " + (i + 1) + ": " + potionString);

            String[] split = potionString.split(",");

            if (split.length < 3 || split.length > 4)
            {
                Log.write(0, "      ERROR: Bad potion specifier, expected <potion>,<duration>,<amplifier>[,<chance>]");
                continue;
            }

            ResourceLocation resourceLocation = new ResourceLocation(split[0].trim());
            Log.write(0, "        Potion ID: " + resourceLocation);

            Potion potion = ForgeRegistries.POTIONS.getValue(resourceLocation);

            if (potion == null)
            {
                Log.write(0, "        ERROR: Can't find potion in registry");
                continue;
            }

            Integer duration = Integer.parseInt(split[1].trim());
            Integer amplifier = Integer.parseInt(split[2].trim());
            Double chance = (split.length == 4) ? Double.parseDouble(split[3].trim()) : 1.0;

            Log.write(0, "        Duration: " + duration + ", Amplifier: " + amplifier + ", Chance: " + chance);

            entityAttributesData.potion.add(new PotionEffect.Data(
                    new net.minecraft.potion.PotionEffect(potion, duration, amplifier), chance));
        }

        Log.write(0, "    Total potion effects added: " + entityAttributesData.potion.size());
        Log.write(0, "    ================================");
        Log.write(0, "");
    }

    private void processPotionEffectsForDataSupport(JsonArray potionArray, AdditionalChecks.Data dataSupport)
    {
        Log.write(0, "Processing potion effects for data support...");
        Log.write(0, "  Number of effects: " + potionArray.size());

        dataSupport.potion = new ArrayList<>();

        for (int i = 0; i < potionArray.size(); i++)
        {
            String potionString = potionArray.get(i).getAsString();
            Log.write(0, "  Effect " + (i + 1) + ": " + potionString);

            String[] split = potionString.split(",");

            if (split.length < 3 || split.length > 4)
            {
                Log.write(0, "  ERROR: Bad potion specifier");
                continue;
            }

            ResourceLocation potionId = new ResourceLocation(split[0].trim());
            Log.write(0, "    Potion ID: " + potionId);

            Potion potion = ForgeRegistries.POTIONS.getValue(potionId);

            if (potion == null)
            {
                Log.write(0, "    ERROR: Can't find potion");
                continue;
            }

            Integer duration = Integer.parseInt(split[1].trim());
            Integer amplifier = Integer.parseInt(split[2].trim());
            Double chance = (split.length == 4) ? Double.parseDouble(split[3].trim()) : 1.0;

            Log.write(0, "    Duration: " + duration + ", Amplifier: " + amplifier + ", Chance: " + chance);

            dataSupport.potion.add(new PotionEffect.Data(
                    new net.minecraft.potion.PotionEffect(potion, duration, amplifier), chance));
        }
    }

    private void processDataSupport(JsonArray dataSupportArray)
    {
        Log.write(0, "Processing data support array...");
        Log.write(0, "  Number of entries: " + dataSupportArray.size());
        Log.write(0, "  ----------------------------------------");
        Log.write(0, "");

        for (int i = 0; i < dataSupportArray.size(); i++)
        {
            Log.write(0, "  Processing entry " + (i + 1) + "...");
            JsonObject dataSupportObject = dataSupportArray.get(i).getAsJsonObject();
            AdditionalChecks.Data dataSupport = new AdditionalChecks.Data();

            dataSupport.seeSky = dataSupportObject.has("see_sky") ? dataSupportObject.get("see_sky").getAsBoolean() : null;
            Log.write(0, "    See sky: " + dataSupport.seeSky);

            dataSupport.idDimension = dataSupportObject.has("idDimension") ? dataSupportObject.get("idDimension").getAsInt() : null;
            Log.write(0, "    Dimension ID: " + dataSupport.idDimension);

            String entityTypeString = dataSupportObject.get("entity_type").getAsString();
            Log.write(0, "    Entity type: " + entityTypeString);

            ResourceLocation entityType = new ResourceLocation(entityTypeString);
            EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(entityType);

            if (entityEntry == null)
            {
                Log.write(0, "    ERROR: Mob not found in registry");
                return;
            }

            dataSupport.entityType = entityType;
            Log.write(0, "    Entity validated: " + entityEntry.getName());

            if (dataSupportObject.has("potion"))
            {
                Log.write(0, "    Processing potion effects...");
                processPotionEffectsForDataSupport(dataSupportObject.getAsJsonArray("potion"), dataSupport);
            }

            SupportCheckSpawnStorage.getInstance().dataSupportList.add(dataSupport);
            Log.write(0, "    Data support entry added to storage");
        }

        Log.write(0, "Data support processing completed");
        Log.write(0, "========================================");
        Log.write(0, "");
    }

    private void handleLoadError(String message, Exception exception)
    {
        Log.write(0, "ERROR: " + message);
        Log.write(0, "Exception: " + exception.getClass().getName() + ": " + exception.getMessage());
        throw new RuntimeException(message, exception);
    }

    @Override
    public void eraseData()
    {
        Log.write(0, "================================================");
        Log.write(0, "Clearing all data storages...");

        int equipmentCount = GeneralCheckSpawnStorage.getInstance().entityEquipmentList.size();
        int priorityCount = GeneralCheckSpawnStorage.getInstance().profilePriorityList.size();
        int worldCount = GeneralCheckSpawnStorage.getInstance().gameWorldList.size();
        int descriptionCount = GeneralCheckSpawnStorage.getInstance().entityDescriptionsList.size();
        int attributeCount = GeneralCheckSpawnStorage.getInstance().entityAttributesList.size();
        int supportCount = SupportCheckSpawnStorage.getInstance().dataSupportList.size();

        Log.write(0, "  Entity equipment entries: " + equipmentCount);
        Log.write(0, "  Profile priority entries: " + priorityCount);
        Log.write(0, "  Game world entries: " + worldCount);
        Log.write(0, "  Entity description entries: " + descriptionCount);
        Log.write(0, "  Entity attribute entries: " + attributeCount);
        Log.write(0, "  Data support entries: " + supportCount);

        GeneralCheckSpawnStorage.getInstance().entityEquipmentList.clear();
        GeneralCheckSpawnStorage.getInstance().profilePriorityList.clear();
        GeneralCheckSpawnStorage.getInstance().gameWorldList.clear();
        GeneralCheckSpawnStorage.getInstance().entityDescriptionsList.clear();
        GeneralCheckSpawnStorage.getInstance().entityAttributesList.clear();
        SupportCheckSpawnStorage.getInstance().dataSupportList.clear();

        Log.write(0, "All data storages cleared successfully");
        Log.write(0, "================================================");
    }
}