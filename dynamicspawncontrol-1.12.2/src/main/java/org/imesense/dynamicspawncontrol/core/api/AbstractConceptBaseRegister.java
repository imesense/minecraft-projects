package org.imesense.dynamicspawncontrol.core.api;

import net.minecraftforge.common.MinecraftForge;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

public abstract class AbstractConceptBaseRegister
{
    protected abstract Class<?>[] getEventClasses();

    public AbstractConceptBaseRegister()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    public void registerClasses()
    {
        for (Class<?> _class : getEventClasses())
        {
            try
            {
                Object object = _class.getConstructor().newInstance();
                MinecraftForge.EVENT_BUS.register(object);
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(2, "Exception in class: " + _class.getName() + " - " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
    }
}
