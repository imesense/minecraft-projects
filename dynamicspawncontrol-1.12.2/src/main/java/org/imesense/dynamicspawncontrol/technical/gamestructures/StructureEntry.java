package org.imesense.dynamicspawncontrol.technical.gamestructures;

import javax.annotation.Nonnull;

/**
 *
 */
public final class StructureEntry
{
    /**
     *
     */
    private final int DIMENSION;

    /**
     *
     */
    private final long CHUNK_POS;

    /**
     *
     */
    private final String STRUCTURE;

    /**
     *
     * @param structure
     * @param dimension
     * @param chunkPos
     */
    public StructureEntry(@Nonnull String structure, int dimension, long chunkPos)
    {
        this.STRUCTURE = structure;
        this.DIMENSION = dimension;
        this.CHUNK_POS = chunkPos;
    }

    /**
     *
     * @return
     */
    @Nonnull
    public String getStructure()
    {
        return this.STRUCTURE;
    }

    /**
     *
     * @return
     */
    public int getDimension()
    {
        return this.DIMENSION;
    }

    /**
     *
     * @return
     */
    public long getChunkPos()
    {
        return this.CHUNK_POS;
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

        if (this.DIMENSION != that.DIMENSION)
        {
            return false;
        }

        if (this.CHUNK_POS != that.CHUNK_POS)
        {
            return false;
        }

        return this.STRUCTURE.equals(that.STRUCTURE);
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode()
    {
        int result = this.STRUCTURE.hashCode();

        result = 31 * result + this.DIMENSION;
        result = 31 * result + (int) (this.CHUNK_POS ^ (this.CHUNK_POS >>> 32));

        return result;
    }
}
