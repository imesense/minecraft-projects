package org.imesense.dynamicspawncontrol.core.structurecache;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureData;
import org.imesense.dynamicspawncontrol.statistics.DSCInlineDebugStats;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class Structure
{
    private static final Structure INSTANCE = new Structure();
    private final ConcurrentMap<StructureEntry, Boolean> structureCache = new ConcurrentHashMap<>();
    private final AtomicInteger cacheHits = new AtomicInteger();
    private final AtomicInteger cacheMisses = new AtomicInteger();
    private final AtomicLong lastCleanTime = new AtomicLong(System.currentTimeMillis());

    public static Structure getInstance()
    {
        return INSTANCE;
    }

    public void clean()
    {
        structureCache.clear();
        cacheHits.set(0);
        cacheMisses.set(0);
        lastCleanTime.set(System.currentTimeMillis());
    }

    public boolean isInStructure(World world, String structure, BlockPos blockPos)
    {
        int dimension = world.provider.getDimension();
        ChunkPos chunkPos = new ChunkPos(blockPos);
        long longChunkPos = ChunkPos.asLong(chunkPos.x, chunkPos.z);
        StructureEntry entry = new StructureEntry(structure, dimension, longChunkPos);

        Boolean cached = structureCache.get(entry);

        if (cached != null)
        {
            cacheHits.incrementAndGet();
            return cached;
        }

        cacheMisses.incrementAndGet();
        return loadStructureData(world, structure, dimension, entry);
    }

    private boolean loadStructureData(World world, String structure, int dimension, StructureEntry entry)
    {
        try
        {
            MapGenStructureData mapGenStructureData =
                    (MapGenStructureData) world.getPerWorldStorage()
                            .getOrLoadData(MapGenStructureData.class, structure);

            if (mapGenStructureData == null)
            {
                structureCache.put(entry, false);
                return false;
            }

            Set<Long> chunks = parseStructureData(mapGenStructureData);

            chunks.forEach(chunk ->
                    structureCache.put(new StructureEntry(structure, dimension, chunk), true));

            return structureCache.containsKey(entry);
        }
        catch (Exception exception)
        {
            //DSCInlineDebugStats.getInstance().logStructureError(structure, exception);

            return false;
        }
    }

    private static Set<Long> parseStructureData(MapGenStructureData mapGenStructureData)
    {
        Set<Long> chunks = new HashSet<>();
        NBTTagCompound nbt = mapGenStructureData.getTagCompound();

        for (String key : nbt.getKeySet())
        {
            NBTBase nbtBase = nbt.getTag(key);

            if (nbtBase.getId() == 10)
            {
                NBTTagCompound compound = (NBTTagCompound) nbtBase;

                if (compound.hasKey("ChunkX") && compound.hasKey("ChunkZ"))
                {
                    chunks.add(ChunkPos.asLong(
                            compound.getInteger("ChunkX"),
                            compound.getInteger("ChunkZ")
                    ));
                }
            }
        }
        return chunks;
    }

    public int getCacheSize() { return structureCache.size(); }
    public int getCacheHits() { return cacheHits.get(); }
    public int getCacheMisses() { return cacheMisses.get(); }
    public long getLastCleanTime() { return lastCleanTime.get(); }
    public double getCacheHitRate()
    {
        int total = cacheHits.get() + cacheMisses.get();
        return total > 0 ? (double) cacheHits.get() / total : 0;
    }
}
