package org.imesense.dynamicspawncontrol.technical.customlibrary.enumeration;

/**
 *
 */
public enum EnumCmdCallType
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
     * @param DESCRIPTION
     */
    EnumCmdCallType(final String DESCRIPTION)
    {
        this.DESCRIPTION = DESCRIPTION;
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
