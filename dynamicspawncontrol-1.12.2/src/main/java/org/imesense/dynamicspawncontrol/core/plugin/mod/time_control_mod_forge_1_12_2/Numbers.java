package org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2;

import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.pluginconfig.timecontrol.PluginTimeControlConfig;

import java.math.BigDecimal;
import java.util.Collections;

public final class Numbers
{
    static final long night_start = 12000L;
    private static final double day_multiplier = multiplier(PluginTimeControlConfig.getInstance(PluginTimeControlConfig.class).getDayLengthMinutes());
    private static final double night_multiplier = multiplier(PluginTimeControlConfig.getInstance(PluginTimeControlConfig.class).getNightLengthMinutes());
    private static final int irl_hour_offset = 6;
    private static final double irl_minute_multiplier = 16.94D;

    public static double multiplier(long worldTime)
    {
        return isDaytime(worldTime) ? day_multiplier : night_multiplier;
    }

    public static long customtime(long worldTime)
    {
        return (long)((double)worldTime * multiplier(worldTime));
    }

    private static long worldtime(long customTime, double multiplier)
    {
        return (long)((double)customTime / multiplier);
    }

    public static void setWorldtime(World world, long customTime, double multiplier)
    {
        world.provider.setWorldTime(worldtime(customTime, multiplier));
    }

    public static long systemtime(int hour, int minute, int day)
    {
        hour = (hour - 6 + 24) % 24 * 1000;
        minute = (int)Math.round((double)minute * 16.94D % 1000.0D);

        return (long)(hour + minute) + (long)day * 24000L;
    }

    public static long day(long worldTime)
    {
        return worldTime / 24000L;
    }

    public static String progressString(long item, String addition)
    {
        int stringLength = 1;
        item %= 12000L;
        int total = 1;
        int percent = (int)(item * 100L / 12000L);
        int division = 2;
        return String.join("", Collections.nCopies(percent == 0 ? 2 : 2 - (int)Math.log10((double)percent), " ")) + String.format(" %d%% [", percent) + String.join("", Collections.nCopies(percent / division, "=")) + '>' + String.join("", Collections.nCopies(50 - percent / division, " ")) + ']' + String.join("", Collections.nCopies(item == 0L ? (int)Math.log10(12000.0D) : (int)Math.log10(12000.0D) - (int)Math.log10((double)item), " ")) + String.format(" %d/%d%s", item, 12000, addition);
    }

    private static double multiplier(int length)
    {
        return (new BigDecimal(String.valueOf((double)length / 10.0D))).setScale(2, 6).doubleValue();
    }

    public static boolean isDaytime(long worldTime)
    {
        return worldTime % 24000L >= 0L && worldTime % 24000L < 12000L;
    }
}
