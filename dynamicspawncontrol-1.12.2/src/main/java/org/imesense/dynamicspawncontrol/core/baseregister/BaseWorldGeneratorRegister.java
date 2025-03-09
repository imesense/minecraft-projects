package org.imesense.dynamicspawncontrol.core.baseregister;

import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

public abstract class BaseWorldGeneratorRegister
{
    protected abstract Class<?>[] getWorldGeneratorClasses();

    public BaseWorldGeneratorRegister()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    public void init(FMLPreInitializationEvent fmlPreInitializationEvent)
    {
        for (Class<?> _class : getWorldGeneratorClasses())
        {
            try
            {
                if (!CodeGeneric.hasDefaultConstructor(_class))
                {
                    Log.writeDataToLogFile(2, "Class " + _class.getName() + " does not have a default constructor.");
                    throw new RuntimeException("Default constructor not found in class: " + _class.getName());
                }

                Object object =
                        _class.getConstructor().newInstance();

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
