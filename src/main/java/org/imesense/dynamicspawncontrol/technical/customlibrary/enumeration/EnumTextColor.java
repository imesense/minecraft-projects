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
     * @param code
     */
    EnumTextColor(final String code)
    {
        this.CODE = code;
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
