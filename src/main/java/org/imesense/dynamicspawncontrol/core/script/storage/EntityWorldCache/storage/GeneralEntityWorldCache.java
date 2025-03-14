package org.imesense.dynamicspawncontrol.core.script.storage.EntityWorldCache.storage;

import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.data.DropItem;
import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.storage.GeneralDropItem;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public final class GeneralEntityWorldCache
{
    private static volatile GeneralEntityWorldCache _INSTANCE;

    public static GeneralEntityWorldCache getInstance()
    {
        return CodeGeneric.getInstance(GeneralEntityWorldCache.class);
    }

    public GeneralEntityWorldCache()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        //this.dropItemList = new ArrayList<>();
    }

    // Лист типа хеш мапы - сущность, ее максимальное количество
    //public List<DropItem.Data> dropItemList;
}
