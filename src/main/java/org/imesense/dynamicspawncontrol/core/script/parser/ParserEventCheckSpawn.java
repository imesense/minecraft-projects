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
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
        if (element.isJsonPrimitive())
        {
            JsonElement resolved = resolveTemplate(element, templates);

            return new TemplateWithChance(resolved, 1.0);
        }

        if (element.isJsonArray())
        {
            JsonArray arr = element.getAsJsonArray();

            if (arr.size() != 2)
                throw new RuntimeException("Template array must be [template, chance]");

            JsonElement template = resolveTemplate(arr.get(0), templates);
            double chance = arr.get(1).getAsDouble();

            return new TemplateWithChance(template, Math.max(0.0, Math.min(1.0, chance)));
        }

        throw new RuntimeException("Invalid template format: " + element);
    }

    private JsonObject processOrderedIncludes(JsonObject jsonObject, File currentFile) throws IOException
    {
        JsonObject result = new JsonObject();

        for (String section : SECTION_ORDER)
        {
            if (jsonObject.has(section))
            {
                JsonElement sectionElement = jsonObject.get(section);
                JsonElement processedSection = processSection(section, sectionElement, currentFile);

                if (processedSection != null)
                {
                    result.add(section, processedSection);
                }
            }
        }

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet())
        {
            String key = entry.getKey();
            if (!Arrays.asList(SECTION_ORDER).contains(key) && !result.has(key))
            {
                result.add(key, entry.getValue());
            }
        }

        return result;
    }

    private JsonElement processSection(String sectionName, JsonElement sectionElement, File currentFile) throws IOException
    {
        if (sectionElement.isJsonObject())
        {
            JsonObject sectionObj = sectionElement.getAsJsonObject();

            if (sectionObj.has("#include"))
            {
                return processIncludeDirective(sectionObj.get("#include"), currentFile, sectionName);
            }
            else
            {
                return processOrderedIncludes(sectionObj, currentFile);
            }
        }
        else if (sectionElement.isJsonArray())
        {
            JsonArray sectionArray = sectionElement.getAsJsonArray();
            JsonArray resultArray = new JsonArray();

            for (JsonElement element : sectionArray)
            {
                if (element.isJsonObject() && element.getAsJsonObject().has("#include"))
                {
                    JsonObject included = loadIncludedFile(element.getAsJsonObject().get("#include").getAsString(), currentFile);
                    resultArray.add(included);
                }
                else
                {
                    resultArray.add(element);
                }
            }

            return resultArray;
        }

        return sectionElement;
    }

    private JsonElement processIncludeDirective(JsonElement includeElement, File currentFile, String sectionName) throws IOException
    {
        if (includeElement.isJsonPrimitive())
        {
            String includeFile = includeElement.getAsString();

            return loadIncludedFileForSection(includeFile, currentFile, sectionName);
        }
        else if (includeElement.isJsonArray())
        {
            JsonArray includeArray = includeElement.getAsJsonArray();

            if (sectionName.equals("configs") || sectionName.equals("data_support"))
            {
                JsonArray mergedArray = new JsonArray();

                for (JsonElement element : includeArray)
                {
                    String includeFile = element.getAsString();
                    JsonObject included = loadIncludedFile(includeFile, currentFile);

                    if (included.has(sectionName))
                    {
                        JsonElement sectionContent = included.get(sectionName);
                        if (sectionContent.isJsonArray())
                        {
                            int count = sectionContent.getAsJsonArray().size();

                            for (JsonElement item : sectionContent.getAsJsonArray())
                            {
                                mergedArray.add(item);
                            }
                        }
                    }
                    else
                    {
                        mergedArray.add(included);
                    }
                }

                return mergedArray;
            }
            else
            {
                JsonObject mergedObject = new JsonObject();

                for (JsonElement element : includeArray)
                {
                    String includeFile = element.getAsString();
                    JsonObject included = loadIncludedFile(includeFile, currentFile);

                    if (included.has(sectionName))
                    {
                        mergeJsonObjects(mergedObject, included.get(sectionName).getAsJsonObject());
                    }
                    else
                    {
                        mergeJsonObjects(mergedObject, included);
                    }
                }

                return mergedObject;
            }
        }

        return JsonNull.INSTANCE;
    }

    private JsonElement loadIncludedFileForSection(String includePath, File currentFile, String sectionName) throws IOException
    {
        JsonObject included = loadIncludedFile(includePath, currentFile);

        if (included.has(sectionName))
        {
            return included.get(sectionName);
        }

        return included;
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

            return processOrderedIncludes(includedJson, includeFilePath.toFile());
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

    private Set<String> getJsonKeys(JsonObject jsonObject)
    {
        Set<String> keys = new HashSet<>();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet())
        {
            keys.add(entry.getKey());
        }

        return keys;
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

    private JsonObject loadTemplates(JsonObject jsonObject, File currentFile) throws IOException
    {
        JsonObject templates = new JsonObject();

        if (jsonObject.has("templates"))
        {
            JsonElement templatesElement = jsonObject.get("templates");

            if (templatesElement.isJsonObject())
            {
                templates = templatesElement.getAsJsonObject();

                if (templates.has("#include"))
                {
                    templates = processIncludeDirective(templates.get("#include"), currentFile, "templates").getAsJsonObject();
                }
            }
        }

        return templates;
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

    private List<TemplateWithChance> resolveMultipleTemplatesWithChance(JsonElement element, JsonObject templates)
    {
        List<TemplateWithChance> result = new ArrayList<>();

        if (element.isJsonPrimitive())
        {
            String templateString = element.getAsString();
            String[] templateNames = templateString.split("\\s*,\\s*");

            for (String templateName : templateNames)
            {
                templateName = templateName.trim();

                if (!templateName.isEmpty())
                {
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

            if (arr.size() == 2 && arr.get(0).isJsonPrimitive())
            {
                String templateString = arr.get(0).getAsString();
                double commonChance = arr.get(1).getAsDouble();

                String[] templateNames = templateString.split("\\s*,\\s*");

                for (String templateName : templateNames)
                {
                    templateName = templateName.trim();
                    if (!templateName.isEmpty())
                    {
                        result.add(new TemplateWithChance(resolveTemplate(
                                new JsonPrimitive(templateName), templates), commonChance));
                    }
                }
            }
            else if (arr.size() > 0 && arr.get(0).isJsonArray())
            {
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

                            result.add(new TemplateWithChance(
                                    resolveTemplate(templateElement, templates), chance));
                        }
                    }
                }
            }
            else
            {
                if (arr.size() == 2)
                {
                    JsonElement templateElement = arr.get(0);
                    double chance = arr.get(1).getAsDouble();

                    result.add(new TemplateWithChance(
                            resolveTemplate(templateElement, templates),
                            chance
                    ));
                }
            }
        }

        return result;
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
            long startTime = System.currentTimeMillis();

            Gson gson = new Gson();
            JsonElement jsonElement = gson.fromJson(fileReader, JsonElement.class);

            if (jsonElement.isJsonArray())
            {
                JsonArray jsonArray = jsonElement.getAsJsonArray();

                if (jsonArray.size() != 1)
                {
                    throw new RuntimeException("Expected exactly one JSON object in array, found " + jsonArray.size() + " elements");
                }

                JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
                processJsonFile(jsonObject, file);
            }
            else if (jsonElement.isJsonObject())
            {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                processJsonFile(jsonObject, file);
            }
            else
            {
                throw new RuntimeException("Invalid JSON format: expected object or array, got " +
                        jsonElement.getClass().getSimpleName());
            }

            long endTime = System.currentTimeMillis();
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
        JsonObject templates = loadTemplates(jsonObject, file);

        JsonObject processedJson = processOrderedIncludes(jsonObject, file);

        processedJson.add("templates", templates);

        removeIncludeDirectives(processedJson);

        processJsonObject(processedJson);
    }

    private void processJsonObject(JsonObject jsonObject) throws RuntimeException
    {
        JsonObject templates = jsonObject.has("templates") ?
                jsonObject.getAsJsonObject("templates") : new JsonObject();

        if (jsonObject.has("configs"))
        {
            processConfigs(jsonObject.getAsJsonArray("configs"), templates);
        }

        if (jsonObject.has("data_support"))
        {
            processDataSupport(jsonObject.getAsJsonArray("data_support"));
        }
    }

    private void processConfigs(JsonArray configs, JsonObject templates) throws RuntimeException
    {
        for (int i = 0; i < configs.size(); i++)
        {
            JsonObject dataObject = configs.get(i).getAsJsonObject().getAsJsonObject("data");

            if (dataObject == null)
            {
                throw new RuntimeException("Key 'data' not found in JSON file.");
            }

            validateRequiredFields(dataObject);
            processDataObject(dataObject, templates);
        }
    }

    private void validateRequiredFields(JsonObject dataObject) throws RuntimeException
    {
        if (!dataObject.has("profile") || !dataObject.has("description"))
        {
            throw new RuntimeException("Fields 'profile' and 'description' are required in the 'data' section.");
        }

        if (dataObject.get("profile").getAsString().isEmpty() || dataObject.get("description").getAsString().isEmpty())
        {
            throw new RuntimeException("Fields 'profile' and 'description' must not be empty in the 'data' section.");
        }
    }

    private void processDataObject(JsonObject dataObject, JsonObject templates)
    {
        String profileName = dataObject.get("profile").getAsString();

        EntityEquipment.Data entityEquipmentData = new EntityEquipment.Data();
        EntityDescription.Data entityDescriptionData = new EntityDescription.Data();
        ProfilePriority.Data profilePriorityData = new ProfilePriority.Data();
        GameWorld.Data gameWorldData = new GameWorld.Data();
        EntityAttributes.Data entityAttributesData = new EntityAttributes.Data();

        if (dataObject.has("potion"))
        {
            List<TemplateWithChance> potionTemplates =
                    resolveMultipleTemplatesWithChance(dataObject.get("potion"), templates);

            JsonArray mergedPotions = new JsonArray();
            double maxChance = 0.0;

            for (TemplateWithChance template : potionTemplates)
            {
                if (template.resolved.isJsonArray())
                {
                    JsonArray potionArray = template.resolved.getAsJsonArray();

                    for (JsonElement potion : potionArray)
                    {
                        mergedPotions.add(potion);
                    }
                }
                else
                {
                    mergedPotions.add(template.resolved);
                }

                maxChance = Math.max(maxChance, template.chance);
            }

            dataObject.add("potion", mergedPotions);
            entityAttributesData.potionChance = maxChance;
        }

        if (dataObject.has("command_nbt"))
        {
            TemplateWithChance nbt =
                    resolveTemplateWithChance(dataObject.get("command_nbt"), templates);

            dataObject.add("command_nbt", nbt.resolved);
            entityAttributesData.commandNbtChance = nbt.chance;
        }

        entityDescriptionData.profile = profileName;
        entityDescriptionData.description = dataObject.get("description").getAsString();

        String entityTypeString = dataObject.get("entity_type").getAsString();

        ResourceLocation entityType = new ResourceLocation(entityTypeString);
        EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(entityType);

        if (entityEntry == null)
        {
            return;
        }

        entityDescriptionData.entityType = entityType;

        profilePriorityData.priority = dataObject.has("priority") ? dataObject.get("priority").getAsInt() : 0;
        entityDescriptionData.isArcher = dataObject.has("is_archer") && dataObject.get("is_archer").getAsBoolean();
        gameWorldData.seeSky = dataObject.has("see_sky") ? dataObject.get("see_sky").getAsBoolean() : null;
        gameWorldData.idDimension = dataObject.has("id_dimension") ? dataObject.get("id_dimension").getAsInt() : null;
        entityAttributesData.commandNbt = dataObject.has("command_nbt") ? dataObject.get("command_nbt").toString() : null;
        gameWorldData.maxHeight = dataObject.has("max_height") ? dataObject.get("max_height").getAsInt() : null;
        gameWorldData.minHeight = dataObject.has("min_height") ? dataObject.get("min_height").getAsInt() : null;
        entityDescriptionData.name = dataObject.has("name") ? dataObject.get("name").getAsString() : null;

        if (dataObject.has("equipment"))
        {
            processEquipment(dataObject.getAsJsonObject("equipment"), entityEquipmentData);
        }

        if (dataObject.has("potion"))
        {
            processPotionEffects(dataObject.getAsJsonArray("potion"), entityAttributesData);
        }

        GeneralCheckSpawnStorage.getInstance().entityEquipmentList.add(entityEquipmentData);
        GeneralCheckSpawnStorage.getInstance().profilePriorityList.add(profilePriorityData);
        GeneralCheckSpawnStorage.getInstance().gameWorldList.add(gameWorldData);
        GeneralCheckSpawnStorage.getInstance().entityDescriptionsList.add(entityDescriptionData);
        GeneralCheckSpawnStorage.getInstance().entityAttributesList.add(entityAttributesData);
    }

    private void processEquipment(JsonObject equipmentObject, EntityEquipment.Data entityEquipmentData)
    {
        entityEquipmentData.heldItem = equipmentObject.has("held_item")
                ? Equipment.getInstance().parseItemList(equipmentObject.get("held_item"))
                : null;

        entityEquipmentData.helmet = equipmentObject.has("armor_helmet")
                ? Equipment.getInstance().parseItemList(equipmentObject.get("armor_helmet"))
                : null;

        entityEquipmentData.chestPlate = equipmentObject.has("armor_chest")
                ? Equipment.getInstance().parseItemList(equipmentObject.get("armor_chest"))
                : null;

        entityEquipmentData.legging = equipmentObject.has("armor_legs")
                ? Equipment.getInstance().parseItemList(equipmentObject.get("armor_legs"))
                : null;

        entityEquipmentData.boots = equipmentObject.has("armor_boots")
                ? Equipment.getInstance().parseItemList(equipmentObject.get("armor_boots"))
                : null;

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
                }
                else if (primitive.isNumber())
                {
                    double chance = primitive.getAsDouble();

                    entityEquipmentData.hasShield = chance > 0.0;
                    entityEquipmentData.shieldChance = Math.max(0.0, Math.min(1.0, chance));
                }
            }
            else
            {
                entityEquipmentData.hasShield = false;
                entityEquipmentData.shieldChance = 0.0;
            }
        }
        else
        {
            entityEquipmentData.hasShield = false;
            entityEquipmentData.shieldChance = 0.0;
        }
    }

    private void processPotionEffects(JsonArray potionArray, EntityAttributes.Data entityAttributesData)
    {
        entityAttributesData.potion = new ArrayList<>();

        for (int i = 0; i < potionArray.size(); i++)
        {
            String potionString = potionArray.get(i).getAsString();

            String[] split = potionString.split(",");

            if (split.length < 3 || split.length > 4)
            {
                continue;
            }

            ResourceLocation resourceLocation = new ResourceLocation(split[0].trim());

            Potion potion = ForgeRegistries.POTIONS.getValue(resourceLocation);

            if (potion == null)
            {
                continue;
            }

            Integer duration = Integer.parseInt(split[1].trim());
            Integer amplifier = Integer.parseInt(split[2].trim());
            Double chance = (split.length == 4) ? Double.parseDouble(split[3].trim()) : 1.0;

            entityAttributesData.potion.add(new PotionEffect.Data(
                    new net.minecraft.potion.PotionEffect(potion, duration, amplifier), chance));
        }
    }

    private void processPotionEffectsForDataSupport(JsonArray potionArray, AdditionalChecks.Data dataSupport)
    {
        dataSupport.potion = new ArrayList<>();

        for (int i = 0; i < potionArray.size(); i++)
        {
            String potionString = potionArray.get(i).getAsString();

            String[] split = potionString.split(",");

            if (split.length < 3 || split.length > 4)
            {
                continue;
            }

            ResourceLocation potionId = new ResourceLocation(split[0].trim());

            Potion potion = ForgeRegistries.POTIONS.getValue(potionId);

            if (potion == null)
            {
                continue;
            }

            Integer duration = Integer.parseInt(split[1].trim());
            Integer amplifier = Integer.parseInt(split[2].trim());
            Double chance = (split.length == 4) ? Double.parseDouble(split[3].trim()) : 1.0;

            dataSupport.potion.add(new PotionEffect.Data(
                    new net.minecraft.potion.PotionEffect(potion, duration, amplifier), chance));
        }
    }

    private void processDataSupport(JsonArray dataSupportArray)
    {
        for (int i = 0; i < dataSupportArray.size(); i++)
        {
            JsonObject dataSupportObject = dataSupportArray.get(i).getAsJsonObject();
            AdditionalChecks.Data dataSupport = new AdditionalChecks.Data();

            dataSupport.seeSky = dataSupportObject.has("see_sky") ? dataSupportObject.get("see_sky").getAsBoolean() : null;
            dataSupport.idDimension = dataSupportObject.has("idDimension") ? dataSupportObject.get("idDimension").getAsInt() : null;

            String entityTypeString = dataSupportObject.get("entity_type").getAsString();

            ResourceLocation entityType = new ResourceLocation(entityTypeString);
            EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(entityType);

            if (entityEntry == null)
            {
                return;
            }

            dataSupport.entityType = entityType;

            if (dataSupportObject.has("potion"))
            {
                processPotionEffectsForDataSupport(dataSupportObject.getAsJsonArray("potion"), dataSupport);
            }

            SupportCheckSpawnStorage.getInstance().dataSupportList.add(dataSupport);
        }
    }

    private void handleLoadError(String message, Exception exception)
    {
        throw new RuntimeException(message, exception);
    }

    @Override
    public void createNewConfigFile(File file)
    {
        try
        {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists())
            {
                parentDir.mkdirs();
            }

            JsonObject rootObject = new JsonObject();

            JsonObject templates = new JsonObject();
            templates.add("#include", new JsonArray());
            rootObject.add("templates", templates);

            JsonObject configs = new JsonObject();
            configs.add("#include", new JsonArray());
            rootObject.add("configs", configs);

            JsonObject dataSupport = new JsonObject();
            dataSupport.add("#include", new JsonArray());
            rootObject.add("data_support", dataSupport);

            JsonArray jsonArray = new JsonArray();
            jsonArray.add(rootObject);

            try (FileWriter writer = new FileWriter(file))
            {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(jsonArray, writer);
            }
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    @Override
    public void eraseData()
    {
        GeneralCheckSpawnStorage.getInstance().entityEquipmentList.clear();
        GeneralCheckSpawnStorage.getInstance().profilePriorityList.clear();
        GeneralCheckSpawnStorage.getInstance().gameWorldList.clear();
        GeneralCheckSpawnStorage.getInstance().entityDescriptionsList.clear();
        GeneralCheckSpawnStorage.getInstance().entityAttributesList.clear();
        SupportCheckSpawnStorage.getInstance().dataSupportList.clear();
    }
}