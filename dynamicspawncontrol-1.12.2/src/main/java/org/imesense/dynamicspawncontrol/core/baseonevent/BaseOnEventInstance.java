package org.imesense.dynamicspawncontrol.core.baseonevent;

import java.util.HashMap;
import java.util.Map;

import org.imesense.dynamicspawncontrol.core.logfile.Log;

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
                Log.writeDataToLogFile(2,
                        String.format("An instance of [%s] already exists!", _class.getSimpleName()));

                throw new RuntimeException();
            }

            INSTANCE_EXITS_MAP.put(_class, true);
        }
    }
}
