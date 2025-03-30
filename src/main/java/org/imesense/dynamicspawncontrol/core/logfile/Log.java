package org.imesense.dynamicspawncontrol.core.logfile;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.config.logfile.LogFileConfig;
import org.imesense.dynamicspawncontrol.core.threads.ThreadMonitor;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public final class Log
{
    private static File logFile;

    private static final ThreadMonitor threadMonitor = ThreadMonitor.getInstance();

    private static final String[] LEVEL_PREFIXES = { "[INFO]: ", "[WARN]: ", "[ERROR]: ", "[INIT]: " };

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    public static void createLogFile(final String PATH, boolean isDebugMode)
    {
        try
        {
            File file = new File(PATH, DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_LOGS);
            file.mkdirs();

            logFile = isDebugMode ? new File(file, "log_debug.txt") :
                    new File(file, "log_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) +
                            DynamicSpawnControlStructure.STRUCT_FILES_EXTENSION.LOG_FILE_EXTENSION);

            FileWriter fileWriter = new FileWriter(logFile, !isDebugMode);
            fileWriter.write("*********************************************************************");
            fileWriter.write("\n** Log file created: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            fileWriter.write("\n** DynamicsSpawnControl. Authors: OldSerpskiStalker, acidicMercury8");
            fileWriter.write("\n*******************************************************************");
            fileWriter.close();

            threadMonitor.startLoggingThreadMonitoring();
        } catch (IOException ignored) {}
    }

    private static void cleanFile(File file, int maxLines)
    {
        try
        {
            List<String> lines = new ArrayList<>(Files.readAllLines(file.toPath()));

            if (lines.size() >= maxLines)
            {
                lines.subList(Math.max(0, 4), Math.min(lines.size(), 4 + maxLines)).clear();
                Files.write(file.toPath(), lines);
            }
        } catch (IOException ignored) {}
    }

    public static void writeDataToLogFile(final int LEVEL_INFO, String data)
    {
        if (logFile != null)
        {
            long startTime = System.currentTimeMillis();

            EXECUTOR.submit(() ->
            {
                try
                {
                    threadMonitor.updateThreadStats("logging", 0,
                            System.currentTimeMillis() - startTime);

                    int logLevel = (LEVEL_INFO < 0 || LEVEL_INFO >= LEVEL_PREFIXES.length) ? 0 : LEVEL_INFO;
                    Files.write(logFile.toPath(), ("\n" + LEVEL_PREFIXES[logLevel] + data).getBytes(), StandardOpenOption.APPEND);
                    cleanFile(logFile, LogFileConfig.getInstance(LogFileConfig.class).getLogMaxLines());
                } catch (IOException ignored) {}
            });
        }
    }
}