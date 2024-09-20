package org.imesense.dynamicspawncontrol.technical.customlibrary;

/**
 *
 */
public enum EnumTextColors
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

    private final String code;

    /**
     *
     * @param code
     */
    EnumTextColors(String code)
    {
        this.code = code;
    }

    /**
     *
     * @return
     */
    public String getCode()
    {
        return this.code;
    }
}
