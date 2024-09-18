package org.imesense.dynamicspawncontrol.technical.gamestructures;

import javax.annotation.Nonnull;

/**
 *
 */
public class StructureEntry
{
    /**
     *
     */
    private final int dimension;

    /**
     *
     */
    private final long chunkPos;

    /**
     *
     */
    private final String structure;

    /**
     *
     * @param structure
     * @param dimension
     * @param chunkPos
     */
    public StructureEntry(@Nonnull String structure, int dimension, long chunkPos)
    {
        this.structure = structure;
        this.dimension = dimension;
        this.chunkPos = chunkPos;
    }

    /**
     *
     * @return
     */
    @Nonnull
    public String getStructure()
    {
        return structure;
    }

    /**
     *
     * @return
     */
    public int getDimension()
    {
        return dimension;
    }

    /**
     *
     * @return
     */
    public long getChunkPos()
    {
        return chunkPos;
    }

    /**
     *
     * @param object
     * @return
     */
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

        if (this.dimension != that.dimension)
        {
            return false;
        }

        if (this.chunkPos != that.chunkPos)
        {
            return false;
        }

        return this.structure.equals(that.structure);
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode()
    {
        int result = this.structure.hashCode();

        result = 31 * result + this.dimension;
        result = 31 * result + (int) (this.chunkPos ^ (this.chunkPos >>> 32));

        return result;
    }
}
