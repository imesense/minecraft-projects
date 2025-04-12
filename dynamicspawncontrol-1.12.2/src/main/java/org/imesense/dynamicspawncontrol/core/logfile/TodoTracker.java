package org.imesense.dynamicspawncontrol.core.logfile;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public final class TodoTracker
{
    private static boolean initialized = false;
    private static final Set<String> loggedTodos = new HashSet<>();

    public TodoTracker()
    {

    }

    public static void init(FMLPreInitializationEvent event)
    {
        if (initialized)
        {
            return;
        }

        initialized = true;

        event.getAsmData().getAll(TODO.class.getName()).forEach(data ->
        {
            String className = data.getClassName();
            TODO todo = getAnnotation(data);

            if (todo.showOnce() && loggedTodos.contains(className))
            {
                return;
            }

            String message = String.format("%s: %s",
                    className.substring(className.lastIndexOf('.') + 1),
                    todo.value());

            Log.writeDataToLogFile(4, message);

            loggedTodos.add(className);
        });
    }

    private static TODO getAnnotation(ASMDataTable.ASMData asmData)
    {
        try
        {
            Class<?> _class = Class.forName(asmData.getClassName());
            return _class.getAnnotation(TODO.class);
        }
        catch (Exception exception)
        {
            return new TODO()
            {
                public Class<? extends Annotation> annotationType()
                {
                    return TODO.class;
                }

                public String value()
                {
                    return "ERROR: Failed to read annotation";
                }

                public boolean showOnce()
                {
                    return false;
                }

                public TodoPriority priority()
                {
                    return TodoPriority.CRITICAL;
                }
            };
        }
    }

    private static int getLogLevel(TODO.TodoPriority priority)
    {
        switch (priority)
        {
            case CRITICAL: return 3;
            case HIGH: return 2;
            case NORMAL: return 1;
            default: return 0;
        }
    }
}
