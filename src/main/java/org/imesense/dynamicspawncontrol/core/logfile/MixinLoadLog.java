package org.imesense.dynamicspawncontrol.core.logfile;

import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlInitMixins;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class MixinLoadLog
{
    public static void loadMixins()
    {
        ClassLoader classLoader = DynamicSpawnControl.class.getClassLoader();

        LogManager.info("Searching for mixin config files...");

        for (String config : DynamicSpawnControlInitMixins.getAllConfigPaths())
        {
            LogManager.info("Trying to load: " + config);

            try (InputStream inputStream = classLoader.getResourceAsStream(config))
            {
                if (inputStream == null)
                {
                    LogManager.error("Mixin not found in resources: " + config);
                    continue;
                }

                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)))
                {
                    String line;
                    StringBuilder stringBuilder = new StringBuilder();
                    boolean isFirstLine = true;

                    while ((line = bufferedReader.readLine()) != null)
                    {
                        if (!isFirstLine)
                        {
                            stringBuilder.append('\n');
                        }
                        else
                        {
                            isFirstLine = false;
                        }

                        stringBuilder.append(line);
                    }

                    LogManager.info("Successfully loaded mixin: " + config);
                    LogManager.info(stringBuilder.toString());
                }
            }
            catch (Exception exception)
            {
                LogManager.error("Error loading " + config + ": " + exception.getMessage());
                exception.printStackTrace();
            }
        }
    }
}
