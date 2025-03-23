package org.imesense.dynamicspawncontrol.core.baseonevent;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseOnEventInstance
{
    private static final Map<Class<?>, Boolean> INSTANCE_EXITS_MAP = new HashMap<>();

    protected BaseOnEventInstance()
    {
        Class<?> _class = this.getClass();

        if (_class.isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(_class);
        }

        synchronized (INSTANCE_EXITS_MAP)
        {
            if (INSTANCE_EXITS_MAP.getOrDefault(_class, false))
            {
                Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", _class.getSimpleName()));
                throw new RuntimeException();
            }

            INSTANCE_EXITS_MAP.put(_class, true);
        }
    }
}
