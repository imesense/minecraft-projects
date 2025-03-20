package org.imesense.dynamicspawncontrol.core.script.storage.lootbox.storage;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public class GeneralLootBoxGeneratorLVL
{
    private static volatile GeneralLootBoxGeneratorLVL _INSTANCE;

    public static GeneralLootBoxGeneratorLVL getInstance()
    {
        return CodeGeneric.getInstance(GeneralLootBoxGeneratorLVL.class);
    }

    public GeneralLootBoxGeneratorLVL()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        this.lootBoxGeneratorLVLData = new ArrayList<>();
    }

    public static class LootBoxGeneratorLVLData
    {

    }

    public List<LootBoxGeneratorLVLData> lootBoxGeneratorLVLData;
}
