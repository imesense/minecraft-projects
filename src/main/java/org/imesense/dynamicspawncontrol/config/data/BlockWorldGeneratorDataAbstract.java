package org.imesense.dynamicspawncontrol.config.data;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

/**
 *
 */
public abstract class BlockWorldGeneratorDataAbstract
{
    /**
     *
     */
    private final String CATEGORY;

    /**
     *
     */
    private Integer chanceSpawn;

    /**
     *
     */
    private Integer minHeight;

    /**
     *
     */
    private Integer maxHeight;

    /**
     *
     * @param category
     * @param chanceSpawn
     * @param minHeight
     * @param maxHeight
     */
    public BlockWorldGeneratorDataAbstract(String category, Integer chanceSpawn, Integer minHeight, Integer maxHeight)
    {
        this.CATEGORY = category;
        this.chanceSpawn = chanceSpawn;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;

        CodeGeneric.printInitClassToLog(this.getClass());
    }

    /**
     *
     * @param value
     */
    public void setChanceSpawn(Integer value)
    {
        this.chanceSpawn = value;
    }

    /**
     *
     * @param value
     */
    public void setMinHeight(Integer value)
    {
        this.minHeight = value;
    }

    /**
     *
     * @param value
     */
    public void setMaxHeight(Integer value)
    {
        this.maxHeight = value;
    }

    /**
     *
     * @return
     */
    public Integer getChanceSpawn()
    {
        return this.chanceSpawn;
    }

    /**
     *
     * @return
     */
    public Integer getMinHeight()
    {
        return this.minHeight;
    }

    /**
     *
     * @return
     */
    public Integer getMaxHeight()
    {
        return this.maxHeight;
    }

    /**
     *
     * @return
     */
    public String getCategoryObject()
    {
        return this.CATEGORY;
    }
}
