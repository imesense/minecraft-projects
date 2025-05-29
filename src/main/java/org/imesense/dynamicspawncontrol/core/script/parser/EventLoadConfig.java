package org.imesense.dynamicspawncontrol.core.script.parser;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;

// Runnable loadTask = () -> {
// };
// MinecraftForge.EVENT_BUS.post(new EventLoadConfig(loadTask));
@InitLog
@TODO(
        value = "Концепт пост.загрузки скриптовых конфигураций",
        priority = TODO.TodoPriority.HIGH,
        showOnce = false
)
public final class EventLoadConfig extends Event
{
    private final Runnable loadTask;
    private static boolean hasLoaded = false;
    private boolean isRegisteredForTickCheck = false;

    public EventLoadConfig(Runnable loadTask)
    {
        this.loadTask = loadTask;

        if (hasLoaded)
        {
            execute();
        }
        else
        {
            MinecraftForge.EVENT_BUS.register(this);
            isRegisteredForTickCheck = true;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END && isRegisteredForTickCheck)
        {
            if (isMainMenuReached())
            {
                hasLoaded = true;
                execute();
                unregister();
            }
        }
    }

    private boolean isMainMenuReached()
    {
        try
        {
            return net.minecraft.client.Minecraft.getMinecraft().currentScreen != null &&
                    net.minecraft.client.Minecraft.getMinecraft().world == null;
        }
        catch (Exception exception)
        {
            return false;
        }
    }

    private void unregister()
    {
        if (isRegisteredForTickCheck)
        {
            MinecraftForge.EVENT_BUS.unregister(this);
            isRegisteredForTickCheck = false;
        }
    }

    public void execute()
    {
        if (!hasLoaded)
        {
            hasLoaded = true;
        }

        loadTask.run();
    }

    public static void reset()
    {
        hasLoaded = false;
    }
}
