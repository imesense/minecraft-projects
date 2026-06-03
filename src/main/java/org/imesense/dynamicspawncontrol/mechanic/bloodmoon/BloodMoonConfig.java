package org.imesense.dynamicspawncontrol.mechanic.bloodmoon;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.Setter;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.base.BaseJsonConfig;

@Getter
@Setter
@InitLog
@ConceptConfig(fileName = "cfg_bloodmoon")
public final class BloodMoonConfig extends BaseJsonConfig
{
    private General general = new General();
    private Appearance appearance = new Appearance();
    private Schedule schedule = new Schedule();
    private Spawning spawning = new Spawning();

    public BloodMoonConfig(String configPath)
    {
        super(configPath, true);
        loadOrCreateConfig();
    }

    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject jsonObject = new JsonObject();

        JsonObject generalObj = new JsonObject();
        generalObj.addProperty("no_sleep", general.isNoSleep());
        generalObj.addProperty("vanish", general.isVanish());
        generalObj.addProperty("respect_gamerule", general.isRespectGamerule());
        generalObj.addProperty("send_message", general.isSendMessage());
        jsonObject.add("general", generalObj);

        JsonObject appearanceObj = new JsonObject();
        appearanceObj.addProperty("red_moon", appearance.isRedMoon());
        appearanceObj.addProperty("red_sky", appearance.isRedSky());
        appearanceObj.addProperty("red_light", appearance.isRedLight());
        appearanceObj.addProperty("black_fog", appearance.isBlackFog());
        jsonObject.add("appearance", appearanceObj);

        JsonObject scheduleObj = new JsonObject();
        scheduleObj.addProperty("chance", schedule.getChance());
        scheduleObj.addProperty("fullmoon", schedule.isFullmoon());
        scheduleObj.addProperty("nth_night", schedule.getNthNight());
        jsonObject.add("schedule", scheduleObj);

        JsonObject spawningObj = new JsonObject();
        spawningObj.addProperty("spawn_speed", spawning.getSpawnSpeed());
        spawningObj.addProperty("spawn_limit_multiplier", spawning.getSpawnLimitMultiplier());
        spawningObj.addProperty("spawn_range", spawning.getSpawnRange());
        spawningObj.addProperty("world_spawn_distance", spawning.getWorldSpawnDistance());

        JsonArray whitelistArray = new JsonArray();

        for (String item : spawning.getSpawnWhitelist())
        {
            whitelistArray.add(new JsonPrimitive(item));
        }

        spawningObj.add("spawn_whitelist", whitelistArray);

        JsonArray blacklistArray = new JsonArray();

        for (String item : spawning.getSpawnBlacklist())
        {
            blacklistArray.add(new JsonPrimitive(item));
        }

        spawningObj.add("spawn_blacklist", blacklistArray);

        jsonObject.add("spawning", spawningObj);

        return jsonObject;
    }

    @Override
    protected void applyConfig(JsonObject jsonObject)
    {
        if (jsonObject.has("general") && jsonObject.get("general").isJsonObject())
        {
            JsonObject generalObj = jsonObject.getAsJsonObject("general");

            if (generalObj.has("no_sleep")) general.setNoSleep(generalObj.get("no_sleep").getAsBoolean());
            if (generalObj.has("vanish")) general.setVanish(generalObj.get("vanish").getAsBoolean());
            if (generalObj.has("respect_gamerule")) general.setRespectGamerule(generalObj.get("respect_gamerule").getAsBoolean());
            if (generalObj.has("send_message")) general.setSendMessage(generalObj.get("send_message").getAsBoolean());
        }

        if (jsonObject.has("appearance") && jsonObject.get("appearance").isJsonObject())
        {
            JsonObject appearanceObj = jsonObject.getAsJsonObject("appearance");
            if (appearanceObj.has("red_moon")) appearance.setRedMoon(appearanceObj.get("red_moon").getAsBoolean());
            if (appearanceObj.has("red_sky")) appearance.setRedSky(appearanceObj.get("red_sky").getAsBoolean());
            if (appearanceObj.has("red_light")) appearance.setRedLight(appearanceObj.get("red_light").getAsBoolean());
            if (appearanceObj.has("black_fog")) appearance.setBlackFog(appearanceObj.get("black_fog").getAsBoolean());
        }

        if (jsonObject.has("schedule") && jsonObject.get("schedule").isJsonObject())
        {
            JsonObject scheduleObj = jsonObject.getAsJsonObject("schedule");

            if (scheduleObj.has("chance")) schedule.setChance(scheduleObj.get("chance").getAsDouble());
            if (scheduleObj.has("fullmoon")) schedule.setFullmoon(scheduleObj.get("fullmoon").getAsBoolean());
            if (scheduleObj.has("nth_night")) schedule.setNthNight(scheduleObj.get("nth_night").getAsInt());
        }

        // Загрузка Spawning секции
        if (jsonObject.has("spawning") && jsonObject.get("spawning").isJsonObject())
        {
            JsonObject spawningObj = jsonObject.getAsJsonObject("spawning");

            if (spawningObj.has("spawn_speed")) spawning.setSpawnSpeed(spawningObj.get("spawn_speed").getAsInt());
            if (spawningObj.has("spawn_limit_multiplier")) spawning.setSpawnLimitMultiplier(spawningObj.get("spawn_limit_multiplier").getAsInt());
            if (spawningObj.has("spawn_range")) spawning.setSpawnRange(spawningObj.get("spawn_range").getAsInt());
            if (spawningObj.has("world_spawn_distance")) spawning.setWorldSpawnDistance(spawningObj.get("world_spawn_distance").getAsInt());

            if (spawningObj.has("spawn_whitelist") && spawningObj.get("spawn_whitelist").isJsonArray())
            {
                JsonArray whitelistArray = spawningObj.getAsJsonArray("spawn_whitelist");
                String[] whitelist = new String[whitelistArray.size()];

                for (int i = 0; i < whitelistArray.size(); i++)
                {
                    whitelist[i] = whitelistArray.get(i).getAsString();
                }

                spawning.setSpawnWhitelist(whitelist);
            }

            if (spawningObj.has("spawn_blacklist") && spawningObj.get("spawn_blacklist").isJsonArray())
            {
                JsonArray blacklistArray = spawningObj.getAsJsonArray("spawn_blacklist");
                String[] blacklist = new String[blacklistArray.size()];
                for (int i = 0; i < blacklistArray.size(); i++)
                {
                    blacklist[i] = blacklistArray.get(i).getAsString();
                }

                spawning.setSpawnBlacklist(blacklist);
            }
        }
    }

    @Getter
    @Setter
    public static class General
    {
        private boolean noSleep = true;
        private boolean vanish = false;
        private boolean respectGamerule = true;
        private boolean sendMessage = true;
    }

    @Getter
    @Setter
    public static class Appearance
    {
        private boolean redMoon = true;
        private boolean redSky = true;
        private boolean redLight = true;
        private boolean blackFog = true;
    }

    @Getter
    @Setter
    public static class Schedule
    {
        private double chance = 0.05d;
        private boolean fullmoon = false;
        private int nthNight = 0;
    }

    @Getter
    @Setter
    public static class Spawning
    {
        private int spawnSpeed = 4;
        private int spawnLimitMultiplier = 4;
        private int spawnRange = 2;
        private int worldSpawnDistance = 24;
        private String[] spawnWhitelist = new String[0];
        private String[] spawnBlacklist = new String[0];
    }
}