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
}