package org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.data;

import net.minecraft.util.ResourceLocation;

import java.util.List;

public final class PopulationChunkStruct
{
    public PopulationChunkStruct()
    {

    }

    public static final class Data
    {
        public ResourceLocation entity;
        public Integer weight;
        public Integer groupCountMin;
        public Integer groupCountMax;
        public List<String> biomes;
        public Boolean isWater;
        public String spawnChancePriority;
        public Integer maxEntitiesPerChunk;
    }
}
