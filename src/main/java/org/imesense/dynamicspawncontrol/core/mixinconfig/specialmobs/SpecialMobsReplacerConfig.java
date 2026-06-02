package org.imesense.dynamicspawncontrol.core.mixinconfig.specialmobs;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.core.mixinconfig.basemixinconfig.BaseMixinConfig;
import org.imesense.dynamicspawncontrol.core.mixinconfig.annotations.MixinConfigFile;

import java.io.*;
import com.google.gson.*;

@MixinConfigFile(
        value = "dsc_specialmobs_replacer.json",
        description = "Config for SpecialMobs vanilla mob replacement",
        createIfAbsent = true
)
public final class SpecialMobsReplacerConfig extends BaseMixinConfig
{
    private static String activeConfigPath = null;

    private String getConfigFileName()
    {
        MixinConfigFile annotation = this.getClass().getAnnotation(MixinConfigFile.class);
        return annotation != null ? annotation.value() : "unknown_config.json";
    }

    @Override
    public void createFile(final String PATH)
    {
        try
        {
            String fileName = getConfigFileName();

            activeConfigPath = PATH + File.separator +
                    DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_MIXINS +
                    File.separator + fileName;

            File configFile = new File(activeConfigPath);

            if (configFile.exists())
            {
                EarlyLogBuffer.log(LogManager.INFO,
                        "SpecialMobs replacer config already exists, loading from: " + activeConfigPath);
                SpecialMobsReplacerData.loadFromFile(activeConfigPath);
                return;
            }

            File mixinsDir = new File(PATH + File.separator +
                    DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_MIXINS);

            if (!mixinsDir.exists())
            {
                mixinsDir.mkdirs();
            }

            JsonArray rootArray = new JsonArray();
            JsonObject wrapperObject = new JsonObject();
            JsonObject configObject = new JsonObject();

            configObject.addProperty("all_replace_vanilla", false);
            wrapperObject.add("DSCSpecialMobsReplaceVanillaMobs", configObject);
            rootArray.add(wrapperObject);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gson.toJson(rootArray);

            try (FileWriter writer = new FileWriter(configFile))
            {
                writer.write(jsonString);
            }

            EarlyLogBuffer.log(LogManager.INFO, "Created SpecialMobs replacer config at: " + activeConfigPath);
            EarlyLogBuffer.log(LogManager.INFO, "Default value: all_replace_vanilla = false");

            SpecialMobsReplacerData.loadFromFile(activeConfigPath);
        }
        catch (Exception exception)
        {
            EarlyLogBuffer.log(LogManager.ERROR,
                    "Failed to create SpecialMobs replacer config: " + exception.getMessage());

            exception.printStackTrace();
        }
    }

    @Override
    public void OverwritingArray()
    {
        if (activeConfigPath != null)
        {
            File configFile = new File(activeConfigPath);

            if (configFile.exists())
            {
                EarlyLogBuffer.log(LogManager.INFO, "Manual reload of SpecialMobs replacer config");
                SpecialMobsReplacerData.loadFromFile(activeConfigPath);
            }
            else
            {
                EarlyLogBuffer.log(LogManager.INFO, "SpecialMobs replacer config missing, resetting to defaults");
                SpecialMobsReplacerData.resetToDefault();
            }
        }
    }

    public static String getConfigPath()
    {
        return activeConfigPath;
    }
}
