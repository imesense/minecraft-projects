package org.imesense.dynamicspawncontrol.core.mixinconfig;

import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.core.mixinconfig.annotations.MixinConfigFile;
import org.imesense.dynamicspawncontrol.core.mixinconfig.basemixinconfig.BaseMixinConfig;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class MixinConfigInitializer
{
    private static final Map<String, BaseMixinConfig> CONFIG_INSTANCES = new ConcurrentHashMap<>();
    private static final Map<String, MixinConfigFile> CONFIG_ANNOTATIONS = new ConcurrentHashMap<>();

    public static void registerConfig(Class<? extends BaseMixinConfig> configClass)
    {
        try
        {
            if (!configClass.isAnnotationPresent(MixinConfigFile.class))
            {
                EarlyLogBuffer.log(LogManager.ERROR, "Config class " + configClass.getSimpleName() +
                        " is missing @MixinConfigFile annotation!");
                return;
            }

            MixinConfigFile annotation = configClass.getAnnotation(MixinConfigFile.class);
            String fileName = annotation.value();

            Constructor<? extends BaseMixinConfig> constructor = configClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            BaseMixinConfig instance = constructor.newInstance();

            CONFIG_INSTANCES.put(fileName, instance);
            CONFIG_ANNOTATIONS.put(fileName, annotation);

            EarlyLogBuffer.log(LogManager.DEBUG, "Registered mixin config: " + fileName +
                    " (" + configClass.getSimpleName() + ")");
        }
        catch (Exception exception)
        {
            EarlyLogBuffer.log(LogManager.ERROR, "Failed to register config " + configClass.getSimpleName() +
                    ": " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    public static void initializeAllConfigs(String basePath)
    {
        EarlyLogBuffer.log(LogManager.INFO, "Initializing " + CONFIG_INSTANCES.size() + " mixin configs...");

        for (Map.Entry<String, BaseMixinConfig> entry : CONFIG_INSTANCES.entrySet())
        {
            String fileName = entry.getKey();
            BaseMixinConfig config = entry.getValue();
            MixinConfigFile annotation = CONFIG_ANNOTATIONS.get(fileName);

            try
            {
                if (annotation.createIfAbsent())
                {
                    config.createFile(basePath);
                    EarlyLogBuffer.log(LogManager.INFO, "Initialized config: " + fileName);
                }
                else
                {
                    EarlyLogBuffer.log(LogManager.DEBUG, "Skipping config creation: " + fileName);
                }
            }
            catch (Exception exception)
            {
                EarlyLogBuffer.log(LogManager.ERROR, "Failed to initialize config " + fileName +
                        ": " + exception.getMessage());
                exception.printStackTrace();
            }
        }

        EarlyLogBuffer.log(LogManager.INFO, "Mixin config initialization complete!");
    }

    public static void reloadAllConfigs()
    {
        EarlyLogBuffer.log(LogManager.INFO, "Reloading all mixin configs...");

        for (BaseMixinConfig config : CONFIG_INSTANCES.values())
        {
            try
            {
                config.OverwritingArray();
            }
            catch (Exception exception)
            {
                EarlyLogBuffer.log(LogManager.ERROR, "Failed to reload config: " + exception.getMessage());
                exception.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseMixinConfig> T getConfig(String fileName)
    {
        return (T) CONFIG_INSTANCES.get(fileName);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseMixinConfig> T getConfig(Class<T> configClass)
    {
        if (!configClass.isAnnotationPresent(MixinConfigFile.class))
        {
            return null;
        }

        MixinConfigFile annotation = configClass.getAnnotation(MixinConfigFile.class);
        return (T) CONFIG_INSTANCES.get(annotation.value());
    }

    public static Collection<BaseMixinConfig> getAllConfigs()
    {
        return CONFIG_INSTANCES.values();
    }
}