package org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.data;

import net.minecraft.util.ResourceLocation;

public final class PopulationChunkStruct
{
    public static final class Data
    {
        public ResourceLocation entity;
        public int weight;
        public int groupCountMin;
        public int groupCountMax;
        public String spawnChancePriority;
        public int maxEntitiesPerChunk;
    }
}
