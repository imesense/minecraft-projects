package org.imesense.dynamicspawncontrol.technical.gamestructures;

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
public class Structures
{
    /**
     *
     */
    public static final Structures StructuresCache = new Structures();

    /**
     *
     */
    private final Map<StructureEntry, Boolean> structuresCacheHashMap = new HashMap<>();

    /**
     *
     */
    public void clean()
    {
        structuresCacheHashMap.clear();
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
        ChunkPos objectChunkPos = new ChunkPos(blockPos);
        long longChunkPos = ChunkPos.asLong(objectChunkPos.x, objectChunkPos.z);
        StructureEntry entry = new StructureEntry(structure, dimension, longChunkPos);

        if (structuresCacheHashMap.containsKey(entry))
        {
            return structuresCacheHashMap.get(entry);
        }

        MapGenStructureData data = (MapGenStructureData) world.getPerWorldStorage().getOrLoadData(MapGenStructureData.class, structure);

        if (data == null)
        {
            return false;
        }

        Set<Long> longs = parseStructureData(data);

        for (Long _long : longs)
        {
            structuresCacheHashMap.put(new StructureEntry(structure, dimension, _long), true);
        }

        if (structuresCacheHashMap.containsKey(entry))
        {
            return true;
        }
        else
        {
            structuresCacheHashMap.put(entry, false);
            return false;
        }
    }

    /**
     *
     * @param data
     * @return
     */
    private static Set<Long> parseStructureData(MapGenStructureData data)
    {
        Set<Long> chunks = new HashSet<>();
        NBTTagCompound nbttagcompound = data.getTagCompound();

        for (String _getStringNBT : nbttagcompound.getKeySet())
        {
            NBTBase nbtbase = nbttagcompound.getTag(_getStringNBT);

            if (nbtbase.getId() == 10)
            {
                NBTTagCompound _nbtBase = (NBTTagCompound) nbtbase;

                if (_nbtBase.hasKey("ChunkX") && _nbtBase.hasKey("ChunkZ"))
                {
                    int i = _nbtBase.getInteger("ChunkX");
                    int j = _nbtBase.getInteger("ChunkZ");

                    chunks.add(ChunkPos.asLong(i, j));
                }
            }
        }

        return chunks;
    }
}
