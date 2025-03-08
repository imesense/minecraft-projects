package org.imesense.dynamicspawncontrol.core.script.storage.dropitem.storage;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public class GeneralDropItem
{
    private static volatile GeneralDropItem _INSTANCE;

    public static GeneralDropItem getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (GeneralDropItem.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new GeneralDropItem();
                }
            }
        }
        return _INSTANCE;
    }

    public GeneralDropItem()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        this.dropItemList = new ArrayList<>();
    }

    public static final class Data
    {

    }

    public List<GeneralDropItem.Data> dropItemList;
}
