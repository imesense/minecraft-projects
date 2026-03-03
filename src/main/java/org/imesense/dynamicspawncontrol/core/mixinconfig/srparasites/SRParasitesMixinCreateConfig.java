package org.imesense.dynamicspawncontrol.core.mixinconfig.srparasites;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.Logger;
import org.imesense.dynamicspawncontrol.core.mixinconfig.basemixinconfig.BaseMixinConfig;
import org.imesense.dynamicspawncontrol.core.mixinconfig.annotations.MixinConfigFile;

import java.io.*;
import com.google.gson.*;

@MixinConfigFile(
        value = "dsc_srparasites_coth_immune.json",
        description = "This config is responsible for adapting mobs from mod 'DivineRGP' to 'SRParasites` without changing the basic configurations",
        createIfAbsent = true
)
public final class SRParasitesMixinCreateConfig extends BaseMixinConfig
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
                EarlyLogBuffer.log(Logger.INFO, "Blacklist file already exists, loading from: " + activeConfigPath);
                SRParasitesBlacklistData.loadFromFile(activeConfigPath);
                return;
            }

            File mixinsDir = new File(PATH + File.separator +
                    DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_MIXINS);

            if (!mixinsDir.exists())
            {
                mixinsDir.mkdirs();
            }

            JsonObject rootObject = new JsonObject();
            JsonArray blacklistArray = new JsonArray();

            String[] currentList = SRParasitesBlacklistData.getActiveBlacklist();

            for (String entity : currentList)
            {
                blacklistArray.add(entity);
            }

            rootObject.add("DSCBlackListCOTHMobImmune", blacklistArray);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gson.toJson(rootObject);

            try (FileWriter writer = new FileWriter(configFile))
            {
                writer.write(jsonString);
            }

            EarlyLogBuffer.log(Logger.INFO, "Created blacklist file at: " + activeConfigPath);
            EarlyLogBuffer.log(Logger.INFO, "Saved " + currentList.length + " entities to file");
            EarlyLogBuffer.log(Logger.INFO, "Config file name from annotation: " + fileName);

            SRParasitesBlacklistData.loadFromFile(activeConfigPath);
        }
        catch (Exception exception)
        {
            EarlyLogBuffer.log(Logger.ERROR, "Failed to create mixin config: " + exception.getMessage());
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
                EarlyLogBuffer.log(Logger.INFO, "Manual reload of blacklist from file");
                SRParasitesBlacklistData.loadFromFile(activeConfigPath);
            }
            else
            {
                EarlyLogBuffer.log(Logger.INFO, "Config file missing, resetting to defaults");
                SRParasitesBlacklistData.resetToDefault();
            }
        }
    }

    public static String getConfigPath()
    {
        return activeConfigPath;
    }
}