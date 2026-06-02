package org.imesense.dynamicspawncontrol.core.debug;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public final class CpuInfo
{
    private CpuInfo() {}

    public static String getCpuModel()
    {
        OperatingSystemInfo.OSType osType = OperatingSystemInfo.getOsType();

        try
        {
            switch (osType)
            {
                case WINDOWS:
                    return getWindowsCpuModel();
                case LINUX:
                case MAC:
                    return getUnixCpuModel();
                default:
                    return "Unknown CPU";
            }
        }
        catch (Exception ignored)
        {
            return "Unknown CPU";
        }
    }

    public static String getCpuCoreInfo()
    {
        OperatingSystemInfo.OSType osType = OperatingSystemInfo.getOsType();

        try
        {
            if (osType == OperatingSystemInfo.OSType.WINDOWS)
            {
                return getWindowsCpuCoreInfo();
            }
        }
        catch (Exception ignored) {}

        int threads = Runtime.getRuntime().availableProcessors();
        return threads + " threads";
    }

    private static String getWindowsCpuModel() throws Exception
    {
        Process process = Runtime.getRuntime().exec("wmic cpu get name");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;

        reader.readLine();

        while ((line = reader.readLine()) != null)
        {
            line = line.trim();
            if (!line.isEmpty())
            {
                return line;
            }
        }

        return "Unknown CPU";
    }

    private static String getUnixCpuModel() throws Exception
    {
        Process process = Runtime.getRuntime().exec("cat /proc/cpuinfo | grep 'model name' | head -1");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();

        if (line != null && line.contains(":"))
        {
            return line.split(":")[1].trim();
        }

        return "Unknown CPU";
    }

    private static String getWindowsCpuCoreInfo() throws Exception
    {
        Process process = Runtime.getRuntime().exec(
                "wmic cpu get NumberOfCores,NumberOfLogicalProcessors");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        String line;
        reader.readLine();

        while ((line = reader.readLine()) != null)
        {
            if (line.trim().matches("\\d+\\s+\\d+"))
            {
                String[] parts = line.trim().split("\\s+");
                return parts[0] + " cores / " + parts[1] + " threads";
            }
        }

        int threads = Runtime.getRuntime().availableProcessors();
        return threads + " threads";
    }
}
