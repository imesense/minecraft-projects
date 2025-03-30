package org.imesense.dynamicspawncontrol.core.script.storage.lootbox.storage;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

public final class GeneralLootBox
{
    private static volatile GeneralLootBox _INSTANCE;

    public static GeneralLootBox getInstance()
    {
        return CodeGeneric.getInstance(GeneralLootBox.class);
    }
}
