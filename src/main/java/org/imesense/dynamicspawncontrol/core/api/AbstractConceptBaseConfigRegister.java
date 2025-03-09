package org.imesense.dynamicspawncontrol.core.api;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.lang.reflect.Constructor;

public abstract class AbstractConceptBaseConfigRegister
{
    protected abstract Class<?>[] getConfigClasses();

    public AbstractConceptBaseConfigRegister()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    public void initializeConfigs()
    {
        for (Class<?> configClass : getConfigClasses())
        {
            initializeConfig(configClass);
        }
    }

    private <T> void initializeConfig(Class<T> _class)
    {
        try
        {
            if (_class.isAnnotationPresent(ConceptConfig.class))
            {
                ConceptConfig dcsSingleConfig = _class.getAnnotation(ConceptConfig.class);
                String configFileName = dcsSingleConfig.fileName() + DynamicSpawnControlStructure.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION;

                Constructor<T> constructor = _class.getConstructor(String.class);
                final T INSTANCE = constructor.newInstance(configFileName);

                Log.writeDataToLogFile(0, "Initialized config: " + configFileName);
                Log.writeDataToLogFile(0, "configClass: " + _class + " " + INSTANCE);
            }
            else
            {
                Log.writeDataToLogFile(2, "No ConfigClass annotation found in: " + _class.getName());
            }
        }
        catch (NoSuchMethodException exception)
        {
            Log.writeDataToLogFile(2, "Constructor with String parameter not found in class: " + _class.getName() + " - " + exception.getMessage());
        }
        catch (Exception exception)
        {
            Log.writeDataToLogFile(2, "Exception in class: " + _class.getName() + " - " + exception.getMessage());
            throw new RuntimeException(exception);
        }
    }
}
