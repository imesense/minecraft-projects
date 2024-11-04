package org.imesense.dynamicspawncontrol.core.register;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.config.file.LogFileConfig;
import org.imesense.dynamicspawncontrol.core.LogFile;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.lang.reflect.Constructor;

/**
 *
 */
public final class RegisterConfigClass
{
    /**
     *
     */
    private static final Class<?>[] CONFIG_CLASSES =
    {
        LogFileConfig.class
    };

    /**
     *
     */
    public RegisterConfigClass()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    /**
     *
     */
    public static void initializeConfigs()
    {
        for (Class<?> configClass : CONFIG_CLASSES)
        {
            initializeConfig(configClass);
        }
    }

    /**
     *
     * @param _class
     * @param <T>
     */
    private static <T> void initializeConfig(Class<T> _class)
    {
        try
        {
            if (_class.isAnnotationPresent(ConceptConfig.class))
            {
                ConceptConfig dcsSingleConfig = _class.getAnnotation(ConceptConfig.class);
                String configFileName = dcsSingleConfig.fileName() + DynamicSpawnControlStructure.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION;

                Constructor<T> constructor = _class.getConstructor(String.class);
                final T INSTANCE = constructor.newInstance(configFileName);

                LogFile.writeDataToLogFile(0, "Initialized config: " + configFileName);
                LogFile.writeDataToLogFile(0, "configClass: " + _class + " " + INSTANCE);
            }
            else
            {
                LogFile.writeDataToLogFile(2, "No ConfigClass annotation found in: " + _class.getName());
            }
        }
        catch (NoSuchMethodException exception)
        {
            LogFile.writeDataToLogFile(2, "Constructor with String parameter not found in class: " + _class.getName() + " - " + exception.getMessage());
        }
        catch (Exception exception)
        {
            LogFile.writeDataToLogFile(2, "Exception in class: " + _class.getName() + " - " + exception.getMessage());
            throw new RuntimeException(exception);
        }
    }
}
