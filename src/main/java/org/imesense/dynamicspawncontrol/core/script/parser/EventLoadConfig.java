package org.imesense.dynamicspawncontrol.core.script.parser;

import net.minecraftforge.fml.common.eventhandler.Event;

// Runnable loadTask = () -> {
// };
// MinecraftForge.EVENT_BUS.post(new EventLoadConfig(loadTask));
// Концепт пост.загрузки конфигов
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
