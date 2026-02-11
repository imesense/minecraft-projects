package org.imesense.dynamicspawncontrol.core.mixinconfig.srparasites;

import java.util.*;
import java.io.*;
import com.google.gson.*;
import lombok.Getter;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

public final class SRParasitesBlacklistData
{
    private static final String[] DEFAULT_BLACKLIST =
    {
        "divinerpg:saguaro_worm",
        "divinerpg:crab",
        "divinerpg:cyclops",
        "divinerpg:ehu",
        "divinerpg:brown_grizzle",
        "divinerpg:white_grizzle",
        "divinerpg:husk",
        "divinerpg:jack_o_man",
        "divinerpg:king_crab",
        "divinerpg:kobblin",
        "divinerpg:liopleurodon",
        "divinerpg:livestock_merchant",
        "divinerpg:pumpkin_spider",
        "divinerpg:rainbour",
        "divinerpg:shark",
        "divinerpg:smelter",
        "divinerpg:snapper",
        "divinerpg:stone_golem",
        "divinerpg:whale"
    };

    private static String[] activeBlacklist = DEFAULT_BLACKLIST.clone();

    @Getter
    private static boolean loadedFromFile = false;

    public static String[] getActiveBlacklist()
    {
        return activeBlacklist.clone();
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
                    List<String> list = new ArrayList<>();

                    for (JsonElement element : array)
                    {
                        list.add(element.getAsString());
                    }

                    activeBlacklist = list.toArray(new String[0]);
                    loadedFromFile = true;

                    EarlyLogBuffer.log(Log.INFO,"Loaded " + list.size() + " entities from blacklist file");
                }
                else if (jsonElement.isJsonObject())
                {
                    JsonObject obj = jsonElement.getAsJsonObject();

                    if (obj.has("DSCBlackListCOTHMobImmune"))
                    {
                        JsonArray array = obj.getAsJsonArray("DSCBlackListCOTHMobImmune");
                        List<String> list = new ArrayList<>();

                        for (JsonElement element : array)
                        {
                            list.add(element.getAsString());
                        }

                        activeBlacklist = list.toArray(new String[0]);
                        loadedFromFile = true;

                        EarlyLogBuffer.log(Log.INFO,"Loaded " + list.size() + " entities from blacklist file (object format)");
                    }
                }
            }
        }
        catch (Exception exception)
        {
            EarlyLogBuffer.log(Log.ERROR,"Failed to load blacklist from file: " + exception.getMessage());

            activeBlacklist = DEFAULT_BLACKLIST.clone();
            loadedFromFile = false;
        }
    }

    public static void resetToDefault()
    {
        activeBlacklist = DEFAULT_BLACKLIST.clone();
        loadedFromFile = false;

        EarlyLogBuffer.log(Log.INFO, "Reset blacklist to default");
    }
}