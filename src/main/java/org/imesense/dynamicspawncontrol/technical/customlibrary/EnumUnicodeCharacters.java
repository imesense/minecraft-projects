package org.imesense.dynamicspawncontrol.technical.customlibrary;

/**
 *
 */
public enum EnumUnicodeCharacters
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
    private final char character;

    /**
     *
     */
    private final String description;

    /**
     *
     * @param character
     * @param description
     */
    EnumUnicodeCharacters(char character, String description)
    {
        this.character = character;
        this.description = description;
    }

    /**
     *
     * @return
     */
    public char getCharacter()
    {
        return character;
    }

    /**
     *
     * @return
     */
    public String getDescription()
    {
        return description;
    }

    /**
     *
     * @param character
     * @return
     */
    public static EnumUnicodeCharacters fromCharacter(char character)
    {
        for (EnumUnicodeCharacters unicodeCharacter : values())
        {
            if (unicodeCharacter.character == character)
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
        return String.format("%s ('%c')", this.description, this.character);
    }
}
