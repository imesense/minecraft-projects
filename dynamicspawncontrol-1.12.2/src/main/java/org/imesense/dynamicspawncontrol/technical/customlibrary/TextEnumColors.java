package org.imesense.dynamicspawncontrol.technical.customlibrary;

/**
 *
 */
public enum TextEnumColors
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
    TextEnumColors(String code)
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
