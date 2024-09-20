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
    private final String description;

    /**
     *
     * @param description
     */
    EnumCmdCalledType(String description)
    {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public String getDescription()
    {
        return this.description;
    }
}