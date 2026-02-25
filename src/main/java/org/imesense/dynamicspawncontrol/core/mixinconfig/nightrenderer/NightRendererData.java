package org.imesense.dynamicspawncontrol.core.mixinconfig.nightrenderer;

import lombok.Getter;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

public final class NightRendererData
{
    private static final boolean DEFAULT_ENABLE_DARK_NIGHT = true;
    private static final boolean DEFAULT_DEPENDENCE_MOON = true;
    private static final float[] DEFAULT_MOON_PHASE_FACTORS = new float[]
    {
        0.1F, 0.075F, 0.050F, 0.250F, 0.0F, 0.0250F, 0.050F, 0.0750F
    };

    private static final int EXPECTED_MOON_PHASES_COUNT = 8;

    @Getter
    private static boolean enableDarkNight = DEFAULT_ENABLE_DARK_NIGHT;

    @Getter
    private static boolean dependenceLightMoonPhase = DEFAULT_DEPENDENCE_MOON;
    private static float[] moonPhaseFactorsArray = DEFAULT_MOON_PHASE_FACTORS.clone();

    @Getter
    private static boolean loadedFromFile = false;

    public static boolean getDefaultEnableDarkNight()
    {
        return DEFAULT_ENABLE_DARK_NIGHT;
    }

    public static boolean getDefaultDependenceMoon()
    {
        return DEFAULT_DEPENDENCE_MOON;
    }

    public static float[] getDefaultMoonPhaseFactors()
    {
        return DEFAULT_MOON_PHASE_FACTORS.clone();
    }

    public static float[] getMoonPhaseFactorsArray()
    {
        return moonPhaseFactorsArray.clone();
    }

    public static float getMoonPhaseFactor(int phase)
    {
        if (phase >= 0 && phase < moonPhaseFactorsArray.length)
        {
            return moonPhaseFactorsArray[phase];
        }
        return 0.0F;
    }

    public static void loadFromFile(String filePath)
    {
        if (filePath == null)
        {
            EarlyLogBuffer.log(Log.ERROR, "Cannot load config: file path is null");
            resetToDefault();
            return;
        }

        try
        {
            File configFile = new File(filePath);

            if (!configFile.exists())
            {
                EarlyLogBuffer.log(Log.WARN,
                        "Night renderer config not found at: " + filePath + ", using defaults");
                resetToDefault();
                return;
            }

            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(configFile));

            if (!jsonElement.isJsonArray())
            {
                throw new JsonParseException("Root element is not an array");
            }

            JsonArray array = jsonElement.getAsJsonArray();
            boolean configLoaded = false;

            for (JsonElement element : array)
            {
                if (!element.isJsonObject()) continue;

                JsonObject obj = element.getAsJsonObject();

                if (!obj.has("DSCNightRenderer")) continue;

                JsonObject configObj = obj.getAsJsonObject("DSCNightRenderer");

                if (configObj.has("enableDarkNight"))
                {
                    enableDarkNight = configObj.get("enableDarkNight").getAsBoolean();
                }

                if (configObj.has("dependenceLightMoonPhase"))
                {
                    dependenceLightMoonPhase = configObj.get("dependenceLightMoonPhase").getAsBoolean();
                }

                if (configObj.has("moonPhaseFactorsArray"))
                {
                    JsonArray moonArray = configObj.getAsJsonArray("moonPhaseFactorsArray");

                    if (moonArray.size() != EXPECTED_MOON_PHASES_COUNT)
                    {
                        EarlyLogBuffer.log(Log.WARN,
                                "Invalid moon phase array size: " + moonArray.size() +
                                        ", expected " + EXPECTED_MOON_PHASES_COUNT + ". Using defaults for missing values");
                    }

                    float[] newArray = new float[EXPECTED_MOON_PHASES_COUNT];

                    for (int i = 0; i < EXPECTED_MOON_PHASES_COUNT; i++)
                    {
                        if (i < moonArray.size())
                        {
                            newArray[i] = moonArray.get(i).getAsFloat();
                        }
                        else
                        {
                            newArray[i] = DEFAULT_MOON_PHASE_FACTORS[i];
                        }
                    }

                    moonPhaseFactorsArray = newArray;
                }

                configLoaded = true;
                loadedFromFile = true;

                EarlyLogBuffer.log(Log.INFO,
                        "Loaded night renderer config: enableDarkNight=" + enableDarkNight +
                                ", dependenceLightMoonPhase=" + dependenceLightMoonPhase +
                                ", moonPhaseFactors=" + Arrays.toString(moonPhaseFactorsArray));
                break;
            }

            if (!configLoaded)
            {
                EarlyLogBuffer.log(Log.WARN,
                        "Night renderer config has unknown format, using defaults");
                resetToDefault();
            }
        }
        catch (Exception exception)
        {
            EarlyLogBuffer.log(Log.ERROR,
                    "Failed to load night renderer config: " + exception.getMessage());
            exception.printStackTrace();
            resetToDefault();
        }
    }

    public static void resetToDefault()
    {
        enableDarkNight = DEFAULT_ENABLE_DARK_NIGHT;
        dependenceLightMoonPhase = DEFAULT_DEPENDENCE_MOON;
        moonPhaseFactorsArray = DEFAULT_MOON_PHASE_FACTORS.clone();
        loadedFromFile = false;

        EarlyLogBuffer.log(Log.INFO, "Reset night renderer config to defaults");
    }
}