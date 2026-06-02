package org.imesense.dynamicspawncontrol.core.script.storage.lootbox.storage;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.data.LootBox;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GeneralLootBox
{
    private static volatile GeneralLootBox _INSTANCE;

    public static GeneralLootBox getInstance()
    {
        return CodeGeneric.getInstance(GeneralLootBox.class);
    }

    public GeneralLootBox()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }

        this.lootTable = new HashMap<>();
    }

    public Map<String, List<LootBox.Data>> lootTable;
}
