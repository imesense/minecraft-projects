package org.imesense.dynamicspawncontrol.core.script.storage.dropitem.storage;

import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.data.DropItem;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public final class GeneralDropItem
{
    private static volatile GeneralDropItem _INSTANCE;

    public static GeneralDropItem getInstance()
    {
        return CodeGeneric.getInstance(GeneralDropItem.class);
    }

    public GeneralDropItem()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        this.dropItemList = new ArrayList<>();
    }

    public List<DropItem.Data> dropItemList;
}
