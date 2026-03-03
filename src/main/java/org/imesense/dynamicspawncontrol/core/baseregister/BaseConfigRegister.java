package org.imesense.dynamicspawncontrol.core.baseregister;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.logfile.Logger;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.lang.reflect.Constructor;

@TODO(value = "Merge 'base' files into 'core/base/...' and fix the class diagram in version 0.2", showOnce = false, priority = TODO.TodoPriority.HIGH)
public abstract class BaseConfigRegister
{
    protected abstract Class<?>[] getConfigClasses();

    public BaseConfigRegister()
    {

    }

    public void initializeConfigs()
    {
        for (Class<?> configClass : getConfigClasses())
        {
            initializeConfig(configClass);
        }
    }

    protected <T> void initializeConfig(Class<T> _class)
    {
        try
        {
            if (_class.isAnnotationPresent(ConceptConfig.class))
            {
                if (_class.isAnnotationPresent(InitLog.class))
                {
                    CodeGeneric.logInitialization(_class);
                }

                ConceptConfig dcsSingleConfig =
                        _class.getAnnotation(ConceptConfig.class);

                String configFileName =
                        dcsSingleConfig.fileName() + DynamicSpawnControlStructure.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION;

                Constructor<T> constructor = _class.getConstructor(String.class);
                final T INSTANCE = constructor.newInstance(configFileName);

                Logger.write(0, "Initialized config: " + configFileName);
                Logger.write(0, "configClass: " + _class + " " + INSTANCE);
            }
            else
            {
                Logger.write(2, "No ConfigClass annotation found in: " + _class.getName());
            }
        }
        catch (NoSuchMethodException exception)
        {
            Logger.write(2, "Constructor with String parameter not found in class: " + _class.getName() + " - " + exception.getMessage());
        }
        catch (Exception exception)
        {
            Logger.write(2, "Exception in class: " + _class.getName() + " - " + exception.getMessage());
            throw new RuntimeException(exception);
        }
    }
}
