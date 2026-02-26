package org.imesense.dynamicspawncontrol.core.mixinconfig.nightrenderer;

import lombok.Getter;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class NightRendererData
{
    private static final boolean DEFAULT_ENABLE_DARK_NIGHT = true;
    private static final boolean DEFAULT_DEPENDENCE_MOON = true;
    private static final float[] DEFAULT_MOON_PHASE_FACTORS = new float[]
    {
        0.06f, 0.04f, 0.03f, 0.02f, 0.0f, 0.01f, 0.02f, 0.04f
    };

    private static final int[] DEFAULT_BLACKLIST_DIMENSIONS = new int[]
    {
        -1, 1, 7, 420, 421, 422, 423, 424, 425, 426, 427
    };

    private static final int EXPECTED_MOON_PHASES_COUNT = 8;

    @Getter
    private static boolean enableDarkNight = DEFAULT_ENABLE_DARK_NIGHT;

    @Getter
    private static boolean dependenceLightMoonPhase = DEFAULT_DEPENDENCE_MOON;
    private static float[] moonPhaseFactorsArray = DEFAULT_MOON_PHASE_FACTORS.clone();

    @Getter
    private static boolean loadedFromFile = false;

    private static Set<Integer> blacklistedDimensions = Arrays.stream(DEFAULT_BLACKLIST_DIMENSIONS)
            .boxed()
            .collect(Collectors.toCollection(HashSet::new));

    public static int[] getDefaultBlacklistDimensions()
    {
        return DEFAULT_BLACKLIST_DIMENSIONS.clone();
    }

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

    public static boolean isDimensionBlacklisted(int dimensionId)
    {
        return blacklistedDimensions.contains(dimensionId);
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

                if (configObj.has("blacklistedDimensions"))
                {
                    JsonArray blacklistArray = configObj.getAsJsonArray("blacklistedDimensions");
                    Set<Integer> newBlacklist = new HashSet<>();

                    for (int i = 0; i < blacklistArray.size(); i++)
                    {
                        newBlacklist.add(blacklistArray.get(i).getAsInt());
                    }

                    blacklistedDimensions = newBlacklist;

                    EarlyLogBuffer.log(Log.INFO,
                            "Loaded blacklisted dimensions: " + blacklistedDimensions);
                }

                configLoaded = true;
                loadedFromFile = true;

                EarlyLogBuffer.log(Log.INFO,
                        "Loaded night renderer config: enableDarkNight=" + enableDarkNight +
                                ", dependenceLightMoonPhase=" + dependenceLightMoonPhase +
                                ", moonPhaseFactors=" + Arrays.toString(moonPhaseFactorsArray) +
                                ", blacklistedDimensions=" + blacklistedDimensions);
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

        blacklistedDimensions = Arrays.stream(DEFAULT_BLACKLIST_DIMENSIONS)
                .boxed().collect(Collectors.toCollection(HashSet::new));

        loadedFromFile = false;

        EarlyLogBuffer.log(Log.INFO, "Reset night renderer config to defaults");
    }
}