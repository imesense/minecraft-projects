package org.imesense.dynamicspawncontrol.core.collection;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class UnicodeCharacterCollection
{
    /**
     *
     */
    public static UnicodeCharacterCollection instance;

    /**
     *
     */
    private static final Map<Character, String> UNICODE_CHARACTERS;

    /**
     *
     */
    static
    {
        Map<Character, String> characters = new HashMap<>();

        characters.put(' ', "White Space");
        characters.put('\u00A7', "Section Symbol");
        characters.put('\u00A9', "Copyright Symbol");
        characters.put('\u00AE', "Registered Trademark Symbol");
        characters.put('\u2022', "Bullet Point");
        characters.put('\u20AC', "Euro Currency Symbol");
        characters.put('\u00A3', "Pound Currency Symbol");

        UNICODE_CHARACTERS = Collections.unmodifiableMap(characters);
    }

    /**
     *
     */
    public UnicodeCharacterCollection()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    /**
     *
     * @param character
     * @return
     */
    public String getDescription(char character)
    {
        return UNICODE_CHARACTERS.get(character);
    }

    /**
     *
     * @param description
     * @return
     */
    public Character getCharacter(String description)
    {
        for (Map.Entry<Character, String> entry : UNICODE_CHARACTERS.entrySet())
        {
            if (entry.getValue().equalsIgnoreCase(description))
            {
                return entry.getKey();
            }
        }

        return null;
    }

    /**
     *
     * @param character
     * @return
     */
    public String toString(char character)
    {
        String description = getDescription(character);
        return description != null ? String.format("%s ('%c')", description, character) : "Unknown character";
    }
}
