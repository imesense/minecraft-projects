package org.imesense.dynamicspawncontrol.core.collection;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class TextColorCollection
{
    /**
     *
     */
    public static TextColorCollection instance;

    /**
     *
     */
    private static final Map<String, String> TEXT_COLORS;

    /**
     *
     */
    public TextColorCollection()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    /**
     *
     */
    static
    {
        Map<String, String> colors = new HashMap<>();

        colors.put("RED", "c");
        colors.put("AQUA", "b");
        colors.put("GOLD", "6");
        colors.put("GRAY", "7");
        colors.put("BLUE", "9");
        colors.put("WHITE", "f");
        colors.put("GREEN", "a");
        colors.put("BLACK", "0");
        colors.put("PURPLE", "d");
        colors.put("YELLOW", "e");

        TEXT_COLORS = Collections.unmodifiableMap(colors);
    }

    /**
     *
     * @param colorName
     * @return
     */
    public String getCode(String colorName)
    {
        return TEXT_COLORS.get(colorName.toUpperCase());
    }
}
