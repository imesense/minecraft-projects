package org.imesense.dynamicspawncontrol.core.collection;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@InitLog
@TODO(
        value = "Требуется рефакторинг",
        priority = TODO.TodoPriority.HIGH,
        showOnce = false
)
public final class CmdCallTypeCollection
{
    public static CmdCallTypeCollection instance;

    private static final List<String> CMD_CALL_TYPES;

    public CmdCallTypeCollection()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    static
    {
        List<String> types = new ArrayList<>();

        types.add("[Hit]");
        types.add("[Command]");
        types.add("[Timer]");
        types.add("[Signal]");
        types.add("[Message]");
        types.add("[Item drop]");

        CMD_CALL_TYPES = Collections.unmodifiableList(types);
    }

    public String getDescription(int index)
    {
        if (index < 0 || index >= CMD_CALL_TYPES.size())
        {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }

        return CMD_CALL_TYPES.get(index);
    }
}
