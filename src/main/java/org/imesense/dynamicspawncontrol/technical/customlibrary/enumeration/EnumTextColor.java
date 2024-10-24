package org.imesense.dynamicspawncontrol.technical.customlibrary.enumeration;

/**
 *
 */
public enum EnumTextColor
{
    /**
     *
     */
    RED("c"),

    /**
     *
     */
    AQUA("b"),

    /**
     *
     */
    GOLD("6"),

    /**
     *
     */
    GRAY("7"),

    /**
     *
     */
    BLUE("9"),

    /**
     *
     */
    WHITE("f"),

    /**
     *
     */
    GREEN("a"),

    /**
     *
     */
    BLACK("0"),

    /**
     *
     */
    PURPLE("d"),

    /**
     *
     */
    YELLOW("e");

    private final String CODE;

    /**
     *
     * @param CODE
     */
    EnumTextColor(final String CODE)
    {
        this.CODE = CODE;
    }

    /**
     *
     * @return
     */
    public String getCode()
    {
        return this.CODE;
    }
}
