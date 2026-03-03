package org.imesense.dynamicspawncontrol.core.mixinconfig.srparasites;

import java.util.*;
import java.io.*;
import com.google.gson.*;
import lombok.Getter;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;

public final class SRParasitesBlacklistData
{
    private static final String[] DEFAULT_BLACKLIST =
    {
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
        "divinerpg:saguaro_worm",
        "divinerpg:shark",
        "divinerpg:smelter",
        "divinerpg:snapper",
        "divinerpg:stone_golem",
        "divinerpg:whale",
        "divinerpg:hell_pig",
        "divinerpg:ender_triplets",
        "divinerpg:fractite",
        "divinerpg:frost_archer",
        "divinerpg:workshop_merchant",
        "divinerpg:workshop_tinkerer",
        "divinerpg:bunny",
        "divinerpg:weak_cori",
        "divinerpg:mage",
        "divinerpg:moon_wolf",
        "divinerpg:spellbinder",
        "divinerpg:mystic",
        "divinerpg:sorcerer",
        "divinerpg:captain_merik",
        "divinerpg:datticon",
        "divinerpg:fyracryx",
        "divinerpg:kazari",
        "divinerpg:leorna",
        "divinerpg:paratiku",
        "divinerpg:golem_of_rejuvenation",
        "divinerpg:seimer",
        "divinerpg:lord_vatticus",
        "divinerpg:war_general",
        "divinerpg:wraith",
        "divinerpg:zelus",
        "divinerpg:crypt_keeper",
        "divinerpg:dissiment",
        "divinerpg:dreamwrecker",
        "divinerpg:lheiva",
        "divinerpg:mysterious_man_layer_1",
        "divinerpg:mysterious_man_layer_2",
        "divinerpg:mysterious_man_layer_3",
        "divinerpg:shadahier",
        "divinerpg:the_hunger",
        "divinerpg:temple_guardian",
        "divinerpg:twins",
        "divinerpg:zone",
        "divinerpg:zoragon",
        "divinerpg:experienced_cori",
        "divinerpg:the_watcher",
        "divinerpg:twilight_demon"
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

                    EarlyLogBuffer.log(LogManager.INFO,"Loaded " + list.size() + " entities from blacklist file");
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

                        EarlyLogBuffer.log(LogManager.INFO,"Loaded " + list.size() + " entities from blacklist file (object format)");
                    }
                }
            }
        }
        catch (Exception exception)
        {
            EarlyLogBuffer.log(LogManager.ERROR,"Failed to load blacklist from file: " + exception.getMessage());

            activeBlacklist = DEFAULT_BLACKLIST.clone();
            loadedFromFile = false;
        }
    }

    public static void resetToDefault()
    {
        activeBlacklist = DEFAULT_BLACKLIST.clone();
        loadedFromFile = false;

        EarlyLogBuffer.log(LogManager.INFO, "Reset blacklist to default");
    }
}