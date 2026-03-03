package org.imesense.dynamicspawncontrol.core.logfile;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.config.logfile.LogFileConfig;
import org.imesense.dynamicspawncontrol.core.debug.*;
import org.imesense.dynamicspawncontrol.core.taskmanager.TaskManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

public final class Logger
{
    private static File logFile;
    private static TaskManager taskManager;

    public static final int INFO = 0;
    public static final int WARN = 1;
    public static final int ERROR = 2;
    public static final int DEBUG = 3;

    private static volatile boolean shuttingDown = false;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";

    private static final String[] LEVEL_NAMES = { "INFO", "WARN", "ERROR", "DEBUG" };
    private static final String[] LEVEL_COLORS = { ANSI_RESET, ANSI_YELLOW, ANSI_RED, ANSI_CYAN };

    static
    {
        taskManager = TaskManager.getInstance();

        taskManager.addErrorHandler(new Consumer<Throwable>()
        {
            @Override
            public void accept(Throwable throwable)
            {
                System.err.println("[Logger Error] " + throwable.getMessage());
            }
        });
    }

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
                    "** JVM Args: " + JavaInfo.getJvmArgs(),
                    "** System Information:",
                    "**   OS: " + OperatingSystemInfo.getFullOsInfo(),
                    "**   Java: " + JavaInfo.getFullJavaInfo(),
                    "**   CPU: " + CpuInfo.getCpuModel(),
                    "**   CPU Cores/Threads: " + CpuInfo.getCpuCoreInfo(),
                    "**   GPU (All): " + GpuInfo.getAllGpusInfo(),
                    "**   GPU (Active): " + GpuInfo.getActiveGpuInfo(),
                    "**   GPU Driver: " + GpuInfo.getGpuDriverVersion(),
                    "**   GPU VRAM: " + GpuInfo.getVramInfo(),
                    "**   Game Allocated Memory: " + FormatUtils.formatBytes(Runtime.getRuntime().totalMemory()),
                    "**   Game Max Memory (-Xmx): " + FormatUtils.formatBytes(Runtime.getRuntime().maxMemory()),
                    "**   System RAM: " + OperatingSystemInfo.getSystemMemoryInfo(),
                    "*********************************************************************",
                    ""
            );

            Files.write(logFile.toPath(), header.getBytes(), StandardOpenOption.CREATE);

            LogIsReady.markReady();
            EarlyLogBuffer.flush();
        }
        catch (IOException ignored) { }
    }

    private static void write(final int level, final String message)
    {
        if (logFile == null || shuttingDown) return;

        try
        {
            taskManager.submitLogTask(new Runnable()
            {
                @Override
                public void run()
                {
                    if (shuttingDown) return;

                    try
                    {
                        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
                        String logLevel = level >= 0 && level < LEVEL_NAMES.length ? LEVEL_NAMES[level] : "UNKNOWN";

                        String logEntry = String.format("[%s] [%s] %s\n", timestamp, logLevel, message);

                        Files.write(logFile.toPath(), logEntry.getBytes(), StandardOpenOption.APPEND);

                        synchronized (System.out)
                        {
                            if (level >= 0 && level < LEVEL_COLORS.length)
                            {
                                System.out.print(LEVEL_COLORS[level] + logEntry + ANSI_RESET);
                            }
                            else
                            {
                                System.out.print(logEntry);
                            }
                        }

                        cleanLogFile(LogFileConfig.getInstance(LogFileConfig.class).getLogMaxLines());

                    } catch (IOException ignored) { }
                }
            });
        }
        catch (Exception exception)
        {
            if (!taskManager.isShuttingDown())
            {
                System.err.println("[Logger] Failed to submit log task: " + exception.getMessage());
            }
        }
    }

    private static void cleanLogFile(int maxLines)
    {
        try
        {
            if (maxLines <= 0 || logFile == null) return;

            java.util.List<String> lines = Files.readAllLines(logFile.toPath());

            if (lines.size() > maxLines + 5)
            {
                java.util.List<String> newContent = new java.util.ArrayList<>(lines.subList(0, 5));
                newContent.addAll(lines.subList(lines.size() - maxLines, lines.size()));
                Files.write(logFile.toPath(), newContent);
            }
        }
        catch (IOException ignored) { }
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

    public static void debugByteCode(int level, String message)
    {
        write(level, message);
    }

    public static void shutdown()
    {
        if (shuttingDown) return;
        shuttingDown = true;

        System.out.println("[Logger] Starting shutdown...");

        if (logFile != null)
        {
            try
            {
                String footer = String.join("\n",
                        "*********************************************************************",
                        "** Log file closed: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                        "** Shutdown initiated",
                        "*********************************************************************"
                );

                Files.write(logFile.toPath(), footer.getBytes(), StandardOpenOption.APPEND);
            }
            catch (IOException exception)
            {
                System.err.println("[Logger] Failed to write footer: " + exception.getMessage());
            }
        }

        taskManager.shutdown();

        System.out.println("[Logger] Shutdown complete");
    }
}