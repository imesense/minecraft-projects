package org.imesense.dynamicspawncontrol.core.debug;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public final class GpuInfo
{
    private GpuInfo() {}

    public static String getAllGpusInfo()
    {
        OperatingSystemInfo.OSType osType = OperatingSystemInfo.getOsType();

        try
        {
            switch (osType)
            {
                case WINDOWS:
                    return getWindowsAllGpusInfo();
                case LINUX:
                    return getLinuxAllGpusInfo();
                case MAC:
                    return getMacAllGpusInfo();
                default:
                    return "Unknown GPU";
            }
        }
        catch (Exception ignored)
        {
            return "Unknown GPU";
        }
    }

    public static String getActiveGpuInfo()
    {
        try
        {
            if (GLContext.getCapabilities() != null)
            {
                String renderer = GL11.glGetString(GL11.GL_RENDERER);
                String vendor = GL11.glGetString(GL11.GL_VENDOR);
                String version = GL11.glGetString(GL11.GL_VERSION);

                if (renderer != null && vendor != null)
                {
                    return String.format("%s (%s) - OpenGL: %s",
                            renderer, vendor, version != null ? version : "Unknown");
                }
            }

            if (OperatingSystemInfo.getOsType() == OperatingSystemInfo.OSType.WINDOWS)
            {
                return getWindowsActiveGpuInfo();
            }
        }
        catch (Exception ignored) {}

        return "Unknown";
    }

    public static String getGpuDriverVersion()
    {
        OperatingSystemInfo.OSType osType = OperatingSystemInfo.getOsType();

        try
        {
            switch (osType)
            {
                case WINDOWS:
                    return getWindowsGpuDriverVersion();
                case LINUX:
                    return getLinuxGpuDriverVersion();
                default:
                    return "Unknown";
            }
        }
        catch (Exception ignored)
        {
            return "Unknown";
        }
    }

    public static String getVramInfo()
    {
        try
        {
            if (GLContext.getCapabilities() != null)
            {
                String nvidiaVram = getNvidiaVramInfo();
                if (nvidiaVram != null) return nvidiaVram;

                String amdVram = getAmdVramInfo();
                if (amdVram != null) return amdVram;
            }

            if (OperatingSystemInfo.getOsType() == OperatingSystemInfo.OSType.WINDOWS)
            {
                return getWindowsVramInfo();
            }
        }
        catch (Exception ignored) {}

        return "Unknown";
    }

    private static String getWindowsAllGpusInfo() throws Exception
    {
        Process process = Runtime.getRuntime().exec("wmic path win32_VideoController get name");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder gpuInfo = new StringBuilder();
        String line;
        boolean firstLine = true;

        reader.readLine();

        while ((line = reader.readLine()) != null)
        {
            line = line.trim();
            if (!line.isEmpty())
            {
                if (!firstLine)
                {
                    gpuInfo.append(" | ");
                }
                gpuInfo.append(line);
                firstLine = false;
            }
        }

        return gpuInfo.length() > 0 ? gpuInfo.toString() : "Unknown GPU";
    }

    private static String getLinuxAllGpusInfo() throws Exception
    {
        Process process = Runtime.getRuntime().exec("lspci | grep VGA");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();

        if (line != null && line.contains(":"))
        {
            return line.substring(line.indexOf(":") + 1).trim();
        }

        return "Unknown GPU";
    }

    private static String getMacAllGpusInfo() throws Exception
    {
        Process process = Runtime.getRuntime().exec("system_profiler SPDisplaysDataType | grep Chipset");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();

        if (line != null && line.contains(":"))
        {
            return line.split(":")[1].trim();
        }

        return "Unknown GPU";
    }

    private static String getWindowsActiveGpuInfo() throws Exception
    {
        Process process = Runtime.getRuntime().exec("wmic path win32_VideoController get name");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;

        reader.readLine();

        while ((line = reader.readLine()) != null)
        {
            line = line.trim();
            if (!line.isEmpty())
            {
                return line + " (via WMI)";
            }
        }

        return "Unknown";
    }

    private static String getWindowsGpuDriverVersion() throws Exception
    {
        Process process = Runtime.getRuntime().exec("wmic path win32_VideoController get DriverVersion");
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

        return "Unknown";
    }

    private static String getLinuxGpuDriverVersion() throws Exception
    {
        Process process = Runtime.getRuntime().exec("glxinfo | grep 'OpenGL version'");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();

        if (line != null && line.contains(":"))
        {
            return line.split(":")[1].trim();
        }

        return "Unknown";
    }

    private static String getNvidiaVramInfo()
    {
        if (GLContext.getCapabilities().GL_NVX_gpu_memory_info)
        {
            int totalKB = GL11.glGetInteger(0x9048);
            int currentKB = GL11.glGetInteger(0x9049);

            if (totalKB > 0)
            {
                long totalBytes = totalKB * 1024L;
                long freeBytes = currentKB * 1024L;
                return formatBytes(totalBytes) + " total / " + formatBytes(freeBytes) + " free (NVIDIA)";
            }
        }

        return null;
    }

    private static String getAmdVramInfo()
    {
        if (GLContext.getCapabilities().GL_ATI_meminfo)
        {
            int totalMemory = 0;
            int freeMemory = 0;

            try
            {
                totalMemory = GL11.glGetInteger(0x87FB);
                freeMemory = GL11.glGetInteger(0x87FC);
            }
            catch (Exception ignored) {}

            if (totalMemory > 0)
            {
                long totalBytes = totalMemory * 1024L;
                long freeBytes = freeMemory * 1024L;

                return formatBytes(totalBytes) + " total / " + formatBytes(freeBytes) + " free (AMD/ATI)";
            }
        }

        return null;
    }

    private static String getWindowsVramInfo()
    {
        try
        {
            Process process = Runtime.getRuntime().exec(
                    "wmic path win32_VideoController get name,adapterram /format:csv"
            );

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();

            reader.readLine();

            while ((line = reader.readLine()) != null)
            {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");

                if (parts.length >= 2)
                {
                    String gpuName = parts.length > 1 ? parts[1].trim() : "";

                    if (!gpuName.isEmpty() && !gpuName.equals("Name") &&
                            (gpuName.contains("NVIDIA") || gpuName.contains("AMD") ||
                                    gpuName.contains("Radeon") || gpuName.contains("Intel"))) {

                        if (result.length() > 0)
                        {
                            result.append(" | ");
                        }

                        result.append(gpuName);

                        if (parts.length > 2 && parts[2] != null)
                        {
                            String vramStr = parts[2].trim();

                            if (vramStr.matches("\\d+"))
                            {
                                try
                                {
                                    long vram = Long.parseLong(vramStr);
                                    result.append(" [").append(formatBytes(vram)).append("]");
                                }
                                catch (NumberFormatException ignored) {}
                            }
                        }
                    }
                }
            }

            if (result.length() > 0)
            {
                return result.toString();
            }
        }
        catch (Exception ignored) {}

        return "Unknown";
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
