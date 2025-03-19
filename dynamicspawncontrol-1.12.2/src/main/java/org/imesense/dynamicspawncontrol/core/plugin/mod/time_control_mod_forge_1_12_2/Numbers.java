package org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2;

import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.pluginconfig.timecontrol.PluginTimeControlConfig;

import java.math.BigDecimal;
import java.util.Collections;

public final class Numbers
{
    private static final double day_multiplier =
            multiplier(PluginTimeControlConfig.getInstance(PluginTimeControlConfig.class).getDayLengthMinutes());

    private static final double night_multiplier =
            multiplier(PluginTimeControlConfig.getInstance(PluginTimeControlConfig.class).getNightLengthMinutes());

    public static double multiplier(long worldTime)
    {
        return isDaytime(worldTime) ? day_multiplier : night_multiplier;
    }

    public static long _customTime(long worldTime)
    {
        return (long)((double)worldTime * multiplier(worldTime));
    }

    private static long _worldTime(long customTime, double multiplier)
    {
        return (long)((double)customTime / multiplier);
    }

    public static void _setWorldTime(World world, long customTime, double multiplier)
    {
        world.provider.setWorldTime(_worldTime(customTime, multiplier));
    }

    public static long _systemTime(int hour, int minute, int day)
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
        item %= 12000L;
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
