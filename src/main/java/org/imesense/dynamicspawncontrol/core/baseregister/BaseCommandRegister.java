package org.imesense.dynamicspawncontrol.core.baseregister;

import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@TODO(value = "Merge 'base' files into 'core/base/...' and fix the class diagram in version 0.2", showOnce = false, priority = TODO.TodoPriority.HIGH)
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
                LogManager.error("Exception in class: " + _class.getName() + " - " + exception.getMessage());

                throw new RuntimeException(exception);
            }
        }
    }
}
