package org.imesense.dynamicspawncontrol.core.mixinconfig.nightrenderer;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
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
                EarlyLogBuffer.log(Log.INFO,
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

            // Значения по умолчанию
            configObject.addProperty("enableDarkNight", true);
            configObject.addProperty("dependenceLightMoonPhase", true);

            JsonArray moonArray = new JsonArray();
            double[] defaultFactors = {0.0, 0.075, 0.15, 0.225, 0.3};
            for (double factor : defaultFactors)
            {
                moonArray.add(factor);
            }
            configObject.add("moonPhaseFactors", moonArray);

            wrapperObject.add("DSCNightRenderer", configObject);
            rootArray.add(wrapperObject);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gson.toJson(rootArray);

            try (FileWriter writer = new FileWriter(configFile))
            {
                writer.write(jsonString);
            }

            EarlyLogBuffer.log(Log.INFO, "Created night renderer config at: " + activeConfigPath);
            EarlyLogBuffer.log(Log.INFO, "Default values: enableDarkNight=true, dependenceLightMoonPhase=true");

            NightRendererData.loadFromFile(activeConfigPath);
        }
        catch (Exception exception)
        {
            EarlyLogBuffer.log(Log.ERROR,
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
                EarlyLogBuffer.log(Log.INFO, "Manual reload of night renderer config");
                NightRendererData.loadFromFile(activeConfigPath);
            }
            else
            {
                EarlyLogBuffer.log(Log.INFO, "Night renderer config missing, resetting to defaults");
                NightRendererData.resetToDefault();
            }
        }
    }

    public static String getConfigPath()
    {
        return activeConfigPath;
    }
}