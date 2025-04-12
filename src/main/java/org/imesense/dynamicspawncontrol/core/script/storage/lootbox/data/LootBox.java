package org.imesense.dynamicspawncontrol.core.script.storage.lootbox.data;

public final class LootBox
{
    public LootBox()
    {

    }

    public final static class Data
    {
        private String item;
        private int minCount = 1;
        private int maxCount = 1;
        private float chance = 1.0f;

        public String getItem() { return item; }
        public int getMinCount() { return minCount; }
        public int getMaxCount() { return maxCount; }
        public float getChance() { return chance; }
    }
}
