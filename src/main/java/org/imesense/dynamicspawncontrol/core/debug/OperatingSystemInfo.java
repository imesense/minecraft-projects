package org.imesense.dynamicspawncontrol.core.debug;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public final class OperatingSystemInfo
{
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    public enum OSType
    {
        WINDOWS,
        LINUX,
        MAC,
        UNKNOWN
    }

    private OperatingSystemInfo() {}

    public static OSType getOsType()
    {
        if (OS_NAME.contains("win"))
        {
            return OSType.WINDOWS;
        }
        else if (OS_NAME.contains("nix") || OS_NAME.contains("nux") || OS_NAME.contains("aix"))
        {
            return OSType.LINUX;
        }
        else if (OS_NAME.contains("mac"))
        {
            return OSType.MAC;
        }

        return OSType.UNKNOWN;
    }

    public static String getOsName()
    {
        return System.getProperty("os.name");
    }

    public static String getOsVersion()
    {
        return System.getProperty("os.version");
    }

    public static String getOsArch()
    {
        return System.getProperty("os.arch");
    }

    public static String getFullOsInfo()
    {
        return String.format("%s %s (%s)",
                getOsName(), getOsVersion(), getOsArch());
    }

    public static String getSystemMemoryInfo()
    {
        try
        {
            OperatingSystemMXBean osBean =
                    (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

            long total = osBean.getTotalPhysicalMemorySize();
            long free = osBean.getFreePhysicalMemorySize();

            return formatBytes(total) + " total / " + formatBytes(free) + " free";
        }
        catch (Exception exception)
        {
            return "Unknown";
        }
    }

    private static String formatBytes(long bytes)
    {
        if (bytes < 1024)
        {
            return bytes + " B";
        }

        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";

        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}
