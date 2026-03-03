package org.imesense.dynamicspawncontrol.core.debug;

import java.lang.management.ManagementFactory;

public final class JavaInfo
{
    private JavaInfo() {}

    public static String getJavaVersion()
    {
        return System.getProperty("java.version");
    }

    public static String getJavaVendor()
    {
        return System.getProperty("java.vendor");
    }

    public static String getFullJavaInfo()
    {
        return getJavaVersion() + " (" + getJavaVendor() + ")";
    }

    public static String getJvmArgs()
    {
        return String.join(" ",
                ManagementFactory.getRuntimeMXBean().getInputArguments());
    }

    public static String getTotalMemory()
    {
        return formatBytes(Runtime.getRuntime().totalMemory());
    }

    public static String getMaxMemory()
    {
        return formatBytes(Runtime.getRuntime().maxMemory());
    }

    public static int getAvailableProcessors()
    {
        return Runtime.getRuntime().availableProcessors();
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
