package org.imesense.dynamicspawncontrol.core.script.storage.lootbox.data;

import lombok.Getter;

public final class LootBox
{
    public LootBox()
    {

    }

    public final static class Data
    {
        @Getter
        private String item;

        @Getter
        private int minCount = 1;

        @Getter
        private int maxCount = 1;

        @Getter
        private float chance = 1.0f;
    }
}
