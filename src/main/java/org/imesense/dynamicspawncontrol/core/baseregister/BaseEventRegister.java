package org.imesense.dynamicspawncontrol.core.baseregister;

import net.minecraftforge.common.MinecraftForge;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.register.attach.AttachRegister;
import org.imesense.dynamicspawncontrol.core.register.commandevent.CommandEventRegister;
import org.imesense.dynamicspawncontrol.core.register.event.block.EventBlockRegister;
import org.imesense.dynamicspawncontrol.core.register.event.entity.EventEntityRegister;
import org.imesense.dynamicspawncontrol.core.register.event.fmlnetwork.EventFMLRegister;
import org.imesense.dynamicspawncontrol.core.register.event.living.EventLivingRegister;
import org.imesense.dynamicspawncontrol.core.register.event.player.EventPlayerRegister;
import org.imesense.dynamicspawncontrol.core.register.event.populatechunk.EventPopulateChunkRegister;
import org.imesense.dynamicspawncontrol.core.register.event.rendergame.EventRenderGameRegister;
import org.imesense.dynamicspawncontrol.core.register.event.tickevent.EventTickEventRegister;
import org.imesense.dynamicspawncontrol.core.register.event.world.EventWorldRegister;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseEventRegister
{
    protected abstract Class<?>[] getEventClasses();

    protected static final List<BaseEventRegister> REGISTERS = new ArrayList<>();

    public BaseEventRegister()
    {
        REGISTERS.add(this);
    }

    public void registerClasses()
    {
        for (Class<?> _class : getEventClasses())
        {
            try
            {
                if (_class.isAnnotationPresent(InitLog.class))
                {
                    CodeGeneric.logInitialization(_class);
                }

                Object object = _class.getConstructor().newInstance();
                MinecraftForge.EVENT_BUS.register(object);
            }
            catch (Exception exception)
            {
                Log.write(2, "Exception in class: " + _class.getName() + " - " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
    }

    public static void initialize()
    {
        Log.write(0, "Initializing all registers...");

        new EventBlockRegister();
        new EventEntityRegister();
        new EventFMLRegister();
        new EventLivingRegister();
        new EventPlayerRegister();
        new EventPopulateChunkRegister();
        new EventRenderGameRegister();
        new EventTickEventRegister();
        new EventWorldRegister();
        new AttachRegister();
        new CommandEventRegister();

        Log.write(0, "Total registers created: " + REGISTERS.size());

        for (BaseEventRegister baseEventRegister : REGISTERS)
        {
            try
            {
                baseEventRegister.registerClasses();
            }
            catch (Exception exception)
            {
                Log.write(2, "Exception while registering events for: " +
                        baseEventRegister.getClass().getSimpleName() + " - " + exception.getMessage());

                throw new RuntimeException(exception);
            }
        }

        Log.write(0, "All registers initialized and events registered.");
    }
}
