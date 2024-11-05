package org.imesense.dynamicspawncontrol.config.data;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.annotation.Getter;
import org.imesense.dynamicspawncontrol.core.annotation.Setter;

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
    @Setter
    public void setChanceSpawn(Integer value)
    {
        this.chanceSpawn = value;
    }

    /**
     *
     * @param value
     */
    @Setter
    public void setMinHeight(Integer value)
    {
        this.minHeight = value;
    }

    /**
     *
     * @param value
     */
    @Setter
    public void setMaxHeight(Integer value)
    {
        this.maxHeight = value;
    }

    /**
     *
     * @return
     */
    @Getter
    public Integer getChanceSpawn()
    {
        return this.chanceSpawn;
    }

    /**
     *
     * @return
     */
    @Getter
    public Integer getMinHeight()
    {
        return this.minHeight;
    }

    /**
     *
     * @return
     */
    @Getter
    public Integer getMaxHeight()
    {
        return this.maxHeight;
    }

    /**
     *
     * @return
     */
    @Getter
    public String getCategoryObject()
    {
        return this.CATEGORY;
    }
}
