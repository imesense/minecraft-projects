package org.imesense.dynamicspawncontrol.technical.customlibrary.enumeration;

/**
 *
 */
public enum EnumUnicodeCharacter
{
    /**
     *
     */
    WHITE_SPACE(' ', "White Space"),

    /**
     *
     */
    SECTION('\u00A7', "Section Symbol"),

    /**
     *
     */
    COPYRIGHT('\u00A9', "Copyright Symbol"),

    /**
     *
     */
    REGISTERED('\u00AE', "Registered Trademark Symbol"),

    /**
     *
     */
    BULLET('\u2022', "Bullet Point"),

    /**
     *
     */
    EURO('\u20AC', "Euro Currency Symbol"),

    /**
     *
     */
    POUND('\u00A3', "Pound Currency Symbol");

    /**
     *
     */
    private final char CHARACTER;

    /**
     *
     */
    private final String DESCRIPTION;

    /**
     *
     * @param CHARACTER
     * @param DESCRIPTION
     */
    EnumUnicodeCharacter(final char CHARACTER, final String DESCRIPTION)
    {
        this.CHARACTER = CHARACTER;
        this.DESCRIPTION = DESCRIPTION;
    }

    /**
     *
     * @return
     */
    public char getCharacter()
    {
        return this.CHARACTER;
    }

    /**
     *
     * @return
     */
    public String getDescription()
    {
        return this.DESCRIPTION;
    }

    /**
     *
     * @param character
     * @return
     */
    public static EnumUnicodeCharacter fromCharacter(char character)
    {
        for (EnumUnicodeCharacter unicodeCharacter : values())
        {
            if (unicodeCharacter.CHARACTER == character)
            {
                return unicodeCharacter;
            }
        }

        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString()
    {
        return String.format("%s ('%c')", this.DESCRIPTION, this.CHARACTER);
    }
}
