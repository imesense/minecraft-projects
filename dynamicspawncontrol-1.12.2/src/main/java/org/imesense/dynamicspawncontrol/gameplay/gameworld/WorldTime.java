package org.imesense.dynamicspawncontrol.gameplay.gameworld;

import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.technical.configs.SettingsWorldTime;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.math.BigDecimal;
import java.util.Collections;

/**
 *
 */
public class WorldTime
{
    /**
     *
     * @param nameClass
     */
    public WorldTime(final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);
    }

    /**
     *
     */
    private static final double day_multiplier = multiplier(SettingsWorldTime.DayLengthMinutes);

    /**
     *
     */
    private static final double night_multiplier = multiplier(SettingsWorldTime.NightLengthMinutes);

    /**
     *
     * @param worldTime
     * @return
     */
    public static double multiplier(long worldTime)
    {
        return isDaytime(worldTime) ? day_multiplier : night_multiplier;
    }

    /**
     *
     * @param worldTime
     * @return
     */
    public static long customTime(long worldTime)
    {
        return (long)((double)worldTime * multiplier(worldTime));
    }

    /**
     *
     * @param customTime
     * @param multiplier
     * @return
     */
    private static long worldTime(long customTime, double multiplier)
    {
        return (long)((double)customTime / multiplier);
    }

    /**
     *
     * @param world
     * @param customTime
     * @param multiplier
     */
    public static void setWorldTime(World world, long customTime, double multiplier)
    {
        world.provider.setWorldTime(worldTime(customTime, multiplier));
    }

    /**
     *
     * @param hour
     * @param minute
     * @param day
     * @return
     */
    public static long systemTime(int hour, int minute, int day)
    {
        hour = (hour - 6 + 24) % 24 * 1000;
        minute = (int)Math.round((double)minute * 16.94D % 1000.0D);
        return (long)(hour + minute) + (long)day * 24000L;
    }

    /**
     *
     * @param worldTime
     * @return
     */
    public static long day(long worldTime)
    {
        return worldTime / 24000L;
    }

    /**
     *
     * @param item
     * @param addition
     * @return
     */
    public static String progressString(long item, String addition)
    {
        item %= 12000L;
        int percent = (int)(item * 100L / 12000L);
        int division = 2;

        return String.join
                ("", Collections.nCopies(percent == 0 ? 2 : 2 - (int)Math.log10((double)percent), " "))
                + String.format(" %d%% [", percent) + String.join("", Collections.nCopies(percent / division, "="))
                + '>' + String.join("", Collections.nCopies(50 - percent / division, " ")) + ']'
                + String.join("", Collections.nCopies(item == 0L ? (int)Math.log10(12000.0D)
                : (int)Math.log10(12000.0D) - (int)Math.log10((double)item), " "))
                + String.format(" %d/%d%s", item, 12000, addition);
    }

    /**
     *
     * @param length
     * @return
     */
    private static double multiplier(int length)
    {
        return (new BigDecimal(String.valueOf((double)length / 10.0D))).setScale(2, 6).doubleValue();
    }

    /**
     *
     * @param worldTime
     * @return
     */
    public static boolean isDaytime(long worldTime)
    {
        return worldTime % 24000L >= 0L && worldTime % 24000L < 12000L;
    }
}
