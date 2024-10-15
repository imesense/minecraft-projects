package org.imesense.dynamicspawncontrol.technical.config.spiderattackweb;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.config.CfgClassAbstract;
import org.imesense.dynamicspawncontrol.technical.config.player.DataPlayer;
import org.imesense.dynamicspawncontrol.technical.config.skeletondropitem.DataSkeletonDropItem;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotations.DCSSingleConfig;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 
 */
@DCSSingleConfig(fileName = "cfg_plugin_spider_attack_web.json")
public final class CfgSpiderAttackWeb extends CfgClassAbstract
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgSpiderAttackWeb(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.FALSE);

        CodeGenericUtils.printInitClassToLog(this.getClass());

        DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance =
                new DataSpiderAttackWeb.ConfigDataSpiderAttackWeb("spider_web_attack");

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
                DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getBlockWebReplacement());

        jsonObjectWeb.addProperty("web_melee_chance",
                DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getWebMeleeChance());

        jsonObjectWeb.addProperty("sling_coolDown",
                DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getSlingCoolDown());

        jsonObjectWeb.addProperty("sling_inaccuracy",
                DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getSlingInaccuracy());

        jsonObjectWeb.addProperty("sling_variance",
                DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getSlingVariance());

        jsonObjectWeb.addProperty("sling_webbing",
                DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getSlingWebbing());

        jsonObjectWeb.addProperty("sling_webbing_on_web",
                DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getSlingWebbingOnWeb());

        jsonObjectWeb.addProperty("ai_priority_sling_webs",
                DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getAIPrioritySlingWebs());

        jsonObjectWeb.addProperty("debug_info",
                DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getDebugInfo());

        recordObject.add(DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getCategoryObject(), jsonObjectWeb);

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

            if (readableObject.has(DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getCategoryObject()))
            {
                JsonObject jsonObjectWeb = readableObject.getAsJsonObject(
                        DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.getCategoryObject());

                if (jsonObjectWeb.has("block_web_replacement"))
                {
                    DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.setBlockWebReplacement(
                            jsonObjectWeb.get("block_web_replacement").getAsBoolean());
                }

                if (jsonObjectWeb.has("web_melee_chance"))
                {
                    DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.setWebMeleeChance(
                            jsonObjectWeb.get("web_melee_chance").getAsFloat());
                }

                if (jsonObjectWeb.has("sling_coolDown"))
                {
                    DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.setSlingCoolDown(
                            jsonObjectWeb.get("sling_coolDown").getAsDouble());
                }

                if (jsonObjectWeb.has("sling_inaccuracy"))
                {
                    DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.setSlingInaccuracy(
                            jsonObjectWeb.get("sling_inaccuracy").getAsFloat());
                }

                if (jsonObjectWeb.has("sling_variance"))
                {
                    DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.setSlingVariance(
                            jsonObjectWeb.get("sling_variance").getAsFloat());
                }

                if (jsonObjectWeb.has("sling_webbing"))
                {
                    DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.setSlingWebbing(
                            jsonObjectWeb.get("sling_webbing").getAsBoolean());
                }

                if (jsonObjectWeb.has("sling_webbing_on_web"))
                {
                    DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.setSlingWebbingOnWeb(
                            jsonObjectWeb.get("sling_webbing_on_web").getAsBoolean());
                }

                if (jsonObjectWeb.has("ai_priority_sling_webs"))
                {
                    DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.setAIPrioritySlingWebs(
                            jsonObjectWeb.get("ai_priority_sling_webs").getAsInt());
                }

                if (jsonObjectWeb.has("debug_info"))
                {
                    DataSpiderAttackWeb.ConfigDataSpiderAttackWeb.instance.setDebugInfo(
                            jsonObjectWeb.get("debug_info").getAsBoolean());
                }
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
