package org.imesense.dynamicspawncontrol.technical.customlibrary;

import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.technical.config.ConfigLogFile;

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
public final class Log
{
    /**
     *
     */
    private static File logFile;

    /**
     *
     */
    private static final String[] LEVEL_PREFIXES = { "[INFO]: ", "[WARN]: ", "[ERROR]: " };

    /**
     *
     */
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    /**
     *
     * @param path
     */
    public static void createLogFile(final String path, boolean isDebugMode)
    {
        try
        {
            File logsDirectory = new File(path, DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIR_LOGS);

            if (!logsDirectory.exists())
            {
                if (logsDirectory.mkdirs())
                {
                    System.out.println("The 'logs' folder has been created successfully: " + logsDirectory.getAbsolutePath());
                }
                else
                {
                    System.err.println("The 'logs' folder could not be created.");
                    return;
                }
            }

            if (isDebugMode)
            {
                logFile = new File(logsDirectory, "log_debug.txt");
            }
            else
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                String currentDate = dateFormat.format(new Date());
                String fileName = logsDirectory + "/log_" + currentDate + DynamicSpawnControl.STRUCT_FILES_EXTENSION.LOG_FILE_EXTENSION;
                logFile = new File(fileName);
            }

            FileWriter writer = new FileWriter(logFile, !isDebugMode);

            writer.write("*********************************************************************");
            writer.write("\n** Log file created: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            writer.write("\n** DynamicsSpawnControl. Authors: OldSerpskiStalker, acidicMercury8");
            writer.write("\n*******************************************************************");

            writer.close();
        }
        catch (IOException exception)
        {
            System.err.println("Error creating the file: " + exception.getMessage());
        }
    }

    /**
     *
     * @param file
     * @param maxLines
     */
    private static void cleanFile(File file, int maxLines)
    {
        try
        {
            String line;
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(file));

            while ((line = reader.readLine()) != null)
            {
                lines.add(line);
            }

            reader.close();

            if (lines.size() >= maxLines)
            {
                final int START_LINE = 5;

                int startIndex = Math.max(0, START_LINE - 1);
                int endIndex = Math.min(lines.size(), startIndex + maxLines);

                lines.subList(startIndex, endIndex).clear();

                BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                for (int i = 0; i < lines.size(); i++)
                {
                    writer.write(lines.get(i));

                    if (i < lines.size() - 1)
                    {
                        writer.newLine();
                    }
                }

                writer.close();
            }
            else
            {
                System.out.println("No update needed. The file has not reached the maximum number of lines.");
            }
        }
        catch (IOException exception)
        {
            System.err.println("Error updating the file: " + exception.getMessage());
        }
    }

    /**
     *
     * @param levelInfo
     * @param data
     */
    public static void writeDataToLogFile(final int levelInfo, String data)
    {
        if (logFile != null)
        {
            final int[] logLevel = { levelInfo };

            EXECUTOR.submit(() ->
            {
                try
                {
                    if (logLevel[0] < 0 || logLevel[0] >= LEVEL_PREFIXES.length)
                    {
                        logLevel[0] = 0;
                    }

                    FileWriter writer = new FileWriter(logFile, true);

                    writer.write("\n" + LEVEL_PREFIXES[logLevel[0]] + data);
                    writer.close();

                    cleanFile(logFile, ConfigLogFile.LogMaxLines);
                }
                catch (IOException exception)
                {
                    System.err.println("Error writing data to a file: " + exception.getMessage());
                }
            });
        }
        else
        {
            System.err.println("The log file has not been created. First, create a log file.");
        }
    }
}
