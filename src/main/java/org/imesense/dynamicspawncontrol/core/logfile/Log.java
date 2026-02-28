package org.imesense.dynamicspawncontrol.core.logfile;

import lombok.var;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.config.logfile.LogFileConfig;
import org.imesense.dynamicspawncontrol.core.threads.ThreadMonitor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public final class Log
{
    private static File logFile;
    private static final ThreadMonitor threadMonitor = ThreadMonitor.getInstance();

    private static final ExecutorService EXECUTOR =

    Executors.newSingleThreadExecutor(run ->
    {
        Thread thread = new Thread(run, "[Dynamic Spawn Control - Log]");

        thread.setDaemon(true);

        return thread;
    });

    public static final int INFO = 0;
    public static final int WARN = 1;
    public static final int ERROR = 2;
    public static final int DEBUG = 3;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";

    private static final String[] LEVEL_NAMES = { "INFO", "WARN", "ERROR", "DEBUG" };
    private static final String[] LEVEL_COLORS = { ANSI_RESET, ANSI_YELLOW, ANSI_RED, ANSI_CYAN };

    public static void createLogFile(final String PATH, boolean isDebugMode)
    {
        try
        {
            String logFileName;
            File logsDir = new File(PATH, DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_LOGS);

            logsDir.mkdirs();
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

            if (isDebugMode)
            {
                logFileName = "debug_" + timestamp +
                        DynamicSpawnControlStructure.STRUCT_FILES_EXTENSION.LOG_FILE_EXTENSION;
            }
            else
            {
                logFileName = timestamp +
                        DynamicSpawnControlStructure.STRUCT_FILES_EXTENSION.LOG_FILE_EXTENSION;
            }

            logFile = new File(logsDir, logFileName);

            String header = String.join("\n",
                    "*********************************************************************",
                    "** Log file created: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                    "** DynamicsSpawnControl. Authors: OldSerpskiStalker, acidicMercury8",
                    "** Mode: " + (isDebugMode ? "DEBUG" : "RELEASE"),
                    "** JVM Args: " + getJvmArgs(),
                    "** System Information:",
                    "**   OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version") + " (" + System.getProperty("os.arch") + ")",
                    "**   Java: " + System.getProperty("java.version") + " (" + System.getProperty("java.vendor") + ")",
                    "**   CPU: " + getCpuInfo(),
                    "**   CPU Cores/Threads: " + getCpuCoreInfo(),
                    "**   GPU (All): " + getGpuInfo(),
                    "**   GPU (Active): " + getActiveGpuInfo(),
                    "**   GPU Driver: " + getGpuDriverVersion(),
                    "**   GPU VRAM: " + getVramInfo(),
                    "**   Game Allocated Memory: " + formatBytes(Runtime.getRuntime().totalMemory()),
                    "**   Game Max Memory (-Xmx): " + formatBytes(Runtime.getRuntime().maxMemory()),
                    "**   System RAM: " + getSystemMemory(),
                    "*********************************************************************",
                    ""
            );

            Files.write(logFile.toPath(), header.getBytes(), StandardOpenOption.CREATE);

            LogIsReady.markReady();
            EarlyLogBuffer.flush();

            threadMonitor.startLoggingThreadMonitoring();
        } catch (IOException ignored) { }
    }

    public static void write(int level, String message)
    {
        if (logFile == null) return;

        EXECUTOR.submit(() ->
        {
            long startTime = System.currentTimeMillis();

            try
            {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
                String logLevel = level >= 0 && level < LEVEL_NAMES.length ? LEVEL_NAMES[level] : "UNKNOWN";

                String logEntry = String.format("[%s] [%s] %s\n",
                        timestamp,
                        logLevel,
                        message
                );

                Files.write(logFile.toPath(), logEntry.getBytes(), StandardOpenOption.APPEND);

                if (level >= 0 && level < LEVEL_COLORS.length)
                {
                    System.out.print(LEVEL_COLORS[level] + logEntry + ANSI_RESET);
                }
                else
                {
                    System.out.print(logEntry);
                }

                cleanLogFile(LogFileConfig.getInstance(LogFileConfig.class).getLogMaxLines());

                threadMonitor.updateThreadStats("logging", 0, System.currentTimeMillis() - startTime);
            } catch (IOException ignored) { }
        });
    }

    private static void cleanLogFile(int maxLines)
    {
        try
        {
            if (maxLines <= 0) return;

            var lines = Files.readAllLines(logFile.toPath());
            if (lines.size() > maxLines + 5)
            {
                var newContent = lines.subList(0, 5);
                newContent.addAll(lines.subList(lines.size() - maxLines, lines.size()));
                Files.write(logFile.toPath(), newContent);
            }
        } catch (IOException ignored) { }
    }

    public static void info(String message)
    {
        write(INFO, message);
    }

    public static void warn(String message)
    {
        write(WARN, message);
    }

    public static void error(String message)
    {
        write(ERROR, message);
    }

    public static void debug(String message)
    {
        write(DEBUG, message);
    }

    private static String getCpuInfo()
    {
        try
        {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win"))
            {
                Process process = Runtime.getRuntime().exec("wmic cpu get name");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null)
                {
                    line = line.trim();

                    if (!line.isEmpty() && !line.equals("Name"))
                    {
                        return line;
                    }
                }
            }
            else if (os.contains("nix") || os.contains("nux") || os.contains("mac"))
            {
                Process process = Runtime.getRuntime().exec("cat /proc/cpuinfo | grep 'model name' | head -1");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = reader.readLine();

                if (line != null && line.contains(":"))
                {
                    return line.split(":")[1].trim();
                }
            }
        } catch (Exception ignored) { }

        return System.getProperty("os.arch") + " (" + Runtime.getRuntime().availableProcessors() + " cores)";
    }

    private static String getGpuInfo()
    {
        try
        {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win"))
            {
                Process process = Runtime.getRuntime().exec("wmic path win32_VideoController get name");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder gpuInfo = new StringBuilder();
                String line;
                boolean firstLine = true;

                while ((line = reader.readLine()) != null)
                {
                    line = line.trim();

                    if (!line.isEmpty() && !line.equals("Name"))
                    {
                        if (!firstLine)
                        {
                            gpuInfo.append(" | ");
                        }

                        gpuInfo.append(line);
                        firstLine = false;
                    }
                }

                if (gpuInfo.length() > 0)
                {
                    return gpuInfo.toString();
                }
            }
            else if (os.contains("nix") || os.contains("nux"))
            {
                Process process = Runtime.getRuntime().exec("lspci | grep VGA");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = reader.readLine();

                if (line != null && line.contains(":"))
                {
                    return line.substring(line.indexOf(":") + 1).trim();
                }
            }
            else if (os.contains("mac"))
            {
                Process process = Runtime.getRuntime().exec("system_profiler SPDisplaysDataType | grep Chipset");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = reader.readLine();

                if (line != null && line.contains(":"))
                {
                    return line.split(":")[1].trim();
                }
            }
        } catch (Exception ignored) { }

        return "Unknown GPU";
    }

    private static String getActiveGpuInfo()
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

            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win"))
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
            }
        } catch (Exception ignored) { }

        return "Unknown";
    }

    private static String getGpuDriverVersion()
    {
        try
        {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win"))
            {
                Process process = Runtime.getRuntime().exec("wmic path win32_VideoController get DriverVersion");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null)
                {
                    line = line.trim();

                    if (!line.isEmpty() && !line.equals("DriverVersion"))
                    {
                        return line;
                    }
                }
            }
            else if (os.contains("nix") || os.contains("nux"))
            {
                Process process = Runtime.getRuntime().exec("glxinfo | grep 'OpenGL version'");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = reader.readLine();

                if (line != null && line.contains(":"))
                {
                    return line.split(":")[1].trim();
                }
            }
        } catch (Exception ignored) { }

        return "Unknown";
    }

    private static String getSystemMemory()
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

    private static String getCpuCoreInfo()
    {
        try
        {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win"))
            {
                Process process = Runtime.getRuntime().exec(
                        "wmic cpu get NumberOfCores,NumberOfLogicalProcessors");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null)
                {
                    if (line.trim().matches("\\d+\\s+\\d+"))
                    {
                        String[] parts = line.trim().split("\\s+");
                        return parts[0] + " cores / " + parts[1] + " threads";
                    }
                }
            }
        } catch (Exception ignored) { }

        int threads = Runtime.getRuntime().availableProcessors();

        return threads + " threads";
    }

    private static String getVramInfo()
    {
        try
        {
            if (GLContext.getCapabilities() != null)
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

                if (GLContext.getCapabilities().GL_ATI_meminfo)
                {
                    int totalMemory = 0;
                    int freeMemory = 0;

                    try
                    {
                        totalMemory = GL11.glGetInteger(0x87FB);
                        freeMemory = GL11.glGetInteger(0x87FC);
                    } catch (Exception ignored) { }

                    if (totalMemory > 0)
                    {
                        long totalBytes = totalMemory * 1024L;
                        long freeBytes = freeMemory * 1024L;

                        return formatBytes(totalBytes) + " total / " + formatBytes(freeBytes) + " free (AMD/ATI)";
                    }
                }
            }

            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win"))
            {
                return getWindowsVramInfo();
            }
        }
        catch (Exception exception)
        {
            Log.debug("Error getting VRAM info: " + exception.getMessage());
        }

        return "Unknown";
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
                                } catch (NumberFormatException ignored) { }
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
        catch (Exception exception)
        {
            Log.debug("Error getting Windows VRAM: " + exception.getMessage());
        }

        return "Unknown";
    }

    private static String getJvmArgs()
    {
        return String.join(" ",
                ManagementFactory.getRuntimeMXBean().getInputArguments());
    }

    private static String formatBytes(long bytes)
    {
        if (bytes < 1024)
        {
            return bytes + " B";
        }

        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp-1) + "";

        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}