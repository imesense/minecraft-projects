package org.imesense.dynamicspawncontrol.core.mixinconfig.specialmobs;

import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import com.google.gson.*;

import java.io.File;
import java.io.FileReader;

public final class SpecialMobsReplacerData
{
    private static final boolean DEFAULT_REPLACE_VANILLA = false;

    private static boolean replaceVanilla = DEFAULT_REPLACE_VANILLA;

    @lombok.Getter
    private static boolean loadedFromFile = false;

    public static boolean shouldReplaceVanilla()
    {
        return replaceVanilla;
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

                            if (obj.has("DSCSpecialMobsReplaceVanillaMobs"))
                            {
                                JsonObject configObj = obj.getAsJsonObject("DSCSpecialMobsReplaceVanillaMobs");

                                if (configObj.has("all_replace_vanilla"))
                                {
                                    replaceVanilla = configObj.get("all_replace_vanilla").getAsBoolean();
                                    loadedFromFile = true;

                                    EarlyLogBuffer.log(Log.INFO,
                                            "Loaded SpecialMobs replacer config: all_replace_vanilla = " + replaceVanilla);
                                    return;
                                }
                            }
                        }
                    }
                }

                EarlyLogBuffer.log(Log.WARN,
                        "SpecialMobs replacer config has unknown format, using defaults");

                replaceVanilla = DEFAULT_REPLACE_VANILLA;
                loadedFromFile = false;
            }
        }
        catch (Exception exception)
        {
            EarlyLogBuffer.log(Log.ERROR,
                    "Failed to load SpecialMobs replacer config: " + exception.getMessage());

            replaceVanilla = DEFAULT_REPLACE_VANILLA;
            loadedFromFile = false;
        }
    }

    public static void resetToDefault()
    {
        replaceVanilla = DEFAULT_REPLACE_VANILLA;
        loadedFromFile = false;

        EarlyLogBuffer.log(Log.INFO, "Reset SpecialMobs replacer config to default: false");
    }

    public static void setReplaceVanilla(boolean value)
    {
        replaceVanilla = value;
        EarlyLogBuffer.log(Log.INFO, "SpecialMobs replacer config set to: " + value);
    }
}