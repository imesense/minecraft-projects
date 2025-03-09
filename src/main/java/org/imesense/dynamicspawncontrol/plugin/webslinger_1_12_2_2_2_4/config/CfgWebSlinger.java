package org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.config;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
@ConceptConfig(fileName = "cfg_webslinger_1_12_2_2_2_4")
public final class CfgWebSlinger extends BaseConfig
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgWebSlinger(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.FALSE);

        CodeGeneric.printInitClassToLog(this.getClass());

        DataWebSlinger.ConfigDataSpiderAttackWeb.Instance =
                new DataWebSlinger.ConfigDataSpiderAttackWeb("webslinger_1_12_2_2_2_4");

        //entityIdPriorityMap = new HashMap<>();
        //entityIdPriorityMap.put("minecraft:spider", 3);

        if (Files.exists(Paths.get(this.nameConfig)))
        {
            this.loadFromFile();
        }
        else
        {
            this.saveToFile();
        }
    }

    /**
     *
     */
    @Override
    public void saveToFile()
    {
        Path configPath = Paths.get(this.nameConfig).getParent();

        if (Files.notExists(configPath))
        {
            try
            {
                Files.createDirectories(configPath);
            }
            catch (IOException exception)
            {
                throw new RuntimeException(exception);
            }
        }

        JsonObject recordObject = new JsonObject();
        JsonObject jsonObjectWeb = new JsonObject();

        jsonObjectWeb.addProperty("block_web_replacement",
                DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getBlockWebReplacement());

        jsonObjectWeb.addProperty("web_melee_chance",
                DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getWebMeleeChance());

        jsonObjectWeb.addProperty("sling_coolDown",
                DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getSlingCoolDown());

        jsonObjectWeb.addProperty("sling_inaccuracy",
                DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getSlingInaccuracy());

        jsonObjectWeb.addProperty("sling_variance",
                DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getSlingVariance());

        jsonObjectWeb.addProperty("sling_webbing",
                DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getSlingWebbing());

        jsonObjectWeb.addProperty("sling_webbing_on_web",
                DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getSlingWebbingOnWeb());

        jsonObjectWeb.addProperty("ai_priority_sling_webs",
                DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getAIPrioritySlingWebs());

        //JsonArray EntityIdsNameArray = new JsonArray();

        //for (String name : DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getEntityIds())
        //{
        //    EntityIdsNameArray.add(name);
        //}

        //jsonObjectWeb.add("entity_to_attack_web", EntityIdsNameArray);

        JsonArray entityIdPriorityArray = new JsonArray();
        for (Map.Entry<String, Integer> entry : DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getEntityIdPriorityMap().entrySet()) {
            JsonObject entityObject = new JsonObject();
            entityObject.addProperty("entityId", entry.getKey());
            entityObject.addProperty("priority", entry.getValue());
            entityIdPriorityArray.add(entityObject);
        }

        jsonObjectWeb.add("entity_to_attack_web", entityIdPriorityArray);

        recordObject.add(DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getCategoryObject(), jsonObjectWeb);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter file = new FileWriter(this.nameConfig))
        {
            gson.toJson(recordObject, file);
        }
        catch (IOException exception)
        {
            throw new RuntimeException("Error writing to file: " + exception.getMessage(), exception);
        }
    }

    /**
     *
     */
    @Override
    public void loadFromFile()
    {
        try (FileReader fileReader = new FileReader(this.nameConfig))
        {
            JsonElement fileReaderJsonElement = new JsonParser().parse(fileReader);
            JsonObject readableObject = fileReaderJsonElement.getAsJsonObject();

            if (readableObject.has(DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getCategoryObject()))
            {
                JsonObject jsonObjectWeb = readableObject.getAsJsonObject(
                        DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.getCategoryObject());

                if (jsonObjectWeb.has("block_web_replacement"))
                {
                    DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.setBlockWebReplacement(
                            jsonObjectWeb.get("block_web_replacement").getAsBoolean());
                }

                if (jsonObjectWeb.has("web_melee_chance"))
                {
                    DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.setWebMeleeChance(
                            jsonObjectWeb.get("web_melee_chance").getAsFloat());
                }

                if (jsonObjectWeb.has("sling_coolDown"))
                {
                    DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.setSlingCoolDown(
                            jsonObjectWeb.get("sling_coolDown").getAsDouble());
                }

                if (jsonObjectWeb.has("sling_inaccuracy"))
                {
                    DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.setSlingInaccuracy(
                            jsonObjectWeb.get("sling_inaccuracy").getAsFloat());
                }

                if (jsonObjectWeb.has("sling_variance"))
                {
                    DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.setSlingVariance(
                            jsonObjectWeb.get("sling_variance").getAsFloat());
                }

                if (jsonObjectWeb.has("sling_webbing"))
                {
                    DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.setSlingWebbing(
                            jsonObjectWeb.get("sling_webbing").getAsBoolean());
                }

                if (jsonObjectWeb.has("sling_webbing_on_web"))
                {
                    DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.setSlingWebbingOnWeb(
                            jsonObjectWeb.get("sling_webbing_on_web").getAsBoolean());
                }

                if (jsonObjectWeb.has("ai_priority_sling_webs"))
                {
                    DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.setAIPrioritySlingWebs(
                            jsonObjectWeb.get("ai_priority_sling_webs").getAsInt());
                }

                if (jsonObjectWeb.has("entity_to_attack_web")) {
                    JsonArray entityIdPriorityArray = jsonObjectWeb.getAsJsonArray("entity_to_attack_web");
                    Map<String, Integer> entityIdPriorityMap = new HashMap<>();

                    for (JsonElement element : entityIdPriorityArray) {
                        JsonObject entityObject = element.getAsJsonObject();
                        String entityId = entityObject.get("entityId").getAsString();
                        int priority = entityObject.get("priority").getAsInt();
                        entityIdPriorityMap.put(entityId, priority);
                    }

                    DataWebSlinger.ConfigDataSpiderAttackWeb.Instance.setEntityIdPriorityMap(entityIdPriorityMap);
                }

                //if (jsonObjectWeb.has("entity_to_attack_web"))
                //{
                //    JsonArray entityToAttackWebArray =
                //            jsonObjectWeb.getAsJsonArray("entity_to_attack_web");
                //
                //    String[] entity = new String[entityToAttackWebArray.size()];
                //
                //    for (int i = 0; i < entityToAttackWebArray.size(); i++)
                //    {
                    //        entity[i] = entityToAttackWebArray.get(i).getAsString();
                    //    }
                //
                //    DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.
                //            setEntityIds(entity);
                //}
            }
            else
            {
                Log.writeDataToLogFile(2, "'web' section is missing in the config file.");
            }
        }
        catch (FileNotFoundException exception)
        {
            Log.writeDataToLogFile(2, "File not found: " + exception.getMessage());
        }
        catch (IOException exception)
        {
            Log.writeDataToLogFile(2, "IO Exception while loading: " + exception.getMessage());
        }
    }
}
