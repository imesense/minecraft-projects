package org.imesense.dynamicspawncontrol.technical.gamestructure;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public final class Structure
{
    /**
     *
     */
    public static final Structure STRUCTURES_CACHE = new Structure();

    /**
     *
     */
    private final Map<StructureEntry, Boolean> STRUCTURE_HASH = new HashMap<>();

    /**
     *
     */
    public void clean()
    {
        STRUCTURE_HASH.clear();
    }

    /**
     *
     * @param world
     * @param structure
     * @param blockPos
     * @return
     */
    public boolean isInStructure(World world, String structure, BlockPos blockPos)
    {
        int dimension = world.provider.getDimension();

        ChunkPos chunkPos = new ChunkPos(blockPos);

        long longChunkPos = ChunkPos.asLong(chunkPos.x, chunkPos.z);
        StructureEntry entry = new StructureEntry(structure, dimension, longChunkPos);

        if (STRUCTURE_HASH.containsKey(entry))
        {
            return STRUCTURE_HASH.get(entry);
        }

        MapGenStructureData mapGenStructureData =
                (MapGenStructureData) world.getPerWorldStorage().getOrLoadData(MapGenStructureData.class, structure);

        if (mapGenStructureData == null)
        {
            return false;
        }

        Set<Long> longs = parseStructureData(mapGenStructureData);

        for (Long _long : longs)
        {
            STRUCTURE_HASH.put(new StructureEntry(structure, dimension, _long), true);
        }

        if (STRUCTURE_HASH.containsKey(entry))
        {
            return true;
        }
        else
        {
            STRUCTURE_HASH.put(entry, false);
            return false;
        }
    }

    /**
     *
     * @param mapGenStructureData
     * @return
     */
    private static Set<Long> parseStructureData(MapGenStructureData mapGenStructureData)
    {
        Set<Long> chunks = new HashSet<>();
        NBTTagCompound nbtTagCompound = mapGenStructureData.getTagCompound();

        for (String getStringNBT : nbtTagCompound.getKeySet())
        {
            NBTBase nbtbase = nbtTagCompound.getTag(getStringNBT);

            if (nbtbase.getId() == 10)
            {
                NBTTagCompound nbtTagCompound1 = (NBTTagCompound) nbtbase;

                if (nbtTagCompound1.hasKey("ChunkX") && nbtTagCompound1.hasKey("ChunkZ"))
                {
                    int i = nbtTagCompound1.getInteger("ChunkX");
                    int j = nbtTagCompound1.getInteger("ChunkZ");

                    chunks.add(ChunkPos.asLong(i, j));
                }
            }
        }

        return chunks;
    }
}
