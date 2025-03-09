package org.imesense.dynamicspawncontrol.core.baseregister;

import net.minecraftforge.common.MinecraftForge;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.register.event.block.EventBlockRegister;
import org.imesense.dynamicspawncontrol.core.register.event.entity.EventEntityRegister;
import org.imesense.dynamicspawncontrol.core.register.event.fmlnetwork.EventFMLRegister;
import org.imesense.dynamicspawncontrol.core.register.event.living.EventLivingRegister;
import org.imesense.dynamicspawncontrol.core.register.event.player.EventPlayerRegister;
import org.imesense.dynamicspawncontrol.core.register.event.populatechunk.EventPopulateChunk;
import org.imesense.dynamicspawncontrol.core.register.event.rendergame.EventRenderGame;
import org.imesense.dynamicspawncontrol.core.register.event.tickevent.EventTickEventRegister;
import org.imesense.dynamicspawncontrol.core.register.event.world.EventWorldRegister;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseEventRegister
{
    protected abstract Class<?>[] getEventClasses();

    private static final List<BaseEventRegister> REGISTERS = new ArrayList<>();

    public BaseEventRegister()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
        REGISTERS.add(this);
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

    public static void initialize()
    {
        Log.writeDataToLogFile(0, "Initializing all registers...");

        new EventBlockRegister();
        new EventEntityRegister();
        new EventFMLRegister();
        new EventLivingRegister();
        new EventPlayerRegister();
        new EventPopulateChunk();
        new EventRenderGame();
        new EventTickEventRegister();
        new EventWorldRegister();

        Log.writeDataToLogFile(0, "Total registers created: " + REGISTERS.size());

        for (BaseEventRegister register : REGISTERS)
        {
            try
            {
                register.registerClasses();
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(2, "Exception while registering events for: " + register.getClass().getSimpleName() + " - " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }

        Log.writeDataToLogFile(0, "All registers initialized and events registered.");
    }
}
