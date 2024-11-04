package org.imesense.dynamicspawncontrol.core;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.config.data.LogFileData;

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
public final class LogFile
{
    /**
     *
     */
    private static File logFile;

    /**
     *
     */
    private static final String[] LEVEL_PREFIXES = { "[INFO]: ", "[WARN]: ", "[ERROR]: ", "[INIT]: " };

    /**
     *
     */
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    /**
     *
     */
    public LogFile()
    {

    }

    /**
     *
     * @param PATH
     * @param isDebugMode
     */
    public static void createLogFile(final String PATH, boolean isDebugMode)
    {
        try
        {
            File file = new File(PATH, DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_LOGS);

            if (!file.exists())
            {
                if (file.mkdirs())
                {
                    System.out.println("The 'logs' folder has been created successfully: " + file.getAbsolutePath());
                }
                else
                {
                    System.err.println("The 'logs' folder could not be created.");
                    return;
                }
            }

            if (isDebugMode)
            {
                logFile = new File(file, "log_debug.txt");
            }
            else
            {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

                String currentDate = simpleDateFormat.format(new Date());
                String fileName = file + "/log_" + currentDate + DynamicSpawnControlStructure.STRUCT_FILES_EXTENSION.LOG_FILE_EXTENSION;

                logFile = new File(fileName);
            }

            FileWriter fileWriter = new FileWriter(logFile, !isDebugMode);

            fileWriter.write("*********************************************************************");
            fileWriter.write("\n** Log file created: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            fileWriter.write("\n** DynamicsSpawnControl. Authors: OldSerpskiStalker, acidicMercury8");
            fileWriter.write("\n*******************************************************************");

            fileWriter.close();
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
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            while ((line = bufferedReader.readLine()) != null)
            {
                lines.add(line);
            }

            bufferedReader.close();

            if (lines.size() >= maxLines)
            {
                final int START_LINE = 5;

                int startIndex = Math.max(0, START_LINE - 1);
                int endIndex = Math.min(lines.size(), startIndex + maxLines);

                lines.subList(startIndex, endIndex).clear();

                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

                for (int i = 0; i < lines.size(); i++)
                {
                    bufferedWriter.write(lines.get(i));

                    if (i < lines.size() - 1)
                    {
                        bufferedWriter.newLine();
                    }
                }

                bufferedWriter.close();
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
     * @param LEVEL_INFO
     * @param data
     */
    public static void writeDataToLogFile(final int LEVEL_INFO, String data)
    {
        if (logFile != null)
        {
            final int[] LOG_LEVEL = { LEVEL_INFO };

            EXECUTOR.submit(() ->
            {
                try
                {
                    if (LOG_LEVEL[0] < 0 || LOG_LEVEL[0] >= LEVEL_PREFIXES.length)
                    {
                        LOG_LEVEL[0] = 0;
                    }

                    FileWriter fileWriter = new FileWriter(logFile, true);

                    fileWriter.write("\n" + LEVEL_PREFIXES[LOG_LEVEL[0]] + data);
                    fileWriter.close();

                    cleanFile(logFile, LogFileData.ConfigDataLogFile.Instance.getLogMaxLines());
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
