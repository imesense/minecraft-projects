package org.imesense.dynamicspawncontrol.core.text;

/**
 *
 */
public enum UnicodeCharacter
{
    WHITE_SPACE(' ', "White Space"),
    SECTION('\u00A7', "Section Symbol"),
    COPYRIGHT('\u00A9', "Copyright Symbol"),
    REGISTERED('\u00AE', "Registered Trademark Symbol"),
    BULLET('\u2022', "Bullet Point"),
    EURO('\u20AC', "Euro Currency Symbol"),
    POUND('\u00A3', "Pound Currency Symbol");

    private final char character;
    private final String description;

    UnicodeCharacter(char character, String description)
    {
        this.character = character;
        this.description = description;
    }

    public char getChar()
    {
        return character;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public String toString()
    {
        return description + " ('" + character + "')";
    }

    public static UnicodeCharacter fromDescription(String desc)
    {
        for (UnicodeCharacter uc : values())
        {
            if (uc.description.equalsIgnoreCase(desc))
            {
                return uc;
            }
        }

        return null;
    }
}
