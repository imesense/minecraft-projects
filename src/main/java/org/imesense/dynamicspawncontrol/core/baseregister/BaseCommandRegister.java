package org.imesense.dynamicspawncontrol.core.baseregister;

import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

public abstract class BaseCommandRegister
{
    protected abstract Class<?>[] getCommandClasses();

    public BaseCommandRegister()
    {

    }

    public void registerCommands(FMLServerStartingEvent event)
    {
        for (Class<?> _class : getCommandClasses())
        {
            try
            {
                if (_class.isAnnotationPresent(InitLog.class))
                {
                    CodeGeneric.logInitialization(_class);
                }

                Object object =
                        _class.getConstructor().newInstance();

                event.registerServerCommand((ICommand) object);
            }
            catch (Exception exception)
            {
                Log.write(2, "Exception in class: "
                        + _class.getName() + " - " + exception.getMessage());

                throw new RuntimeException(exception);
            }
        }
    }
}
