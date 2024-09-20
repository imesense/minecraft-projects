package org.imesense.dynamicspawncontrol.technical.customlibrary;

/**
 *
 */
public enum EnumCmdCalledType
{
    /**
     *
     */
    HIT("[Hit]"),

    /**
     *
     */
    CMD("[Command]"),

    /**
     *
     */
    TIMER("[Timer]"),

    /**
     *
     */
    SIGNAL("[Signal]"),

    /**
     *
     */
    MESSAGE("[Message]"),

    /**
     *
     */
    ITEM_DROP("[Item drop]");

    /**
     *
     */
    private final String DESCRIPTION;

    /**
     *
     * @param description
     */
    EnumCmdCalledType(String description)
    {
        this.DESCRIPTION = description;
    }

    /**
     *
     * @return
     */
    public String getDescription()
    {
        return this.DESCRIPTION;
    }
}
