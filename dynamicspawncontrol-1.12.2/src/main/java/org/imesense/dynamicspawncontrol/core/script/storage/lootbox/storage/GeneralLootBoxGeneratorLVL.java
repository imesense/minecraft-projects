package org.imesense.dynamicspawncontrol.core.script.storage.lootbox.storage;

import net.minecraft.item.ItemStack;
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

    public static class LootBoxGeneratorLVLData
    {
        public final double spawnChance;
        public final int maxHeight;
        public final int minHeight;
        public final List<ItemStack> items;

        public LootBoxGeneratorLVLData(double spawnChance, int maxHeight, int minHeight, List<ItemStack> items)
        {
            this.spawnChance = spawnChance;
            this.maxHeight = maxHeight;
            this.minHeight = minHeight;
            this.items = items;
        }
    }

    public Map<String, LootBoxGeneratorLVLData> lootBoxGeneratorLVLData;
}
