package org.imesense.dynamicspawncontrol.core.script.parser;

import net.minecraftforge.fml.common.eventhandler.Event;
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
public class EventLoadConfig extends Event
{
    private final Runnable loadTask;

    public EventLoadConfig(Runnable loadTask)
    {
        this.loadTask = loadTask;
    }

    public void execute()
    {
        loadTask.run();
    }
}
