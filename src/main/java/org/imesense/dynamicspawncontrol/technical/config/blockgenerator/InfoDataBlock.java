package org.imesense.dynamicspawncontrol.technical.config.blockgenerator;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

/**
 *
 */
public abstract class InfoDataBlock
{
    /**
     *
     */
    private final String category;

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
    public InfoDataBlock(String category, Integer chanceSpawn, Integer minHeight, Integer maxHeight)
    {
        this.category = category;
        this.chanceSpawn = chanceSpawn;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;

        CodeGenericUtils.printInitClassToLog(this.getClass());
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
     * @param chanceSpawn
     */
    public void setChanceSpawn(Integer chanceSpawn)
    {
        this.chanceSpawn = chanceSpawn;
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
     * @param minHeight
     */
    public void setMinHeight(Integer minHeight)
    {
        this.minHeight = minHeight;
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
     * @param maxHeight
     */
    public void setMaxHeight(Integer maxHeight)
    {
        this.maxHeight = maxHeight;
    }

    /**
     *
     * @return
     */
    public String getCategoryObject()
    {
        return this.category;
    }
}
