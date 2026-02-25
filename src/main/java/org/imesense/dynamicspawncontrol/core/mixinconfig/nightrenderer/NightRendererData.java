package org.imesense.dynamicspawncontrol.core.mixinconfig.nightrenderer;

import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import com.google.gson.*;

import java.io.File;
import java.io.FileReader;

public final class NightRendererData
{
    private static final boolean DEFAULT_ENABLE_DARK_NIGHT = true;
    private static final boolean DEFAULT_DEPENDENCE_MOON = true;
    private static final double[] DEFAULT_MOON_FACTORS = {0.0, 0.075, 0.15, 0.225, 0.3};

    private static boolean enableDarkNight = DEFAULT_ENABLE_DARK_NIGHT;
    private static boolean dependenceLightMoonPhase = DEFAULT_DEPENDENCE_MOON;
    private static double[] moonPhaseFactors = DEFAULT_MOON_FACTORS.clone();

    @lombok.Getter
    private static boolean loadedFromFile = false;

    public static boolean isEnableDarkNight()
    {
        return enableDarkNight;
    }

    public static boolean isDependenceLightMoonPhase()
    {
        return dependenceLightMoonPhase;
    }

    public static double[] getMoonPhaseFactors()
    {
        return moonPhaseFactors.clone();
    }

    public static float getMoonPhaseFactor(float moonPhase)
    {
        if (moonPhaseFactors.length == 0)
            return 0.0f;

        int index = (int) Math.min(moonPhaseFactors.length - 1,
                Math.round(moonPhase / (1.0f / moonPhaseFactors.length)));

        return (float) moonPhaseFactors[index];
    }

    public static void loadFromFile(String filePath)
    {
        try
        {
            File configFile = new File(filePath);

            if (configFile.exists())
            {
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(new FileReader(configFile));

                if (jsonElement.isJsonArray())
                {
                    JsonArray array = jsonElement.getAsJsonArray();

                    for (JsonElement element : array)
                    {
                        if (element.isJsonObject())
                        {
                            JsonObject obj = element.getAsJsonObject();

                            if (obj.has("DSCNightRenderer"))
                            {
                                JsonObject configObj = obj.getAsJsonObject("DSCNightRenderer");

                                if (configObj.has("enableDarkNight"))
                                {
                                    enableDarkNight = configObj.get("enableDarkNight").getAsBoolean();
                                }

                                if (configObj.has("dependenceLightMoonPhase"))
                                {
                                    dependenceLightMoonPhase = configObj.get("dependenceLightMoonPhase").getAsBoolean();
                                }

                                if (configObj.has("moonPhaseFactors"))
                                {
                                    JsonArray moonArray = configObj.getAsJsonArray("moonPhaseFactors");
                                    moonPhaseFactors = new double[moonArray.size()];
                                    for (int i = 0; i < moonArray.size(); i++)
                                    {
                                        moonPhaseFactors[i] = moonArray.get(i).getAsDouble();
                                    }
                                }

                                loadedFromFile = true;

                                EarlyLogBuffer.log(Log.INFO,
                                        "Loaded night renderer config: enableDarkNight=" + enableDarkNight +
                                                ", dependenceLightMoonPhase=" + dependenceLightMoonPhase);
                                return;
                            }
                        }
                    }
                }

                EarlyLogBuffer.log(Log.WARN,
                        "Night renderer config has unknown format, using defaults");

                resetToDefault();
                loadedFromFile = false;
            }
        }
        catch (Exception exception)
        {
            EarlyLogBuffer.log(Log.ERROR,
                    "Failed to load night renderer config: " + exception.getMessage());

            resetToDefault();
            loadedFromFile = false;
        }
    }

    public static void resetToDefault()
    {
        enableDarkNight = DEFAULT_ENABLE_DARK_NIGHT;
        dependenceLightMoonPhase = DEFAULT_DEPENDENCE_MOON;
        moonPhaseFactors = DEFAULT_MOON_FACTORS.clone();
        loadedFromFile = false;

        EarlyLogBuffer.log(Log.INFO, "Reset night renderer config to defaults");
    }

    public static void setEnableDarkNight(boolean value)
    {
        enableDarkNight = value;
        EarlyLogBuffer.log(Log.INFO, "Night renderer enableDarkNight set to: " + value);
    }

    public static void setDependenceLightMoonPhase(boolean value)
    {
        dependenceLightMoonPhase = value;
        EarlyLogBuffer.log(Log.INFO, "Night renderer dependenceLightMoonPhase set to: " + value);
    }

    public static void setMoonPhaseFactors(double[] factors)
    {
        moonPhaseFactors = factors.clone();
        EarlyLogBuffer.log(Log.INFO, "Night renderer moonPhaseFactors updated");
    }
}