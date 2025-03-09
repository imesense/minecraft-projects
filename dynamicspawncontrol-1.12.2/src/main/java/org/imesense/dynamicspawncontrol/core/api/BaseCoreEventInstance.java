package org.imesense.dynamicspawncontrol.core.api;

import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseCoreEventInstance
{
    private static final Map<Class<?>, Boolean> INSTANCE_EXITS_MAP = new HashMap<>();

    protected BaseCoreEventInstance()
    {
        Class<?> clazz = this.getClass();
        CodeGeneric.printInitClassToLog(clazz);

        synchronized (INSTANCE_EXITS_MAP)
        {
            if (INSTANCE_EXITS_MAP.getOrDefault(clazz, false))
            {
                Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", clazz.getSimpleName()));
                throw new RuntimeException();
            }

            INSTANCE_EXITS_MAP.put(clazz, true);
        }
    }
}
