package org.imesense.dynamicspawncontrol.core.script.storage.lootbox.data;

import net.minecraft.item.ItemStack;

import java.util.List;

public final class LootBoxGeneratorLVL
{
    public static final class Data
    {
        public Double spawnChance;
        public Integer maxHeight;
        public Integer minHeight;
        public List<ItemStack> items;

        public Data(Double spawnChance, Integer maxHeight, Integer minHeight, List<ItemStack> items)
        {
            this.spawnChance = spawnChance;
            this.maxHeight = maxHeight;
            this.minHeight = minHeight;
            this.items = items;
        }
    }
}
