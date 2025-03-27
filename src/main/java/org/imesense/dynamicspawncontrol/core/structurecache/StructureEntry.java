package org.imesense.dynamicspawncontrol.core.structurecache;

import lombok.Getter;

import javax.annotation.Nonnull;

public final class StructureEntry
{
    @Getter
    private final int dimension;

    @Getter
    private final long chunkPos;

    private final String structure;

    @Getter
    private final long lastAccessTime;

    public StructureEntry(@Nonnull String structure, int dimension, long chunkPos)
    {
        this.structure = structure;
        this.dimension = dimension;
        this.chunkPos = chunkPos;
        this.lastAccessTime = System.currentTimeMillis();
    }

    @Nonnull
    public String getStructure()
    {
        return this.structure;
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }

        if (object == null || getClass() != object.getClass())
        {
            return false;
        }

        StructureEntry that = (StructureEntry) object;

        return dimension == that.dimension &&
                chunkPos == that.chunkPos &&
                structure.equals(that.structure);
    }

    @Override
    public int hashCode()
    {
        int result = structure.hashCode();

        result = 31 * result + dimension;
        result = 31 * result + (int) (chunkPos ^ (chunkPos >>> 32));
        
        return result;
    }
}
