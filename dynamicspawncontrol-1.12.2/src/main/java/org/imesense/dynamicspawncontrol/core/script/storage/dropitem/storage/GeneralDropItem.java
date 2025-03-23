package org.imesense.dynamicspawncontrol.core.script.storage.dropitem.storage;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.data.DropItem;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

@InitLog
public final class GeneralDropItem
{
    private static volatile GeneralDropItem _INSTANCE;

    public static GeneralDropItem getInstance()
    {
        return CodeGeneric.getInstance(GeneralDropItem.class);
    }

    public GeneralDropItem()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }

        this.dropItemList = new ArrayList<>();
    }

    public List<DropItem.Data> dropItemList;
}
