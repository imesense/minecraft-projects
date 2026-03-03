package org.imesense.dynamicspawncontrol.core.mixinconfig.nightrenderer;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.core.mixinconfig.basemixinconfig.BaseMixinConfig;
import org.imesense.dynamicspawncontrol.core.mixinconfig.annotations.MixinConfigFile;

import java.io.*;
import com.google.gson.*;

@MixinConfigFile(
        value = "dsc_night_renderer.json",
        description = "Config for night darkness rendering",
        createIfAbsent = true
)
public final class NightRendererConfig extends BaseMixinConfig
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
                        "Night renderer config already exists, loading from: " + activeConfigPath);
                NightRendererData.loadFromFile(activeConfigPath);
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

            configObject.addProperty("enableDarkNight", NightRendererData.getDefaultEnableDarkNight());
            configObject.addProperty("dependenceLightMoonPhase", NightRendererData.getDefaultDependenceMoon());

            JsonArray moonArray = new JsonArray();
            float[] defaultFactors = NightRendererData.getDefaultMoonPhaseFactors();

            for (float factor : defaultFactors)
            {
                moonArray.add(factor);
            }
            configObject.add("moonPhaseFactorsArray", moonArray);

            JsonArray blacklistArray = new JsonArray();
            int[] defaultBlacklist = NightRendererData.getDefaultBlacklistDimensions();

            for (int dimensionId : defaultBlacklist)
            {
                blacklistArray.add(dimensionId);
            }
            configObject.add("blacklistedDimensions", blacklistArray);

            wrapperObject.add("DSCNightRenderer", configObject);
            rootArray.add(wrapperObject);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gson.toJson(rootArray);

            try (FileWriter writer = new FileWriter(configFile))
            {
                writer.write(jsonString);
            }

            EarlyLogBuffer.log(LogManager.INFO, "Created night renderer config at: " + activeConfigPath);
            EarlyLogBuffer.log(LogManager.INFO, "Default blacklisted dimensions: " +
                    java.util.Arrays.toString(defaultBlacklist));

            NightRendererData.loadFromFile(activeConfigPath);
        }
        catch (Exception exception)
        {
            EarlyLogBuffer.log(LogManager.ERROR,
                    "Failed to create night renderer config: " + exception.getMessage());
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
                EarlyLogBuffer.log(LogManager.INFO, "Manual reload of night renderer config");
                NightRendererData.loadFromFile(activeConfigPath);
            }
            else
            {
                EarlyLogBuffer.log(LogManager.INFO, "Night renderer config missing, resetting to defaults");
                NightRendererData.resetToDefault();
            }
        }
    }

    public static String getConfigPath()
    {
        return activeConfigPath;
    }
}