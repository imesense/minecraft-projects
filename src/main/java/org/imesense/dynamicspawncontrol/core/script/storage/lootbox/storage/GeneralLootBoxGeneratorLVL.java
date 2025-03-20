package org.imesense.dynamicspawncontrol.core.script.storage.lootbox.storage;

import net.minecraft.item.ItemStack;
import org.imesense.dynamicspawncontrol.core.script.storage.lootbox.data.LootBoxGeneratorLVL;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        this.lootBoxGeneratorLVLData = new HashMap<>();
    }

    public Map<String, LootBoxGeneratorLVL.Data> lootBoxGeneratorLVLData;
}
