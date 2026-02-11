package org.imesense.dynamicspawncontrol.core.mixinconfig.srparasites;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.mixinconfig.basemixinconfig.BaseMixinConfig;

import java.io.*;
import java.io.File;
import com.google.gson.*;

public final class SRParasitesMixinCreateConfig extends BaseMixinConfig
{
    private static String activeConfigPath = null;

    private static final String CONFIG_FILE_NAME = "dsc_srparasites_coth_immune.json";

    @Override
    public void createFile(final String PATH)
    {
        try
        {
            activeConfigPath = PATH + File.separator + DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_MIXINS + File.separator + CONFIG_FILE_NAME;
            File configFile = new File(activeConfigPath);

            if (configFile.exists())
            {
                EarlyLogBuffer.log(Log.INFO, "Blacklist file already exists, loading from: " + activeConfigPath);

                SRParasitesBlacklistData.loadFromFile(activeConfigPath);

                return;
            }

            File parentDir = new File(PATH);

            if (!parentDir.exists())
            {
                parentDir.mkdirs();
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

            EarlyLogBuffer.log(Log.INFO, "Created blacklist file at: " + activeConfigPath);
            EarlyLogBuffer.log(Log.INFO, "Saved " + currentList.length + " entities to file");

            SRParasitesBlacklistData.loadFromFile(activeConfigPath);
        }
        catch (Exception exception)
        {
            EarlyLogBuffer.log(Log.ERROR,"Failed to create mixin config: " + exception.getMessage());

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
                EarlyLogBuffer.log(Log.INFO, "Manual reload of blacklist from file");

                SRParasitesBlacklistData.loadFromFile(activeConfigPath);
            }
            else
            {
                EarlyLogBuffer.log(Log.INFO, "Config file missing, resetting to defaults");

                SRParasitesBlacklistData.resetToDefault();
            }
        }
    }

    public static String getConfigPath()
    {
        return activeConfigPath;
    }
}