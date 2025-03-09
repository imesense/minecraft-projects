package org.imesense.dynamicspawncontrol.core.baseregister;

import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

public abstract class BaseCommandRegister
{
    protected abstract Class<?>[] getCommandClasses();

    public BaseCommandRegister()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    public void registerCommands(FMLServerStartingEvent fmlServerStartingEvent)
    {
        for (Class<?> cmdClass : getCommandClasses())
        {
            try
            {
                Object object =
                        cmdClass.getConstructor().newInstance();

                fmlServerStartingEvent.registerServerCommand((ICommand) object);
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(2, "Exception in class: " + cmdClass.getName() + " - " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
    }
}
