package org.imesense.dynamicspawncontrol.core.base;

import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.lang.reflect.Constructor;

@TODO(value = "Merge 'base' files into 'core/base/...' and fix the class diagram in version 0.2", showOnce = false, priority = TODO.TodoPriority.HIGH)
public abstract class BaseWorldGeneratorRegister
{
    protected abstract Class<?>[] getWorldGeneratorClasses();

    public BaseWorldGeneratorRegister()
    {

    }

    public void init(FMLPreInitializationEvent event)
    {
        for (Class<?> _class : getWorldGeneratorClasses())
        {
            try
            {
                if (!CodeGeneric.hasDefaultConstructor(_class))
                {
                    LogManager.error("Class " + _class.getName() + " does not have a default constructor.");
                    throw new RuntimeException("Default constructor not found in class: " + _class.getName());
                }

                Constructor<?> constructor =
                        _class.getConstructor();

                Object object = constructor.newInstance();

                GameRegistry.registerWorldGenerator((IWorldGenerator) object, 3);
            }
            catch (Exception exception)
            {
                LogManager.error("Exception in class: " + _class.getName() + " - " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
    }
}
