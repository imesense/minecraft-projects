package org.imesense.dynamicspawncontrol.core.baseregister;

import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.lang.reflect.Constructor;

public abstract class BaseWorldGeneratorRegister
{
    protected abstract Class<?>[] getWorldGeneratorClasses();

    public BaseWorldGeneratorRegister()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    protected void logInitialization(final Class<?> _CLASS)
    {
        Log.writeDataToLogFile(3, String.format("Initializing a class: {%s}", _CLASS.getName()));
    }

    public void init(FMLPreInitializationEvent fmlPreInitializationEvent)
    {
        for (Class<?> _class : getWorldGeneratorClasses())
        {
            try
            {
                if (_class.isAnnotationPresent(InitLog.class))
                {
                    this.logInitialization(_class);
                }

                if (!CodeGeneric.hasDefaultConstructor(_class))
                {
                    Log.writeDataToLogFile(2, "Class " + _class.getName() + " does not have a default constructor.");
                    throw new RuntimeException("Default constructor not found in class: " + _class.getName());
                }

                Constructor<?> constructor =
                        _class.getConstructor();

                Object object = constructor.newInstance();

                GameRegistry.registerWorldGenerator((IWorldGenerator) object, 3);
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(2, "Exception in class: " + _class.getName() + " - " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
    }
}
