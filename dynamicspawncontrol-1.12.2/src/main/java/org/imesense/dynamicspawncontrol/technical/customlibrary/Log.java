package org.imesense.dynamicspawncontrol.technical.customlibrary;

import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.technical.configs.ConfigLogFile;

import javax.annotation.Nonnull;
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
    public static final String[] TypeLog = { "[INFO]: ", "[WARN]: ", "[ERROR]: " };

    /**
     *
     */
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     *
     * @param path
     */
    public static void createLogFile(String path)
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

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String currentDate = dateFormat.format(new Date());

            String fileName = logsDirectory + "/log_" + currentDate + DynamicSpawnControl.STRUCT_FILES_EXTENSION.LOG_FILE_EXTENSION;
            logFile = new File(fileName);

            FileWriter writer = new FileWriter(logFile);

            writer.write("***********************************************");
            writer.write("\n** Log file created: " + currentDate);
            writer.write("\n** DynamicsSpawnControl. Authors: OldSerpskiStalker, acidicMercury8");
            writer.write("\n***********************************************");

            writer.close();

            System.out.println("The file was successfully created: " + logFile.getAbsolutePath());
        }
        catch (IOException e)
        {
            System.err.println("Error creating the file: " + e.getMessage());
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

                System.out.println("The file was successfully updated.");
            }
            else
            {
                System.out.println("No update needed. The file has not reached the maximum number of lines.");
            }
        }
        catch (IOException e)
        {
            System.err.println("Error updating the file: " + e.getMessage());
        }
    }

    /**
     *
     * @param typeInfo
     * @param data
     */
    public static void writeDataToLogFile(@Nonnull String typeInfo, String data)
    {
        if (logFile != null)
        {
            executor.submit(() ->
            {
                try
                {
                    FileWriter writer = new FileWriter(logFile, true);

                    writer.write("\n" + typeInfo + data);
                    writer.close();

                    cleanFile(logFile, ConfigLogFile.LogMaxLines);
                    System.out.println("The data has been successfully written to the log file: " + logFile.getAbsolutePath());
                }
                catch (IOException e)
                {
                    System.err.println("Error writing data to a file: " + e.getMessage());
                }
            });
        }
        else
        {
            System.err.println("The log file has not been created. First, create a log file.");
        }
    }

    /**
     *
     */
    public static void closeExecutor()
    {
        executor.shutdown();
    }
}
