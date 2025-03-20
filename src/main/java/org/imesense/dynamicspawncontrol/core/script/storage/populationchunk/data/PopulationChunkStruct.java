package org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.data;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

import java.util.List;

public final class PopulationChunkStruct
{
    public static final class Data
    {
        public ResourceLocation entity;
        public int weight;
        public int groupCountMin;
        public int groupCountMax;
        public List<String> biomes;
        public boolean isWater;
        public String spawnChancePriority;
        public int maxEntitiesPerChunk;
    }
}
