package org.imesense.dynamicspawncontrol.core.base;

import java.util.HashMap;
import java.util.Map;

import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;

@TODO(value = "Merge 'base' files into 'core/base/...' and fix the class diagram in version 0.2", showOnce = false, priority = TODO.TodoPriority.HIGH)
public abstract class BaseOnEventInstance
{
    private static final Map<Class<?>, Boolean> INSTANCE_EXITS_MAP = new HashMap<>();

    protected BaseOnEventInstance()
    {
        Class<?> _class = this.getClass();

        synchronized (INSTANCE_EXITS_MAP)
        {
            if (INSTANCE_EXITS_MAP.getOrDefault(_class, false))
            {
                LogManager.error(String.format("An instance of [%s] already exists!", _class.getSimpleName()));

                throw new RuntimeException();
            }

            INSTANCE_EXITS_MAP.put(_class, true);
        }
    }
}
